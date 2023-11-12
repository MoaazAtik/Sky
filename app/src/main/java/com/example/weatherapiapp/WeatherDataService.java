package com.example.weatherapiapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDataService {

    private static final String TAG = "WeatherDataService";
    Context context;
    float cityLat, cityLon;

    public static final String QUERY_FOR_CITY_LATL = "https://nominatim.openstreetmap.org/search?format=json&q=";
    public String QUERY_FOR_FORECAST_BY_LATL_SHORT;
    public String QUERY_FOR_FORECAST_BY_LATL_HOURLY;
    public String QUERY_FOR_FORECAST_BY_LATL_DAILY;
    public String QUERY_FOR_FORECAST_BY_LATL_DETAILED;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    // Volley Response Listener. Callback for getCityLatL
    public interface ListenerGetCityLatL {
        void onError(String message);

//        void onResponse(float cityLat1, float cityLon);
        void onResponse(WeatherReportModelShort weatherReportModelShort);
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
                        WeatherReportModelShort weatherReportModelShort = new WeatherReportModelShort();
                        try {
                            JSONObject cityInfo = response.getJSONObject(0);
                            cityLat = Float.parseFloat(cityInfo.getString("lat"));
                            cityLon = Float.parseFloat(cityInfo.getString("lon"));
                            weatherReportModelShort.setLat(cityLat);
                            weatherReportModelShort.setLon(cityLon);
                            String displayName = cityInfo.getString("display_name");
                            String city = displayName.substring(0, displayName.indexOf(','));
                            String country = displayName.substring(displayName.lastIndexOf(',') + 2);
                            Log.d(TAG, "onResponse: " + city + " - " + country);
                            weatherReportModelShort.setCity(city);
                            weatherReportModelShort.setCountry(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        listenerGetCityLatL.onResponse(cityLat, cityLon);
                        listenerGetCityLatL.onResponse(weatherReportModelShort);
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


    // Volley Response Listener. Callback for getForecastByLatLShort, getForecastByLatLSimple, getForecastByLatLDetailed and getForecastByName
    public interface ListenerGetForecastByLatL<T> {
        void onError(String message);

        // weatherReportModels can be a list of WeatherReportModelShort, WeatherReportModelHourly, or WeatherReportModelDetailed
        void onResponse(List<T> weatherReportModels);
    }

    // getForecastByLatLShort(). Get short forecast for city widgets and main forecast.
//    public void getForecastByLatLShort(float Latitude, float Longitude, ListenerGetForecastByLatL<WeatherReportModelShort> listenerGetForecastByLatL) {
    public void getForecastByLatLShort(WeatherReportModelShort weatherReportModelShort, ListenerGetForecastByLatL<WeatherReportModelShort> listenerGetForecastByLatL) {

//        float cityLat = weatherReportModelShort.getLat();
//        float cityLon = weatherReportModelShort.getLon();
        QUERY_FOR_FORECAST_BY_LATL_SHORT =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&current=temperature_2m,weather_code" +
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
                            JSONObject daily = response.getJSONObject("daily");

//                            WeatherReportModelShort one_day_weather = new WeatherReportModelShort();
//                            one_day_weather.setTemperature_2m((float) current.getDouble("temperature_2m"));
//                            one_day_weather.setWeatherCode(current.getInt("weather_code"));
//                            one_day_weather.setCondition(current.getInt("weather_code"));
//                            one_day_weather.setTemperature_2m_max(
//                                    (float) response.getJSONObject("daily").getJSONArray("temperature_2m_max").getDouble(0));
//                            one_day_weather.setTemperature_2m_min(
//                                    (float) response.getJSONObject("daily").getJSONArray("temperature_2m_min").getDouble(0));
                            weatherReportModelShort.setTemperature_2m((float) current.getDouble("temperature_2m"));
                            weatherReportModelShort.setWeatherCode(current.getInt("weather_code"));
                            weatherReportModelShort.setCondition(current.getInt("weather_code"));
                            weatherReportModelShort.setTemperature_2m_max(
                                    (float) daily.getJSONArray("temperature_2m_max").getDouble(0));
                            weatherReportModelShort.setTemperature_2m_min(
                                    (float) daily.getJSONArray("temperature_2m_min").getDouble(0));

//                    weatherReportModels.add(i, one_day_weather);
                            // maybe i will use a single weatherReportModel instead of a list
//                            weatherReportModels.add(one_day_weather);
                            weatherReportModels.add(weatherReportModelShort);
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

    // getForecastByLatLHourly(). Get hourly forecast for Upper bottom sheet.
//    public void getForecastByLatLHourly(WeatherReportModelShort weatherReportModelShort, ListenerGetForecastByLatL<WeatherReportModelHourly> listenerGetForecastByLatL) {
    public void getForecastByLatLHourly(ListenerGetForecastByLatL<WeatherReportModelHourly> listenerGetForecastByLatL) {

        QUERY_FOR_FORECAST_BY_LATL_HOURLY =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&hourly=temperature_2m,precipitation_probability,weather_code" +
                        "&timezone=auto" +
                        "&forecast_days=1";
        Log.d(TAG, "getForecastByLatLHourly: " + cityLat + cityLon);
        List<WeatherReportModelHourly> weatherReportModels = new ArrayList<>();

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_FORECAST_BY_LATL_HOURLY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject hourly = response.getJSONObject("hourly");
                            JSONArray time = hourly.getJSONArray("time");
                            JSONArray temperature_2m = hourly.getJSONArray("temperature_2m");
                            JSONArray precipitation_probability = hourly.getJSONArray("precipitation_probability");
                            JSONArray weather_code = hourly.getJSONArray("weather_code");

//                            Date currentUnformattedTime = new Date();
//                            Log.d(TAG, "onResponse: currentUnformattedTime " + currentUnformattedTime);
//                            SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm aaa", Locale.getDefault());
//                            String currentTime = sdFormat.format(currentUnformattedTime);
//                            Log.d(TAG, "onResponse: currentTime " + currentTime);
//
//                            String currentTimeForComparing = currentTime.substring(0, 2);
//                            Log.d(TAG, "onResponse: currentTimeForComparing " + currentTimeForComparing);
                            String currentTime = getFormattedTime(0, null);

//                            String currentTimeForHourModel = currentTime.substring(0,2) + currentTime.substring(6);
//                            Log.d(TAG, "onResponse: currentTimeForModel " + currentTimeForHourModel);

                            int firstTimeIndexForHourModels = 0;

//                            String parsedTime;
                            String parsedTime;
//                            String parsedTime;

                            for (int x = 0; x < 24; x++) {
//                                parsedTime = time.getString(x);
//                                Log.d(TAG, "onResponse: parsed time full " + parsedTime);
//                                parsedTimeForComparing = parsedTime.substring(parsedTime.indexOf('T') + 1, parsedTime.indexOf('T') + 3); ///
//                                parsedTimeForComparing = getFormattedTime(1, time.getString(x)); ///
                                parsedTime = getFormattedTime(1, time.getString(x));
                                Log.d(TAG, "onResponse: parsedTimeForComparing " + parsedTime);
                                if (currentTime.equals(parsedTime)) {
                                    firstTimeIndexForHourModels = x - 1;
                                    // todo: add the case when the current hour is 00:00 so the first index will be -1 :/
                                    Log.d(TAG, "onResponse: fisttimeindex " + firstTimeIndexForHourModels);
                                    break;
                                }
                                Log.d(TAG, "onResponse: x " + x);
                            }

                            // todo: add the case when the remaining hours of the day are less than 8
                            for (int i = firstTimeIndexForHourModels; i < firstTimeIndexForHourModels + 4; i++) {
                                // TODO: manipulate the time string
                                WeatherReportModelHourly weatherReportModelHourly = new WeatherReportModelHourly();
//                                weatherReportModelHourly.setTime(time.getString(i));
                                weatherReportModelHourly.setTime(getFormattedTime(2, time.getString(i))); ///
                                weatherReportModelHourly.setTemperature_2m((float) temperature_2m.getDouble(i));
                                weatherReportModelHourly.setPrecipitation_probability(precipitation_probability.getInt(i));
                                weatherReportModelHourly.setWeather_code(weather_code.getInt(i));
                                weatherReportModelHourly.setCondition(weather_code.getInt(i));
                                weatherReportModels.add(weatherReportModelHourly);
                                Log.d(TAG, "onResponse: weatherReportModels " + weatherReportModels);
                            }
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
    }

    // getForecastByLatLDaily(). Get daily forecast for Upper bottom sheet.
    public void getForecastByLatLDaily(WeatherReportModelShort weatherReportModelShort, ListenerGetForecastByLatL<WeatherReportModelDaily> listenerGetForecastByLatL) {
        QUERY_FOR_FORECAST_BY_LATL_DAILY =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&hourly=temperature_2m,precipitation_probability,weather_code" +
                        "&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max" +
                        "&timezone=auto" +
                        "&past_days=1";
    }

    // getForecastByLatLDetailed(). Get detailed forecast for Lower bottom sheet.
    public void getForecastByLatLDetailed(WeatherReportModelShort weatherReportModelShort, ListenerGetForecastByLatL<WeatherReportModelDetailed> listenerGetForecastByLatL) {

//        float cityLat = weatherReportModelShort.getLat();
//        float cityLon = weatherReportModelShort.getLon();
//        QUERY_FOR_FORECAST_BY_LATL_DETAILED =
//                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
//                        "&current=relative_humidity_2m,apparent_temperature,wind_speed_10m,wind_direction_10m" + ",dewpoint_2m,visibility" +
//                        "&hourly=rain" +
//                        "&daily=sunrise,sunset,uv_index_max,rain_sum" +
//                        "&timezone=auto" +
//                        "&past_days=1";
    }

    /**
     * getForecastByName(). Get forecast by city name.
     *
     * @param forecastType 0 = short (for city widgets, and main forecast), 1 = hourly (for upper bottom sheet), 2 = daily (for upper bottom sheet), 3 = detailed (for lower bottom sheet)
     */
    public void getForecastByName(String cityName, int forecastType, ListenerGetForecastByLatL listenerGetForecastByLatL) {
        getCityLatL(cityName, new ListenerGetCityLatL() {
            @Override
            public void onError(String message) {
            }

            @Override
//            public void onResponse(float cityLat1, float cityLon) {
            public void onResponse(WeatherReportModelShort weatherReportModelShort) {
                switch (forecastType) {
                    case 0:
//                    getForecastByLatLShort(cityLat1, cityLon, new ListenerGetForecastByLatL<WeatherReportModelShort>() {
                        getForecastByLatLShort(weatherReportModelShort, new ListenerGetForecastByLatL<WeatherReportModelShort>() {
                            @Override
                            public void onError(String message) {
                            }

                            @Override
                            public void onResponse(List<WeatherReportModelShort> weatherReportModels) {
                                listenerGetForecastByLatL.onResponse(weatherReportModels);
                            }
                        });//getForecastByLatLShort
                        break;
                    case 1:
//                        getForecastByLatLHourly(weatherReportModelShort, new ListenerGetForecastByLatL<WeatherReportModelHourly>() {
                        getForecastByLatLHourly(new ListenerGetForecastByLatL<WeatherReportModelHourly>() {
                            @Override
                            public void onError(String message) {
                            }

                            @Override
                            public void onResponse(List<WeatherReportModelHourly> weatherReportModels) {
                                listenerGetForecastByLatL.onResponse(weatherReportModels);
                            }
                        });
                        break;
                    case 2:
//                        getForecastByLatLDaily(new ListenerGetForecastByLatL<WeatherReportModelDaily>() {
                        getForecastByLatLDaily(weatherReportModelShort, new ListenerGetForecastByLatL<WeatherReportModelDaily>() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<WeatherReportModelDaily> weatherReportModels) {

                            }
                        });
                        break;
                    case 3:
                        getForecastByLatLDetailed(weatherReportModelShort, new ListenerGetForecastByLatL<WeatherReportModelDetailed>() {
//                        getForecastByLatLDetailed(new ListenerGetForecastByLatL<WeatherReportModelDetailed>() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<WeatherReportModelDetailed> weatherReportModels) {

                            }
                        });
                        break;
                }// switch
            }// onResponse of ListenerGetCityLatL
        });//getCityLatL
    }//getForecastByName

    /**
     *
     * @param usage 0 = Current time, 1 = Parsed time for comparing, 2 = Parsed time for hour model.
     * @param time (Optional) Provide time to format.
     * @return Formatted time.
     */
    private String getFormattedTime(int usage, String time) {

        switch (usage) {
            case 0:
//                Date currentUnformattedTime = new Date();
//                Log.d(TAG, "onResponse: currentUnformattedTime " + currentUnformattedTime);
//                SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm aaa", Locale.getDefault());
                SimpleDateFormat sdFormat = new SimpleDateFormat("HH", Locale.getDefault());
//                String currentTime = sdFormat.format(currentUnformattedTime);
//                String currentTime = sdFormat.format(new Date());
//                Log.d(TAG, "getFormattedTime: currentTime " + currentTime);

//                String currentTimeForComparing = currentTime.substring(0, 2);
//                Log.d(TAG, "onResponse: currentTimeForComparing " + currentTimeForComparing);
//                return currentTimeForComparing;
//                return currentTime;
                return sdFormat.format(new Date());
//                break;
            case 1:
//                String parsedTimeForComparing = time.substring(time.indexOf('T') + 1, time.indexOf('T') + 3);
                return time.substring(time.indexOf('T') + 1, time.indexOf('T') + 3);
            case 2:

//                // Time received from the API
//                String apiTime = "2023-11-12T15:00";

                // Parse the API time string
                SimpleDateFormat parsedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
                Date date;
                try {
//                    date = parsedDateFormat.parse(apiTime);
                    date = parsedDateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }

                // Format the date into '3 PM' format
                SimpleDateFormat displayFormat = new SimpleDateFormat("h a", Locale.getDefault());
//                String formattedTime = displayFormat.format(date).toUpperCase();
//                Log.d(TAG, "getFormattedTime: formattedTime" + formattedTime);
//                parsedTimeForHourModel

//                return formattedTime;
                return displayFormat.format(date).toUpperCase();
        }
        return null;
    }

}