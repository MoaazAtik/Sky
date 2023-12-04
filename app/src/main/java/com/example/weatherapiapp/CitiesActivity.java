package com.example.weatherapiapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;
import java.util.Objects;

public class CitiesActivity extends AppCompatActivity {

    private static final String TAG = "CitiesActivity";

    private ConstraintLayout citiesLayout;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModelShort> citiesList;
    private AppCompatEditText etCityInput;
    private MyItemTouchHandler itemTouchHandler;

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

//        MyItemTouchHandler itemTouchHandler = new MyItemTouchHandler(this, recyclerView, adapter, citiesList);
        itemTouchHandler = new MyItemTouchHandler(this, recyclerView, adapter, citiesList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHandler);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.addOnItemTouchListener(itemTouchHandler);

////        registerForContextMenu(recyclerView.showContextMenuForChild());
//        registerForContextMenu(etCityInput);
////        recyclerView.showContextMenuForChild(recyclerView);

        registerForContextMenu(recyclerView);
        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                // take this to the handler or adapter
            }
        });
//        holder.view.setOnCreateContextMenuListener { contextMenu, _, _ ->
//                contextMenu.add("Add").setOnMenuItemClickListener {
//            longToast("I'm pressed for the item at position => $position")
//            true
//        }
//        }
        MenuItem.OnMenuItemClickListener m = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                return false;
            }
        };

        prepareData();

        findViewById(R.id.btn_cities_add_city).setOnClickListener(view -> {
//            addCity();
            getForecastShort();
            Snackbar snackbar = Snackbar.make(citiesLayout, "City added", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        });

    } //onCreate

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
////        super.onCreateContextMenu(menu, v, menuInfo);
//        Log.d(TAG, "onCreateContextMenu: ");
//        menu.add("Set as home")
//                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(@NonNull MenuItem item) {
//                        Log.d(TAG, "onMenuItemClick: 1");
//                        return false;
//                    }
//                });
//        menu.add("Remove city")
//                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(@NonNull MenuItem item) {
//                        Log.d(TAG, "onMenuItemClick: 2");
//                        return false;
//                    }
//                });
//    }

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


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        int position = item.getOrder();
//        switch (item.getItemId()) {
//        switch (position) {
//        int position = recyclerView.getChildAdapterPosition(recyclerView.childgetFocusedChild());
//        CityListAdapter adapter = (CityListAdapter) recyclerView.getAdapter();
//        recyclerView.findChildViewUnder()
//        adapter.vieho
//        Log.d(TAG, "onContextItemSelected: position " + position);
        switch (item.getOrder()) {
            case 0:
                Log.d(TAG, "onContextItemSelected: 0");
                return true;
            case 1:
                Log.d(TAG, "onContextItemSelected: 1");
//                Log.d(TAG, "Current rv item position " + itemTouchHandler.getCurrentViewPosition());
//                removeItem(itemTouchHandler.getCurrentViewPosition());
                int p = adapter.getPosition();
                Log.d(TAG, "Current rv item position " + p);
                removeItem(p);
                return true;
        }
        Log.d(TAG, "onContextItemSelected: default");
        return super.onContextItemSelected(item);
    }

    private void removeItem(int position) {
        citiesList.remove(position);
        adapter.notifyItemRemoved(position);
        // Show a confirmation message or perform other actions
    }

}
