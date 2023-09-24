package com.example.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    Context context;
    float cityLat, cityLon;

    public static final String QUERY_FOR_CITY_LATL = "https://nominatim.openstreetmap.org/search?format=json&q=";
    public String QUERY_FOR_CITY_WEATHER_BY_LATL;

    public WeatherDataService(Context context) {
        this.context = context;
    }


    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(float cityLat1, float cityLon);
    }

    public void getCityLatL(String cityName, VolleyResponseListener volleyResponseListener) {

        String url = QUERY_FOR_CITY_LATL + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        cityLat = 0; cityLon = 0;

                        try {
                            JSONObject cityInfo = response.getJSONObject(0);
                            cityLat = Float.parseFloat(cityInfo.getString("lat"));
                            cityLon = Float.parseFloat(cityInfo.getString("lon"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        volleyResponseListener.onResponse(cityLat, cityLon);
                        //↑ actually this is doing the job (passing cityLat received from the Api to the MainActivity's onClick)
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something wrong happened :(", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }//getCityLatL



    public interface ForecastByLatLResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByLatL(float Latitude, float Longitude, ForecastByLatLResponse forecastByLatLResponse) {

        cityLat = Latitude;
        cityLon = Longitude;
        QUERY_FOR_CITY_WEATHER_BY_LATL =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon + "&daily=temperature_2m_max,temperature_2m_min,windspeed_10m_max&timezone=auto";

        Toast.makeText(context, "Lat " + String.valueOf(cityLat) + "\nLon: " + String.valueOf(cityLon), Toast.LENGTH_SHORT).show();

        List<WeatherReportModel> weatherReportModels = new ArrayList<>();

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_CITY_WEATHER_BY_LATL, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray time_from_api = response.getJSONObject("daily").getJSONArray("time");
                    JSONArray temperature_2m_max_from_api = response.getJSONObject("daily").getJSONArray("temperature_2m_max");
                    JSONArray temperature_2m_min_from_api = response.getJSONObject("daily").getJSONArray("temperature_2m_min");
                    JSONArray windspeed_10m_max_from_api = response.getJSONObject("daily").getJSONArray("windspeed_10m_max");

                    for (int i = 0; i < time_from_api.length(); i++) {

                        WeatherReportModel one_day_weather = new WeatherReportModel();

                        one_day_weather.setTime(time_from_api.getString(i));
                        one_day_weather.setTemperature_2m_max((float) temperature_2m_max_from_api.getDouble(i));
                        one_day_weather.setTemperature_2m_min((float) temperature_2m_min_from_api.getDouble(i));
                        one_day_weather.setWindspeed_10m_max((float) windspeed_10m_max_from_api.getDouble(i));

                        weatherReportModels.add(i, one_day_weather);
                    }


                    forecastByLatLResponse.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(weatherRequest);

    }//getCityForecastByLatL



    public interface GetCityForecastByNameCallback {
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback) {

        getCityLatL(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(float cityLat1, float cityLon) {

                getCityForecastByLatL(cityLat1, cityLon, new ForecastByLatLResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {

                        getCityForecastByNameCallback.onResponse(weatherReportModels);

                    }
                });//getCityForecastByLatL

            }
        });//getCityLatL



    }//getCityForecastByName



}