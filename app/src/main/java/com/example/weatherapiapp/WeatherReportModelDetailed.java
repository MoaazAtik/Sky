package com.example.weatherapiapp;

import android.util.Log;

// Used in the Lower part of the bottom sheet
public class WeatherReportModelDetailed {

    private static final String TAG = "ModelDetailed";

    private float uv_index_max;
    private String uvDescription; // Relative to uv_index_max
    private int is_day; // 0: Night, 1: Day. From Current weather.
    private String time; // Current Time for Sun Time Animation
    private float timePercentage; // Current Time as Percentage Float for Sun Time Animation
    private String sunrise;
    private String sunset;
    private String sunTimeTitle; // Relative to is_day
    private String sunTimePrimary; // Relative to is_day
    private String sunTimeSecondary; // Relative to is_day
    private float wind_speed_10m;
    private int wind_direction_10m;
    private float windDirectionPercentage; // Wind Direction as Percentage Float for Wind Animation
    private float rain;
    private float rain_sum;
    private float temperature_2m;
    private float apparent_temperature; // Feels like
    private String apparent_temperatureDescription; // Relative to apparent_temperature
    private int relative_humidity_2m;
    private float dew_point_2m;
    private float visibility;
    private String visibilityDescription; // Relative to visibility

    public WeatherReportModelDetailed(float uv_index_max, int is_day, String time, String sunrise, String sunset, float wind_speed_10m, int wind_direction_10m, float rain, float rain_sum, float temperature_2m, float apparent_temperature, int relative_humidity_2m, float dew_point_2m, float visibility) {
        this.uv_index_max = uv_index_max;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.wind_speed_10m = wind_speed_10m;
        this.wind_direction_10m = wind_direction_10m;
        this.rain = rain;
        this.rain_sum = rain_sum;
        this.temperature_2m = temperature_2m;
        this.apparent_temperature = apparent_temperature;
        this.relative_humidity_2m = relative_humidity_2m;
        this.dew_point_2m = dew_point_2m;
        this.visibility = visibility;
    }

    public WeatherReportModelDetailed() {

    }

    @Override
    public String toString() {
        return "WeatherReportModelDetailed{" +
                "uv_index_max=" + uv_index_max +
                ", uvDescription=" + uvDescription +
                ", is_day=" + is_day +
                ", time=" + time +
                ", timePercentage=" + timePercentage +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", sunTimeTitle=" + sunTimeTitle +
                ", sunTimePrimary=" + sunTimePrimary +
                ", sunTimeSecondary=" + sunTimeSecondary +
                ", wind_speed_10m=" + wind_speed_10m +
                ", wind_direction_10m=" + wind_direction_10m +
                ", windDirectionPercentage=" + windDirectionPercentage +
                ", rain=" + rain +
                ", rain_sum=" + rain_sum +
                ", temperature_2m=" + temperature_2m +
                ", apparent_temperature=" + apparent_temperature +
                ", apparent_temperatureDescription=" + apparent_temperatureDescription +
                ", relative_humidity_2m=" + relative_humidity_2m +
                ", dew_point_2m=" + dew_point_2m +
                ", visibility=" + visibility +
                '}';
    }

    public float getUv_index_max() {
        return uv_index_max;
    }

    public void setUv_index_max(float uv_index_max) {
        this.uv_index_max = uv_index_max;
        setUv_Description(uv_index_max);
    }

    public String getUvDescription() {
        return uvDescription;
    }

    public int getIs_day() {
        return is_day;
    }

