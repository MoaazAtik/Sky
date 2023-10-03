package com.example.weatherapiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.weatherapiapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

//#freeCodeCamp.org (YT) | REST API - Network Data
public class MainActivity extends AppCompatActivity {

    /*
     Get city latitude and longitude from nominatim.org, then get the city's weather from open-meteo.com
     I used city latitude and longitude instead of city id.
     */

    ActivityMainBinding binding;

    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModel> citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
//        setContentView(view);

        setContentView(R.layout.cities);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cities);
        citiesList = new ArrayList<>();
        adapter = new CityListAdapter(this, citiesList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        prepareData();

        findViewById(R.id.btn_cities_add_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCity();
            }
        });


        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);


        binding.btnGetCityLatL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                weatherDataService.getCityLatL(binding.etDataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(float cityLat2, float cityLon) {
                        //↑ this doesn't do anything if it's not called from Api response
                        /*
                        public void getCityLatL(String cityName, VolleyResponseListener volleyResponseListener) {...

                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                                @Override
                                public void onResponse(JSONArray response) {

                           ***         volleyResponseListener.onResponse(cityLat, cityLon);
                         */

                        Toast.makeText(MainActivity.this, "Lat: " + cityLat2 + "\nLon: " + cityLon, Toast.LENGTH_LONG).show();
                    }
                });


            }//onClick
        });//setOnClickListener btn_getCityLatL


        binding.btnGetWeatherByLatL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float cityLat = 0, cityLon = 0;

                try {
                    cityLat = Float.parseFloat(binding.etDataInput.getText().toString().substring(0, binding.etDataInput.getText().toString().indexOf(",")).trim());
                    cityLon = Float.parseFloat(binding.etDataInput.getText().toString().substring(binding.etDataInput.getText().toString().indexOf(",") + 1).trim());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }


                weatherDataService.getCityForecastByLatL(cityLat, cityLon, new WeatherDataService.ForecastByLatLResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                        binding.lvWeatherReports.setAdapter(arrayAdapter);
                    }
                });

            }//onClick
        });//setOnClickListener  btn_getWeatherByLatL


        binding.btnGetWeatherByCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                weatherDataService.getCityForecastByName(binding.etDataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
                    @Override
                    public void onError(String message) {

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {

                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                        binding.lvWeatherReports.setAdapter(arrayAdapter);

                    }
                });

            }
        });//setOnClickListener btn_getWeatherByName

    }//onCreate


    private void prepareData() {
        String[] times = new String[] {
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
}