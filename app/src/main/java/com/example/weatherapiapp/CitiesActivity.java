package com.example.weatherapiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapiapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CitiesActivity extends AppCompatActivity {

    private static final String TAG = "CitiesActivity";

    private ConstraintLayout citiesLayout;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModelShort> citiesList;
    private AppCompatEditText etCityInput;

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
            getForecastShort();
            Snackbar snackbar = Snackbar.make(citiesLayout, "City added", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        });

    } //onCreate

    // Cities layout methods
    private void prepareData() {
        WeatherReportModelShort weatherReportModelShort = new WeatherReportModelShort();
        citiesList.add(weatherReportModelShort);

        adapter.notifyDataSetChanged();
    }

    private void addCity() {
        WeatherReportModelShort weatherReportModelShort = new WeatherReportModelShort();
        citiesList.add(weatherReportModelShort);

        adapter.notifyDataSetChanged();
    }

    private void getForecastShort() {

        WeatherDataService weatherDataService = new WeatherDataService(CitiesActivity.this);

        weatherDataService.getForecastByName(Objects.requireNonNull(etCityInput.getText()).toString(),
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

                        adapter.notifyDataSetChanged();
                    }
                });
    } // getForecastShort

    // Callback for recycler view ItemTouchHelper for onSwiped
    ItemTouchHelper.Callback callbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            citiesList.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            Snackbar snackbar = Snackbar.make(citiesLayout, "City deleted", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }
    };

}
