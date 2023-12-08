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
    private Set<String> citiesCountriesNamesSet;
//    private LinkedHashSet<String> citiesCountriesNamesSet;
    private LinkedHashSet<String> linkedHashSet;
//    private Set<String> linkedHashSet;

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

        linkedHashSet = new LinkedHashSet<>();

        // get the stored cities preferences set (citiesCountriesNamesSet). Otherwise, initialize a new one.
        // Used LinkedHasSet instead of a HashSet of TreeSet to maintain the cities' order.
        citiesCountriesNamesSet = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                .getStringSet(
                        "citiesCountries",
                        new LinkedHashSet<>());
        citiesCountriesNamesSet = new LinkedHashSet<>();
        Log.d(TAG, "prepareData: Set "+ citiesCountriesNamesSet);
        Log.d(TAG, "prepareData: list " + citiesList);

                for (String cityCountryName : citiesCountriesNamesSet) {
                    getForecastShort(cityCountryName);
                    Log.d(TAG, "handler");
                    synchronized (this) {
                        try {
                            wait(80);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                String currentThreadName = Thread.currentThread().getName();
//                Log.i(TAG, "This thread is: " + currentThreadName);
//
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
//
//            }
//        });
        Log.i(TAG, "thread " + Thread.currentThread().getName());
        Log.d(TAG, "prepareData: Set " +citiesCountriesNamesSet);
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

                        citiesCountriesNamesSet.add(weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry());
                        linkedHashSet.add(weatherReportModelShort.getCity() + " - " + weatherReportModelShort.getCountry());
//                        Set<String> tempCitySet = new LinkedHashSet<>();
//                        tempCitySet.addAll(citiesCountriesNamesSet);
//                        Set<String> tempCitySet = new LinkedHashSet<>(citiesCountriesNamesSet);
//                        Log.d(TAG, "onResponse: temp set " +tempCitySet);
                        Log.d(TAG, "onResponse: linkedHashSet " +linkedHashSet);
                        Log.d(TAG, "onResponse: set "+citiesCountriesNamesSet);

                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                .edit()
                                .putStringSet("citiesCountries", citiesCountriesNamesSet)
                                .apply();
                        Log.d(TAG, "onResponse: set "+citiesCountriesNamesSet);

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
                    citiesCountriesNamesSet.remove(cityCountryName);
                    getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            .edit()
                            .putStringSet("citiesCountries", citiesCountriesNamesSet)
                            .apply();

                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    Snackbar snackbar = Snackbar.make(citiesLayout, "City removed", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
            };

}
