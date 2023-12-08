package com.example.weatherapiapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CitiesActivity extends AppCompatActivity {

    private static final String TAG = "CitiesActivity";

    private ConstraintLayout citiesLayout;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModelShort> citiesList;
    private AppCompatEditText etCityInput;
//    private Set<String> citiesCountriesNamesSet;
    private String citiesCountriesNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cities);
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

        findViewById(R.id.btn_cities_add_city).setOnClickListener(view -> {
//            addCity();
            getForecastShort(Objects.requireNonNull(etCityInput.getText()).toString());
            Snackbar snackbar = Snackbar.make(citiesLayout, "City added", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        });

    } //onCreate

    // Cities layout methods
    private void prepareData() {

        // get the stored cities preferences set (citiesCountriesNamesSet). Otherwise, initialize a new one.
        // Used LinkedHasSet instead of a HashSet of TreeSet to maintain the cities' order.
//        citiesCountriesNamesSet = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//                .getStringSet(
//                        "citiesCountries",
//                        new LinkedHashSet<>());
//        citiesCountriesNamesSet = new LinkedHashSet<>();
//        Log.d(TAG, "prepareData: Set "+ citiesCountriesNamesSet);
//        Log.d(TAG, "prepareData: list " + citiesList);

        citiesCountriesNames = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getString(
                        "citiesCountries",
                        ""
                );

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String currentThreadName = Thread.currentThread().getName();
                Log.i(TAG, "This thread is: " + currentThreadName);


                Log.d(TAG, "run: all cities |" + citiesCountriesNames);
                String tempCitiesNames = citiesCountriesNames;
                citiesCountriesNames = ""; // to avoid duplication after calling getForecastShort
                while (!Objects.equals(tempCitiesNames, "")) {
                    int i2 = tempCitiesNames.indexOf(" ; ");
                    Log.d(TAG, "run: i2 " +i2);
                    String sub = null;
                    sub = tempCitiesNames.substring(0, i2);
                    Log.d(TAG, "run: sub |"+sub);
                    tempCitiesNames = tempCitiesNames.substring(i2 + 3);
                    Log.d(TAG, "run: tempNames |" +tempCitiesNames);
                    getForecastShort(sub);
                    Log.d(TAG, "handler");
                    synchronized (this) {
                        try {
                            // Earlier Calls are not always providing earlier Responses which is Mixing the cities Order. To fix this I let the worker (background) Thread to Wait between API Calls. This gives the calls some time to respond to ensures getting Responses in the order of Calling.
                            wait(450);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Log.d(TAG, "run: tempCitiesNames.length() " +tempCitiesNames.length());
                }


//                String tempCitiesNames = citiesCountriesNames;
//                citiesCountriesNames = ""; // to avoid duplication after calling getForecastShort
//                Log.d(TAG, "run: all cities |" + citiesCountriesNames);
//                while (!Objects.equals(tempCitiesNames, "")) {
//                    int i2 = tempCitiesNames.indexOf(" ; ");
//                    Log.d(TAG, "run: i2 " +i2);
//                    String sub = null;
//                    sub = tempCitiesNames.substring(0, i2);
//                    Log.d(TAG, "run: sub |"+sub);
//                    tempCitiesNames = tempCitiesNames.substring(i2 + 3);
//                    Log.d(TAG, "run: tempNames |" +tempCitiesNames);
//                    getForecastShort(sub);
//                    Log.d(TAG, "run: tempCitiesNames.length() " +tempCitiesNames.length());
//                }


//                String tempCitiesNames = citiesCountriesNames;
//                Log.d(TAG, "run: all cities ." + citiesCountriesNames);
//                while (tempCitiesNames.length() > 3) {
//                    int i1 = tempCitiesNames.indexOf(" ; ") + 3;
//                    Log.d(TAG, "run: i1 " +i1);
//                    int i2 = tempCitiesNames.indexOf(" ; ", 3);
//                    Log.d(TAG, "run: i2 " +i2);
//                    String sub = null;
////                    if (i1 != -1) {
//                        sub = tempCitiesNames.substring(i1, i2);
//                        Log.d(TAG, "run: sub "+sub);
//                        tempCitiesNames = tempCitiesNames.substring(i2 + 3);
//                        Log.d(TAG, "run: tempNames " +tempCitiesNames);
////                    }
//                }


//                for (String cityCountryName : citiesCountriesNamesSet) {
//                    getForecastShort(cityCountryName);
//                    Log.d(TAG, "handler");
//                    synchronized (this) {
//                        try {
//                            wait(80);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }

            }
        });
        Log.i(TAG, "thread " + Thread.currentThread().getName());
//        Log.d(TAG, "prepareData: Set " +citiesCountriesNamesSet);
        Log.d(TAG, "prepareData: list " + citiesList);

//        WeatherReportModelShort weatherReportModelShort = new WeatherReportModelShort();
//        citiesList.add(weatherReportModelShort);
//
//        adapter.notifyDataSetChanged();
    }

    private void addCity() {
        WeatherReportModelShort weatherReportModelShort = new WeatherReportModelShort();
        citiesList.add(weatherReportModelShort);

        adapter.notifyDataSetChanged();
    }

    private void getForecastShort(String cityCountry) {

        Log.d(TAG, "getForecastShort: city " +cityCountry);
        WeatherDataService weatherDataService = new WeatherDataService(CitiesActivity.this);

        weatherDataService.getForecastByName(cityCountry,
                0, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelShort>() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(CitiesActivity.this, message, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onError: getForecastShort " + message);
                    }

                    @Override
                    public void onResponse(List<WeatherReportModelShort> weatherReportModels) {
                        Toast.makeText(CitiesActivity.this, "O  K", Toast.LENGTH_SHORT).show();
                        WeatherReportModelShort weatherReportModelShort = weatherReportModels.get(0);
                        citiesList.add(weatherReportModelShort);

//                        citiesCountriesNamesSet.add(weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry());
//                        Log.d(TAG, "onResponse: set "+citiesCountriesNamesSet);
//                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
//                                .edit()
//                                .putStringSet("citiesCountries", citiesCountriesNamesSet)
//                                .apply();
//                        Log.d(TAG, "onResponse: set "+citiesCountriesNamesSet);

//                        citiesCountriesNames += " ; "
//                                + weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry()
//                                + " ; ";

//                        String currentCity =
//                                weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry();
//                        citiesCountriesNames = currentCity // to avoid inverted order at app initialization
//                                + " ; "
//                                + citiesCountriesNames;

                        citiesCountriesNames +=
                                weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry()
                                + " ; ";
                        Log.d(TAG, "onResponse: names " + citiesCountriesNames);
                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("citiesCountries", citiesCountriesNames)
                                .apply();
                        Log.d(TAG, "onResponse: names " + citiesCountriesNames);

                        adapter.notifyDataSetChanged();
                    }
                });
    } // getForecastShort

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
                    int currentPosition = viewHolder.getAdapterPosition();
                    String cityCountryName = citiesList.get(currentPosition).getCity() + " - " + citiesList.get(currentPosition).getCountry();
                    citiesList.remove(currentPosition);
//                    citiesCountriesNamesSet.remove(cityCountryName);
//                    getSharedPreferences("MyPrefs", MODE_PRIVATE)
//                            .edit()
//                            .putStringSet("citiesCountries", citiesCountriesNamesSet)
//                            .apply();

                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    Snackbar snackbar = Snackbar.make(citiesLayout, "City removed", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            };

}
