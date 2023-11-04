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
    public String QUERY_FOR_FORECAST_BY_LATL_SHORT;
    public String QUERY_FOR_FORECAST_BY_LATL_LONG;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    // Volley Response Listener. Callback for getCityLatL
    public interface ListenerGetCityLatL {
        void onError(String message);

        void onResponse(float cityLat1, float cityLon);
    }

    // getCityLatL(). Get city latitude and longitude.
    public void getCityLatL(String cityName, ListenerGetCityLatL listenerGetCityLatL) {
        String url = QUERY_FOR_CITY_LATL + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        cityLat = 0;
                        cityLon = 0;
                        try {
                            JSONObject cityInfo = response.getJSONObject(0);
                            cityLat = Float.parseFloat(cityInfo.getString("lat"));
                            cityLon = Float.parseFloat(cityInfo.getString("lon"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listenerGetCityLatL.onResponse(cityLat, cityLon);
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


    // Volley Response Listener. Callback for getForecastByLatLShort, getForecastByLatLLong, and getForecastByName
    public interface ListenerGetForecastByLatL<T> {
        void onError(String message);

        // weatherReportModels can be a list of WeatherReportModelShort or WeatherReportModelLong
        void onResponse(List<T> weatherReportModels);
    }

    // getForecastByLatLShort(). Get short forecast.
    public void getForecastByLatLShort(float Latitude, float Longitude, ListenerGetForecastByLatL<WeatherReportModelShort> listenerGetForecastByLatL) {

        cityLat = Latitude;
        cityLon = Longitude;
        QUERY_FOR_FORECAST_BY_LATL_SHORT =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&current=temperature_2m,weathercode" +
                        "&daily=temperature_2m_max,temperature_2m_min" +
                        "&timezone=auto" +
                        "&forecast_days=1";
        Toast.makeText(context, "Lat " + String.valueOf(cityLat) + "\nLon: " + String.valueOf(cityLon), Toast.LENGTH_SHORT).show();

        List<WeatherReportModelShort> weatherReportModels = new ArrayList<>();

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_FORECAST_BY_LATL_SHORT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject current = response.getJSONObject("current");

                            WeatherReportModelShort one_day_weather = new WeatherReportModelShort();
                            one_day_weather.setTemperature_2m((float) current.getDouble("temperature_2m"));
                            one_day_weather.setWeathercode(current.getInt("weathercode"));
                            one_day_weather.setTemperature_2m_max(
                                    (float) response.getJSONObject("daily").getJSONArray("temperature_2m_max").getDouble(0));
                            one_day_weather.setTemperature_2m_min(
                                    (float) response.getJSONObject("daily").getJSONArray("temperature_2m_min").getDouble(0));

//                    weatherReportModels.add(i, one_day_weather);
                            // maybe i will use a single weatherReportModel instead of a list
                            weatherReportModels.add(one_day_weather);
                            listenerGetForecastByLatL.onResponse(weatherReportModels);
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
    }//getForecastByLatLShort

    // getForecastByLatLLong(). Get long forecast.
    public void getForecastByLatLLong(float Latitude, float Longitude, ListenerGetForecastByLatL<WeatherReportModelLong> listenerGetForecastByLatL) {

//        cityLat = Latitude;
//        cityLon = Longitude;
//        QUERY_FOR_FORECAST_BY_LATL_SHORT =
//                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
//                        "&current=temperature_2m,weathercode" +
//                        "&daily=temperature_2m_max,temperature_2m_min" +
//                        "&timezone=auto" +
//                        "&forecast_days=1";
//        Toast.makeText(context, "Lat " + String.valueOf(cityLat) + "\nLon: " + String.valueOf(cityLon), Toast.LENGTH_SHORT).show();
//
//        List<WeatherReportModelShort> weatherReportModels = new ArrayList<>();
//
//        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_FORECAST_BY_LATL_SHORT, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray time_from_api = response.getJSONObject("daily").getJSONArray("time");
//                            JSONArray temperature_2m_max_from_api = response.getJSONObject("daily").getJSONArray("temperature_2m_max");
//                            JSONArray temperature_2m_min_from_api = response.getJSONObject("daily").getJSONArray("temperature_2m_min");
//                            JSONArray windspeed_10m_max_from_api = response.getJSONObject("daily").getJSONArray("windspeed_10m_max");
//
//                            for (int i = 0; i < time_from_api.length(); i++) {
//                                WeatherReportModelShort one_day_weather = new WeatherReportModelShort();
//                                one_day_weather.setTime(time_from_api.getString(i));
//                                one_day_weather.setTemperature_2m_max((float) temperature_2m_max_from_api.getDouble(i));
//                                one_day_weather.setTemperature_2m_min((float) temperature_2m_min_from_api.getDouble(i));
//                                one_day_weather.setWindspeed_10m_max((float) windspeed_10m_max_from_api.getDouble(i));
//                                weatherReportModels.add(i, one_day_weather);
//                            }
//                            listenerGetForecastByLatL.onResponse(weatherReportModels);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        MySingleton.getInstance(context).addToRequestQueue(weatherRequest);
    }

    /**
     * getForecastByName(). Get forecast by city name.
     *
     * @param forecastSize 0 = short, 1 = long
     */
    public void getForecastByName(String cityName, int forecastSize, ListenerGetForecastByLatL listenerGetForecastByLatL) {
        getCityLatL(cityName, new ListenerGetCityLatL() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(float cityLat1, float cityLon) {
                if (forecastSize == 0)
                    getForecastByLatLShort(cityLat1, cityLon, new ListenerGetForecastByLatL<WeatherReportModelShort>() {
                        @Override
                        public void onError(String message) {
                        }

                        @Override
                        public void onResponse(List<WeatherReportModelShort> weatherReportModels) {
                            listenerGetForecastByLatL.onResponse(weatherReportModels);
                        }
                    });//getForecastByLatLShort

                else // forecastSize == 1
                    getForecastByLatLLong(cityLat1, cityLon, new ListenerGetForecastByLatL<WeatherReportModelLong>() {
                        @Override
                        public void onError(String message) {
                        }

                        @Override
                        public void onResponse(List<WeatherReportModelLong> weatherReportModels) {
                            listenerGetForecastByLatL.onResponse(weatherReportModels);
                        }
                    });//getForecastByLatLLong
            }// onResponse of ListenerGetCityLatL
        });//getCityLatL
    }//getForecastByName


}