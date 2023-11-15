package com.example.weatherapiapp;

import androidx.annotation.NonNull;

public class WeatherReportModelShort {

    private float lat;
    private float lon;
    private String city;
    private String country;
    private float temperature_2m; // From Current weather.
    private float temperature_2m_max;
    private float temperature_2m_min;
    private int is_day; // 0: Night, 1: Day. From Current weather.
    private int weather_code; // WMO Weather Code

    private String conditionDescription;
    private int conditionImageId;

    public WeatherReportModelShort(float lat, float lon, String city, String country, float temperature_2m, float temperature_2m_max, float temperature_2m_min, int is_day, int weather_code, String conditionDescription) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
        this.country = country;
        this.temperature_2m = temperature_2m;
        this.temperature_2m_max = temperature_2m_max;
        this.temperature_2m_min = temperature_2m_min;
        this.is_day = is_day;
        this.weather_code = weather_code;
        this.conditionDescription = conditionDescription;
    }

    public WeatherReportModelShort() {
    }

    @NonNull
    @Override
    public String toString() {
//        return super.toString();
        return "Latitude: " + lat +
                ", Longitude: " + lon +
                ", City: " + city +
                ", Country: " + country +
                ", Temperature: " + temperature_2m +
                ", Max Temperature: " + temperature_2m_max +
                ", Min Temperature: " + temperature_2m_min +
                ", is_day=" + is_day +
                ", weather_code=" + weather_code +
                ", conditionDescription='" + conditionDescription + '\'' +
                ", conditionImageId=" + conditionImageId +
                '}';
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(float temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public float getTemperature_2m_max() {
        return temperature_2m_max;
    }

    public void setTemperature_2m_max(float temperature_2m_max) {
        this.temperature_2m_max = temperature_2m_max;
    }

    public float getTemperature_2m_min() {
        return temperature_2m_min;
    }

    public void setTemperature_2m_min(float temperature_2m_min) {
        this.temperature_2m_min = temperature_2m_min;
    }

    public int getIs_day() {
        return is_day;
    }

    public void setIs_day(int is_day) {
        this.is_day = is_day;
    }

    public int getWeather_code() {
        return weather_code;
    }

    /**
     * Set Weather code, and call to set Condition Description and Condition Image Id.
     *
     * @param weather_code Parsed WMO Weather Code.
     */
    public void setWeather_code(int weather_code) {
        this.weather_code = weather_code;
        setConditionDescriptionAndImageId(weather_code);
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public int getConditionImageId() {
        return conditionImageId;
    }

    /**
     * Set Condition Description and Condition Image Resource Id according to the given Weather Code
     *
     * @param weatherCode Parsed WMO Weather code.
     */
    public void setConditionDescriptionAndImageId(int weatherCode) {
        switch (weatherCode) {
            case 0:
                conditionDescription = "Clear Sky";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun;
                else conditionImageId = R.drawable.ic_condition_moon_stars;
                break;

            case 1:
                conditionDescription = "Mainly Clear";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun;
                else conditionImageId = R.drawable.ic_condition_moon;
                break;

            case 2:
                conditionDescription = "Partly Cloudy";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud;
                else conditionImageId = R.drawable.ic_condition_moon_cloud;
                break;

            case 3:
                conditionDescription = "Overcast";
                conditionImageId = R.drawable.ic_condition_cloud;
                break;

            case 45:
                conditionDescription = "Fog";
                conditionImageId = R.drawable.ic_condition_cloud;
                break;

            case 48:
                conditionDescription = "Depositing Rime Fog";
                conditionImageId = R.drawable.ic_condition_cloud;
                break;

            case 51:
                conditionDescription = "Light Drizzle";
                conditionImageId = R.drawable.ic_condition_big_rain_drops;
                break;

            case 53:
                conditionDescription = "Moderate Drizzle";
                conditionImageId = R.drawable.ic_condition_big_rain_drops;
                break;

            case 55:
                conditionDescription = "Dense Drizzle";
                conditionImageId = R.drawable.ic_condition_big_rain_drops;
                break;

            case 56:
                conditionDescription = "Light Freezing Drizzle";
                conditionImageId = R.drawable.ic_condition_big_rain_drops;
                break;

            case 57:
                conditionDescription = "Dense Freezing Drizzle";
                conditionImageId = R.drawable.ic_condition_big_rain_drops;
                break;

            case 61:
                conditionDescription = "Slight Rain";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_little_rain;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_little_rain;
                break;

            case 63:
                conditionDescription = "Moderate Rain";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_mid_rain;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_mid_rain;
                break;

            case 65:
                conditionDescription = "Heavy Rain";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_big_rain;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_big_rain;
                break;

            case 66:
                conditionDescription = "Light Freezing Rain";
                conditionImageId = R.drawable.ic_condition_cloud_little_rain;
                break;

            case 67:
                conditionDescription = "Heavy Freezing Rain";
                conditionImageId = R.drawable.ic_condition_cloud_big_rain;
                break;

            case 71:
                conditionDescription = "Slight Snowfall";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_little_snow;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_little_snow;
                break;

            case 73:
                conditionDescription = "Moderate Snowfall";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_mid_snow;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_mid_snow;
                break;

            case 75:
                conditionDescription = "Heavy Snowfall";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_snow;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_snow;
                break;

            case 77:
                conditionDescription = "Snow Grains";
                conditionImageId = R.drawable.ic_condition_big_snow_little_snow;
                break;

            case 80:
                conditionDescription = "Slight Rain Showers";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_angled_rain;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_angled_rain;
                break;

            case 81:
                conditionDescription = "Moderate Rain Showers";
                conditionImageId = R.drawable.ic_condition_cloud_angled_rain;
                break;

            case 82:
                conditionDescription = "Violent Rain Showers";
                conditionImageId = R.drawable.ic_condition_cloud_angled_rain_zap;
                break;

            case 85:
                conditionDescription = "Slight Snow Showers";
                conditionImageId = R.drawable.ic_condition_mid_snow_slow_winds;
                break;

            case 86:
                conditionDescription = "Heavy Snow Showers";
                conditionImageId = R.drawable.ic_condition_mid_snow_fast_winds;
                break;

            case 95:
                conditionDescription = "Slight Thunderstorm";
                conditionImageId = R.drawable.ic_condition_zaps;
                break;

            case 96:
                conditionDescription = "Thunderstorm • Slight Hail";
                if (is_day == 1) conditionImageId = R.drawable.ic_condition_sun_cloud_hailstone;
                else conditionImageId = R.drawable.ic_condition_moon_cloud_hailstone;
                break;

            case 99:
                conditionDescription = "Thunderstorm • Heavy Hail";
                conditionImageId = R.drawable.ic_condition_cloud_hailstone;
                break;

            default:
                conditionDescription = "";
                conditionImageId = R.drawable.ic_condition_cloud;
                break;
        }
    }

}