    public void setIs_day(int is_day) {
        this.is_day = is_day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        setTimePercentage();
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getSunTimeTitle() {
        return sunTimeTitle;
    }

    public String getSunTimePrimary() {
        return sunTimePrimary;
    }

    public String getSunTimeSecondary() {
        return sunTimeSecondary;
    }

    public float getWind_speed_10m() {
        return wind_speed_10m;
    }

    public void setWind_speed_10m(float wind_speed_10m) {
        this.wind_speed_10m = wind_speed_10m;
    }

    public int getWind_direction_10m() {
        return wind_direction_10m;
    }

    public void setWind_direction_10m(int wind_direction_10m) {
        this.wind_direction_10m = wind_direction_10m;
        setWindDirectionPercentage();
    }

    public float getWindDirectionPercentage() {
        return windDirectionPercentage;
    }

    public float getRain() {
        return rain;
    }

    public void setRain(float rain) {
        this.rain = rain;
    }

    public float getRain_sum() {
        return rain_sum;
    }

    public void setRain_sum(float rain_sum) {
        this.rain_sum = rain_sum;
    }

    public float getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(float temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public float getApparent_temperature() {
        return apparent_temperature;
    }

    public void setApparent_temperature(float apparent_temperature) {
        this.apparent_temperature = apparent_temperature;
        setApparent_temperatureDescription();
    }

    public String getApparent_temperatureDescription() {
        return apparent_temperatureDescription;
    }

    public int getRelative_humidity_2m() {
        return relative_humidity_2m;
    }

    public void setRelative_humidity_2m(int relative_humidity_2m) {
        this.relative_humidity_2m = relative_humidity_2m;
    }

    public float getDew_point_2m() {
        return dew_point_2m;
    }

    public void setDew_point_2m(float dew_point_2m) {
        this.dew_point_2m = dew_point_2m;
    }

    public float getVisibility() {
        return visibility;
    }

    /**
     * Assign the visibility after converting it from meters to km.
     *
     * @param visibility parsed float visibility in meters.
     */
    public void setVisibility(float visibility) {
        this.visibility = visibility / 1000;
        setVisibilityDescription(visibility);
    }

    public float getTimePercentage() {
        return timePercentage;
    }

    /**
     * Set Time as Percentage Float by converting Current 24-hour-based Time String to a Float (0.0 to 1.0).
     * It will be automatically called by setTime after assigning time.<p>
     * Note: it should be called after time is assigned.
     * <p></p>
     * 1. Convert the 24-based hours (00 to 23) to a 100-based number (0 to 100).
     * 2. Convert the 60-based minutes (00 to 59) to a 100-based number (0 to 100)
     * 3. Divide Converted Hours by 100, and Converted Minutes by 10000
     * 4. Add Divided Converted Minutes to Divided Converted Hours to achieve a number from 0.0 to 1.0.
     */
    public void setTimePercentage() {
        float timeHour = Integer.parseInt(time.substring(time.indexOf('T') + 1, time.indexOf(':')));
        float timeMinute = Integer.parseInt(time.substring(time.indexOf(':') + 1));

        timeHour = ((timeHour / 24) * 100) / 100;
        timeMinute = ((timeMinute / 60) * 100) / 10000;
        this.timePercentage = timeHour + timeMinute;
    }

    /**
     * Set Wind Direction as Percentage Float by converting 360-degree-based Wind Direction int to a Float (0.0 to 1.0).
     * It will be automatically called by setWind_direction_10m after assigning wind_direction_10m.<p>
     * Note: it should be called after wind_direction_10m is assigned.
     * <p></p>
     * 1. Convert the 360-based degrees (0 to 359) to a 100-based number (0 to 100).
     * 2. Divide Converted Degrees by 100 to achieve a number from 0.0 to 1.0.
     */
    public void setWindDirectionPercentage() {
        // Created windFloat to avoid integer division in floating-point context
        float windFloat = wind_direction_10m;
        this.windDirectionPercentage = ((windFloat / 360) * 100) / 100;
    }

    public String getVisibilityDescription() {
        return visibilityDescription;
    }

    /**
     * Set UV Description relative to UV Index Max value according to the WHO.
     * It will be automatically called by setUv_index_max after assigning uv_index_max.<p>
     * Note: it should be called after uv_index_max is assigned.
     *
     * @param uv_index_max Provided UV index value.
     */
    private void setUv_Description(float uv_index_max) {
        if (uv_index_max < 3)
            uvDescription = "Low"; // Green
        else if (uv_index_max < 6) // && 3 <= uv_index_max
            uvDescription = "Moderate"; // Yellow
        else if (uv_index_max < 8) // && 6 <= uv_index_max
            uvDescription = "High"; // Orange
        else if (uv_index_max < 11) // && 8 <= uv_index_max
            uvDescription = "Vary high"; // Red
        else if (11 <= uv_index_max)
            uvDescription = "Extreme"; // Purple
    }

    /**
     * Set the values for the Sun time widget relative to is_day. Sun time Title, Primary sun time and Secondary sun time.
     * Note: It should be called to assign sunTimePrimary and sunTimeSecondary after is_day is assigned.
     */
    public void setSunTimeTitlePrimarySecondary() {
        if (is_day == 0) {
            sunTimeTitle = "Sunrise";
            sunTimePrimary = sunrise;
            sunTimeSecondary = "Sunset • " + sunset;
        } else { // getIs_day == 1
            sunTimeTitle = "Sunset";
            sunTimePrimary = sunset;
            sunTimeSecondary = "Sunrise • " + sunrise;
        }
    }

    /**
     * Set the description of the apparent temperature by comparing it to the current temperature.
     * It will be automatically called by setApparent_temperature after assigning apparent_temperature.
     * Note: it should be called after temperature_2m and apparent_temperature is assigned.
     */
    public void setApparent_temperatureDescription() {
        if (apparent_temperature < temperature_2m)
            apparent_temperatureDescription = "Cooler" + " than the actual temperature.";
        else if (apparent_temperature > temperature_2m)
            apparent_temperatureDescription = "Warmer" + " than the actual temperature.";
        else // apparent_temperature == temperature_2m
            apparent_temperatureDescription = "Similar to the actual temperature.";
    }

    /**
     * Set the description of the visibility.
     * It will be automatically called by setVisibility after assigning visibility.
     * Note: it should be called after visibility is assigned.
     *
     * @param visibility Visibility Range in meters.
     */
    public void setVisibilityDescription(float visibility) {

        if (visibility < 50)
            visibilityDescription = "Dense fog";
        else if (visibility < 200) // && 50 <= visibility
            visibilityDescription = "Thick fog";
        else if (visibility < 500)
            visibilityDescription = "Moderate fog";
        else if (visibility < 1000)
            visibilityDescription = "Light fog";
        else if (visibility < 2000)
            visibilityDescription = "Thin fog";
        else if (visibility < 4000)
            visibilityDescription = "Haze";
        else if (visibility < 10000)
            visibilityDescription = "Light haze";
        else if (visibility < 20000)
            visibilityDescription = "Clear";
        else if (visibility < 50000)
            visibilityDescription = "Very clear";
        else if (visibility < 277000)
            visibilityDescription = "Exceptionally clear";
        else // if (277000 <= visibility)
            visibilityDescription = "Pure air";
    }
}
