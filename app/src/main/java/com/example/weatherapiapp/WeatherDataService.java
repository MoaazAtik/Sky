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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * Get city latitude and longitude, and assign them to cityLat and cityLon fields.
     * @param cityName Provided country, city, town name.
     * @param listenerGetCityLatL
     */
    public void getCityLatL(String cityName, ListenerGetCityLatL listenerGetCityLatL) {
        Log.d(TAG, "getCityLatL: ");
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
                            Log.d(TAG, "onResponse: getCityLatL " + city + " - " + country);
                            weatherReportModelShort.setCity(city);
                            weatherReportModelShort.setCountry(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: getCityLatL " + e);
                        }

                        listenerGetCityLatL.onResponse(weatherReportModelShort);
                        //↑ actually this is doing the job (passing cityLat received from the Api to the MainActivity's onClick)
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something wrong happened :(", Toast.LENGTH_SHORT).show();
                listenerGetCityLatL.onError(error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }//getCityLatL


    /**
     * Volley Response Listener Interface for Api Callback.
     * Used by getForecastByLatLShort, getForecastByLatLHourly, getForecastByLatLDaily, getForecastByLatLDetailed, and getForecastByName.
     *
     * @param <T> WeatherReportModelShort, WeatherReportModelHourly, WeatherReportModelDaily, or WeatherReportModelDetailed
     */
    public interface ListenerGetForecastByLatL<T> {
        void onError(String message);

        void onResponse(List<T> weatherReportModels);
    }

    /**
     * Get Short forecast for Home screen, and City Widgets.
     * Gets city Latitude and Longitude from the fields cityLat and cityLon, which were assigned when getForecastByName called getCityLatL before calling getForecastByLatLShort.
     *
     * @param weatherReportModelShort Comes with city and country along with cityLat and cityLon fields filled which were assigned when getForecastByName called getCityLatL before calling getForecastByLatLHourly.
     * @param listenerGetForecastByLatL
     */
    public void getForecastByLatLShort(WeatherReportModelShort weatherReportModelShort, ListenerGetForecastByLatL<WeatherReportModelShort> listenerGetForecastByLatL) {

        Log.d(TAG, "getForecastByLatLShort: ");
        QUERY_FOR_FORECAST_BY_LATL_SHORT =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&current=temperature_2m,is_day,weather_code" +
                        "&daily=temperature_2m_max,temperature_2m_min" +
                        "&timezone=auto" +
                        "&forecast_days=1";
        Toast.makeText(context, "Lat " + cityLat + "\nLon: " + cityLon, Toast.LENGTH_SHORT).show();

        List<WeatherReportModelShort> weatherReportModels = new ArrayList<>();

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_FORECAST_BY_LATL_SHORT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject current = response.getJSONObject("current");
                            JSONObject daily = response.getJSONObject("daily");

                            weatherReportModelShort.setTemperature_2m((float) current.getDouble("temperature_2m"));
                            weatherReportModelShort.setTemperature_2m_max(
                                    (float) daily.getJSONArray("temperature_2m_max").getDouble(0));
                            weatherReportModelShort.setTemperature_2m_min(
                                    (float) daily.getJSONArray("temperature_2m_min").getDouble(0));
                            weatherReportModelShort.setIs_day(current.getInt("is_day"));
                            weatherReportModelShort.setWeather_code(current.getInt("weather_code"));

                            // maybe i will use a single weatherReportModel instead of a list
//                            weatherReportModels.add(one_day_weather);
                            weatherReportModels.add(weatherReportModelShort);
                            listenerGetForecastByLatL.onResponse(weatherReportModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: getForecastByLatLShort " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                listenerGetForecastByLatL.onError(error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(weatherRequest);
    }//getForecastByLatLShort

    /**
     * Get Hourly forecast for Upper bottom sheet.
     * Gets city Latitude and Longitude from the fields cityLat and cityLon, which were assigned when getForecastByName called getCityLatL before calling getForecastByLatLHourly.
     *
     * @param listenerGetForecastByLatL
     */
    public void getForecastByLatLHourly(ListenerGetForecastByLatL<WeatherReportModelHourly> listenerGetForecastByLatL) {

        Log.d(TAG, "getForecastByLatLHourly: " + cityLat + " " + cityLon);
        List<WeatherReportModelHourly> weatherReportModels = new ArrayList<>();

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, getQueryUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject hourly = response.getJSONObject("hourly");
                            JSONArray time = hourly.getJSONArray("time");
                            JSONArray temperature_2m = hourly.getJSONArray("temperature_2m");
                            JSONArray precipitation_probability = hourly.getJSONArray("precipitation_probability");
                            JSONArray is_day = hourly.getJSONArray("is_day");
                            JSONArray weather_code = hourly.getJSONArray("weather_code");

                            String currentTime = getFormattedDateTime(0, null);
                            String parsedTime;

                            String currentDate = getFormattedDateTime(3, null);
                            String parsedDate;
                            String parsedDateTime;
                            int firstTimeIndexForHourModels = 0;

                            /*
                             When the current hour is 00:00, the Query will result in 1 past day and 1 day.
                             So, I have to check for the date too because firstTimeIndexForHourModels should be 24 and not 0.
                             I.e the 25th element which represents 00:00 of current day and not past day.
                             */
                            for (int x = 0; x < 25; x++) {
                                parsedDateTime = time.getString(x);
                                parsedDate = getFormattedDateTime(4, parsedDateTime);
                                parsedTime = getFormattedDateTime(1, parsedDateTime);
                                if (currentTime.equals(parsedTime) && currentDate.equals(parsedDate)) {
                                    firstTimeIndexForHourModels = x - 1;
                                    break;
                                }
                            }

                            for (int i = firstTimeIndexForHourModels; i < firstTimeIndexForHourModels + 8; i++) {
                                WeatherReportModelHourly weatherReportModelHourly = new WeatherReportModelHourly();
                                weatherReportModelHourly.setTime(getFormattedDateTime(2, time.getString(i)));
                                weatherReportModelHourly.setTemperature_2m((float) temperature_2m.getDouble(i));
                                weatherReportModelHourly.setPrecipitation_probability(precipitation_probability.getInt(i));
                                weatherReportModelHourly.setIs_day(is_day.getInt(i));
                                weatherReportModelHourly.setWeather_code(weather_code.getInt(i));
                                weatherReportModels.add(weatherReportModelHourly);
                            }
//                            Log.d(TAG, "onResponse: getForecastByLatLHourly Hourly weatherReportModels " + weatherReportModels);
                            listenerGetForecastByLatL.onResponse(weatherReportModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: getForecastByLatLHourly " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                listenerGetForecastByLatL.onError(error.toString() );
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(weatherRequest);
    }

    /**
     * Get Daily forecast for Upper bottom sheet.
     * Gets city Latitude and Longitude from the fields cityLat and cityLon, which were assigned when getForecastByName called getCityLatL before calling getForecastByLatLDaily.
     *
     * @param listenerGetForecastByLatL
     */
    public void getForecastByLatLDaily(ListenerGetForecastByLatL<WeatherReportModelDaily> listenerGetForecastByLatL) {

        Log.d(TAG, "getForecastByLatLDaily: " + cityLat + " " + cityLon);
        List<WeatherReportModelDaily> weatherReportModels = new ArrayList<>();

        QUERY_FOR_FORECAST_BY_LATL_DAILY =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&current=is_day" +
                        "&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max" +
                        "&timezone=auto" +
                        "&past_days=1";

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, QUERY_FOR_FORECAST_BY_LATL_DAILY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject current = response.getJSONObject("current");
                            JSONObject daily = response.getJSONObject("daily");
                            JSONArray time = daily.getJSONArray("time");
                            JSONArray temperature_2m_max = daily.getJSONArray("temperature_2m_max");
                            JSONArray temperature_2m_min = daily.getJSONArray("temperature_2m_min");
                            JSONArray precipitation_probability_max = daily.getJSONArray("precipitation_probability_max");
                            JSONArray weather_code = daily.getJSONArray("weather_code");

                            for (int i = 0; i < 7; i++) {
                                WeatherReportModelDaily weatherReportModelDaily = new WeatherReportModelDaily();
                                weatherReportModelDaily.setTime(getFormattedDateTime(5, time.getString(i)));
                                weatherReportModelDaily.setTemperature_2m((float) temperature_2m_max.getDouble(i), (float) temperature_2m_min.getDouble(i));
                                weatherReportModelDaily.setPrecipitation_probability_max(precipitation_probability_max.getInt(i));
                                weatherReportModelDaily.setIs_day(current.getInt("is_day"));
                                weatherReportModelDaily.setWeather_code(weather_code.getInt(i));
                                weatherReportModels.add(weatherReportModelDaily);
                            }
//                            Log.d(TAG, "onResponse: getForecastByLatLDaily Daily weatherReportModels " + weatherReportModels);
                            listenerGetForecastByLatL.onResponse(weatherReportModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: getForecastByLatLDaily " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                listenerGetForecastByLatL.onError(error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(weatherRequest);
    }

    /**
     * Get Detailed forecast for Lower bottom sheet.
     * Gets city Latitude and Longitude from the fields cityLat and cityLon, which were assigned when getForecastByName called getCityLatL before calling getForecastByLatLDetailed.
     *
     * @param listenerGetForecastByLatL
     */
    public void getForecastByLatLDetailed(ListenerGetForecastByLatL<WeatherReportModelDetailed> listenerGetForecastByLatL) {

        Log.d(TAG, "getForecastByLatLDetailed: ");
        List<WeatherReportModelDetailed> weatherReportModels = new ArrayList<>();

        QUERY_FOR_FORECAST_BY_LATL_DETAILED =
                "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                        "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,rain,wind_speed_10m,wind_direction_10m,dew_point_2m,visibility" +
                        "&daily=sunrise,sunset,uv_index_max,rain_sum" +
                        "&timezone=auto" +
                        "&forecast_days=1";

        JsonObjectRequest weatherRequest = new JsonObjectRequest(QUERY_FOR_FORECAST_BY_LATL_DETAILED, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject current = response.getJSONObject("current");
                    JSONObject daily = response.getJSONObject("daily");

                    WeatherReportModelDetailed weatherReportModelDetailed = new WeatherReportModelDetailed();
                    weatherReportModelDetailed.setUv_index_max((float) daily.getJSONArray("uv_index_max").getDouble(0));
                    weatherReportModelDetailed.setIs_day(current.getInt("is_day"));
                    weatherReportModelDetailed.setTime(current.getString("time"));
                    String parsedSunrise = daily.getJSONArray("sunrise").getString(0);
                    weatherReportModelDetailed.setSunrise(getFormattedDateTime(6, parsedSunrise));
                    String parsedSunset = daily.getJSONArray("sunset").getString(0);
                    weatherReportModelDetailed.setSunset(getFormattedDateTime(6, parsedSunset));
                    weatherReportModelDetailed.setSunTimeTitlePrimarySecondary();
                    weatherReportModelDetailed.setWind_speed_10m((float) current.getDouble("wind_speed_10m"));
                    weatherReportModelDetailed.setWind_direction_10m(current.getInt("wind_direction_10m"));
                    weatherReportModelDetailed.setRain((float) current.getDouble("rain"));
                    weatherReportModelDetailed.setRain_sum((float) daily.getJSONArray("rain_sum").getDouble(0));
                    weatherReportModelDetailed.setApparent_temperature((float) current.getDouble("apparent_temperature"));
                    weatherReportModelDetailed.setRelative_humidity_2m(current.getInt("relative_humidity_2m"));
                    weatherReportModelDetailed.setDew_point_2m((float) current.getDouble("dew_point_2m"));
                    weatherReportModelDetailed.setVisibility((float) current.getDouble("visibility"));
                    weatherReportModels.add(weatherReportModelDetailed);

                    listenerGetForecastByLatL.onResponse(weatherReportModels);

                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: getForecastByLatLDetailed " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listenerGetForecastByLatL.onError(error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(weatherRequest);
    }

    /**
     * getForecastByName(). Get forecast by city name.
     *
     * @param forecastType 0 = short (for city widgets, and main forecast),
     *                     1 = hourly (for upper bottom sheet),
     *                     2 = daily (for upper bottom sheet),
     *                     3 = detailed (for lower bottom sheet)
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
                        getForecastByLatLDaily(new ListenerGetForecastByLatL<WeatherReportModelDaily>() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<WeatherReportModelDaily> weatherReportModels) {
                                listenerGetForecastByLatL.onResponse(weatherReportModels);
                            }
                        });
                        break;
                    case 3:
                        getForecastByLatLDetailed(new ListenerGetForecastByLatL<WeatherReportModelDetailed>() {
                            @Override
                            public void onError(String message) {

                            }

                            @Override
                            public void onResponse(List<WeatherReportModelDetailed> weatherReportModels) {
                                listenerGetForecastByLatL.onResponse(weatherReportModels);
                            }
                        });
                        break;
                }// switch
            }// onResponse of ListenerGetCityLatL
        });//getCityLatL
    }//getForecastByName


    /**
     *
     * @param usage 0: Current time (HH = 00 to 23),
     *              1: Parsed time for comparing (e.g 21),
     *              2: Parsed time for Hour Model (e.g 9 PM),
     *              3: Current date for comparing (e.g 27),
     *              4: Parsed date for comparing (e.g 27),
     *              5: Parsed day of week for Day Model (e.g. WED),
     *              6: Parsed time for Detailed Model (e.g 5:08 AM).
     *
     * @param dateTime (Optional) Provide date and/or time to format.
     * @return Formatted date or time.
     */
    private String getFormattedDateTime(int usage, String dateTime) {

        SimpleDateFormat sdFormat;
        Date date = new Date();

        switch (usage) {
            case 0:
                sdFormat = new SimpleDateFormat("HH", Locale.getDefault());
                return sdFormat.format(date);
            case 1:
                return dateTime.substring(dateTime.indexOf('T') + 1, dateTime.indexOf('T') + 3);
            case 2:
                sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()); // Parsed Date Time Format "2023-11-14T00:00"
                try {
                    date = sdFormat.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getFormattedDateTime: case 2 " + e);
                    return null;
                }
                sdFormat = new SimpleDateFormat("h a", Locale.getDefault());
                return sdFormat.format(date).toUpperCase();
            case 3:
                sdFormat = new SimpleDateFormat("dd", Locale.getDefault());
                return sdFormat.format(date);
            case 4:
                return dateTime.substring(dateTime.indexOf('T') - 2, dateTime.indexOf('T'));
            case 5:
                sdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Parsed Date Time Format "2023-11-14"
                try {
                    date = sdFormat.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getFormattedDateTime: case 5 " + e);
                    return null;
                }
                sdFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                return sdFormat.format(date).toUpperCase();
            case 6:
                sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()); // Parsed Date Time Format "2023-11-14T00:00"
                try {
                    date = sdFormat.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(TAG, "getFormattedDateTime: case 6 " + e);
                    return null;
                }
                sdFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                return sdFormat.format(date).toUpperCase();
        }
        return null;
    }

    /**
     * Get the url of query for hourly forecast according to the current time. 3 days, or 1 day and 1 past day, or 1 day.
     *
     * @return QUERY_FOR_FORECAST_BY_LATL_HOURLY.
     */
    private String getQueryUrl() {

        int currentHour = Integer.parseInt(getFormattedDateTime(0, null));

        if (24 - currentHour < 7)
            QUERY_FOR_FORECAST_BY_LATL_HOURLY =
                    "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                            "&hourly=temperature_2m,precipitation_probability,is_day,weather_code" +
                            "&timezone=auto" +
                            "&forecast_days=" + "3";

        else if (currentHour == 0)
            QUERY_FOR_FORECAST_BY_LATL_HOURLY =
                    "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                            "&hourly=temperature_2m,precipitation_probability,is_day,weather_code" +
                            "&timezone=auto" +
                            "&past_days=" + "1" +
                            "&forecast_days=" + "1";

        else
            QUERY_FOR_FORECAST_BY_LATL_HOURLY =
                    "https://api.open-meteo.com/v1/forecast?latitude=" + cityLat + "&longitude=" + cityLon +
                            "&hourly=temperature_2m,precipitation_probability,is_day,weather_code" +
                            "&timezone=auto" +
                            "&forecast_days=" + "1";
        return QUERY_FOR_FORECAST_BY_LATL_HOURLY;
    }

}