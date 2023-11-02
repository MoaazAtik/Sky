package com.example.weatherapiapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapiapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CitiesActivity extends AppCompatActivity {

    private ConstraintLayout citiesLayout;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModel> citiesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cities);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cities);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cities);
        citiesList = new ArrayList<>();
        adapter = new CityListAdapter(this, citiesList);
        citiesLayout = findViewById(R.id.cities_layout);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper =  new ItemTouchHelper(callbackItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        prepareData();

        findViewById(R.id.btn_cities_add_city).setOnClickListener(view -> {
            addCity();
            Snackbar snackbar = Snackbar.make(citiesLayout, "City Added", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        });

    }

    // Cities layout methods
    private void prepareData() {
        String[] times = new String[]{
                "11", "222"
        };

        WeatherReportModel weatherReportModel = new WeatherReportModel();
        weatherReportModel.setTime(times[0]);
        citiesList.add(weatherReportModel);

        adapter.notifyDataSetChanged();
    }

    private void addCity() {
        WeatherReportModel weatherReportModel = new WeatherReportModel();
        weatherReportModel.setTime("333");
        citiesList.add(weatherReportModel);

        adapter.notifyDataSetChanged();
    }

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
            Snackbar snackbar = Snackbar.make(citiesLayout, "City Deleted", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }
    };

}
