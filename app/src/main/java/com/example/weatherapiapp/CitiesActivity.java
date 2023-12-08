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
                while (!Objects.equals(tempNames, "")) {
                    int lastIndex = tempNames.indexOf(" ; ");
                    currentName = tempNames.substring(0, lastIndex);
                    tempNames = tempNames.substring(lastIndex + 3);
                    getForecastShort(currentName);
                    synchronized (this) {
                        try {
                            // Earlier Calls are not always providing earlier Responses which is Mixing the cities Order. To fix this I let the worker (background) Thread to Wait between API Calls. This gives the calls some time to respond to ensures getting Responses in the order of Calling.
                            wait(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void addCity() {
        WeatherReportModelShort weatherReportModelShort = new WeatherReportModelShort();
        citiesList.add(weatherReportModelShort);

        adapter.notifyDataSetChanged();
    }

    private void getForecastShort(String cityCountry) {

        Log.d(TAG, "getForecastShort: Call City " + cityCountry);
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
                        citiesCountriesNames +=
                                weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry()
                                + " ; ";
                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("citiesCountriesNames", citiesCountriesNames)
                                .apply();

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
                    String currentName = citiesList.get(currentPosition).getCity() + " - " + citiesList.get(currentPosition).getCountry()
                            + " ; ";
                    citiesList.remove(currentPosition);
                    citiesCountriesNames = citiesCountriesNames.replace(currentName, "");
                    getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("citiesCountriesNames", citiesCountriesNames)
                            .apply();

                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    Snackbar snackbar = Snackbar.make(citiesLayout, "City removed", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            };

}
