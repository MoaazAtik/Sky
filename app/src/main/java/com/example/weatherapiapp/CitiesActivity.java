package com.example.weatherapiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CitiesActivity extends AppCompatActivity {

    private static final String TAG = "CitiesActivity";

    private ConstraintLayout citiesLayout;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModelShort> citiesList;
    private AppCompatEditText etCityInput;
    private String citiesCountriesNames;
    private boolean queueIsIdle = true;
    private boolean addingNewCity = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cities);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cities);
        citiesList = new ArrayList<>();
        adapter = new CityListAdapter(this, citiesList);
        citiesLayout = findViewById(R.id.cities_layout);
        etCityInput = findViewById(R.id.et_add_city);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callbackItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        prepareData();

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.btn_cities_add_city).setOnClickListener(view -> {
            addingNewCity = true;
            getForecastShort(Objects.requireNonNull(etCityInput.getText()).toString());
        });

    } //onCreate

    /**
     * Get Forecast details for a City weather Widget.
     * @param cityCountry Provided City and Country names to get the forecast details for.<p>
     *                    Note: City name is enough, but providing Country name too (separated by a Space or Comma) is Recommended.
     */
    private void getForecastShort(String cityCountry) {

        Log.d(TAG, "getForecastShort: Call City " + cityCountry);
        WeatherDataService weatherDataService = new WeatherDataService(CitiesActivity.this);
        weatherDataService.getForecastByName(
                cityCountry,
                0,
                new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelShort>() {
                    @Override
                    public void onError(String message) {
                        Log.d(TAG, "onError: getForecastShort " + message);
                        Toast.makeText(CitiesActivity.this, "Couldn't get City forecast", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModelShort> weatherReportModels) {
                        addCity(weatherReportModels);
                    }
                });
    } // getForecastShort

    /**
     * Create City widgets at CitiesActivity Initialization based on Saved Shared Preferences. The work is done on a Background Thread.
     */
    private void prepareData() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                // get the stored cities preferences (citiesCountriesNames). Otherwise, initialize a new one.
                citiesCountriesNames = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        .getString(
                                "citiesCountriesNames",
                                ""
                        );

                String tempNames = citiesCountriesNames;
                citiesCountriesNames = ""; // to avoid Duplication after calling getForecastShort
                String currentName;
                Queue<String> namesQueue = new LinkedList<>();
                long timeoutEnqueueingAndDequeueing = 2 * 60 * 1000; // 2 minutes
                long startEnqueueingTime = System.currentTimeMillis();
                while (!Objects.equals(tempNames, "")) {
                    int lastIndex = tempNames.indexOf(" ; ");
                    currentName = tempNames.substring(0, lastIndex);
                    tempNames = tempNames.substring(lastIndex + 3);
                    namesQueue.offer(currentName);
                    long elapsedEnqueueingTime = System.currentTimeMillis() - startEnqueueingTime;
                    if (elapsedEnqueueingTime > timeoutEnqueueingAndDequeueing) {
                        Log.d(TAG, "Timeout reached. Breaking the Enqueueing loop.");
                        Snackbar.make(recyclerView, "Timeout reached", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        break;
                    }
                }

                /*
                Earlier Calls are not always providing earlier Responses which is Mixing the cities Order.
                To fix this I utilized a Queue, created queueIsIdle boolean, and let the worker (background) Thread to Check repeatedly if the Queue is Idle API Calls.
                This gives the calls some time to respond to ensures getting Responses in the order of Calling.
                 */
                long startDequeueingTime = System.currentTimeMillis();
                while (!namesQueue.isEmpty()) {
                    if (queueIsIdle) {
                        queueIsIdle = false;
                        getForecastShort(namesQueue.poll());
                    }
                    synchronized (this) {
                        try {
                            wait(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    long elapsedDequeueingTime = System.currentTimeMillis() - startDequeueingTime;
                    if (elapsedDequeueingTime > timeoutEnqueueingAndDequeueing) {
                        Log.d(TAG, "Timeout reached. Breaking the Dequeueing loop.");
                        Snackbar.make(recyclerView, "Timeout reached", BaseTransientBottomBar.LENGTH_LONG)
                                .show();
                        break;
                    }
                }
            }
        });
    }

    /**
     * Add city to Recycler View and Shared Preferences.
     * @param weatherReportModels that was gotten from Response of getForecastShort.
     */
    private void addCity(List<WeatherReportModelShort> weatherReportModels) {
        WeatherReportModelShort weatherReportModelShort = weatherReportModels.get(0);

        /*
         Entering Invalid city name results in Catching 'JSONException: Index 0 out of range' in getCityLatL when Trying 'response.getJSONObject(0)' in WeatherDataService.
         This Check is to avoid such scenarios.
         */
        if (weatherReportModelShort.getCity() == null) {
            Snackbar.make(recyclerView, "Check city name", BaseTransientBottomBar.LENGTH_LONG)
                    .show();
            return;
        }

        citiesList.add(weatherReportModelShort);
        citiesCountriesNames +=
                weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry()
                        + " ; ";
        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .edit()
                .putString("citiesCountriesNames", citiesCountriesNames)
                .apply();

        adapter.notifyDataSetChanged();
        if (addingNewCity)
            Snackbar.make(citiesLayout, "City added", BaseTransientBottomBar.LENGTH_LONG)
                    .show();
        else // Preparing data at Activity Initialization
            queueIsIdle = true;
    }

    /**
     * Remove city from Recycler View and Shared Preferences.
     * @param position The position of City in Recycler View to be removed.
     */
    private void removeCity(int position) {
        String currentName = citiesList.get(position).getCity() + " - " + citiesList.get(position).getCountry()
                + " ; ";
        citiesList.remove(position);
        citiesCountriesNames = citiesCountriesNames.replace(currentName, "");
        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .edit()
                .putString("citiesCountriesNames", citiesCountriesNames)
                .apply();

        adapter.notifyItemRemoved(position);
        Snackbar snackbar = Snackbar.make(citiesLayout, "City removed", BaseTransientBottomBar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Callback of Recycler view's ItemTouchHelper to handle item swipes
     */
    ItemTouchHelper.Callback callbackItemTouchHelper =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    removeCity(viewHolder.getAdapterPosition());
                }
            };

}
