package com.example.weatherapiapp;

import androidx.annotation.NonNull;

public class WeatherReportModelShort {

    private float lat;
    private float lon;
    private String city;
    private String country;
    private float temperature_2m;
    private int weatherCode;
    private float temperature_2m_max;
    private float temperature_2m_min;
    private String condition;

    public WeatherReportModelShort(float lat, float lon, String city, String country, float temperature_2m, int weatherCode, float temperature_2m_max, float temperature_2m_min, String condition) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
        this.country = country;
        this.temperature_2m = temperature_2m;
        this.weatherCode = weatherCode;
        this.temperature_2m_max = temperature_2m_max;
        this.temperature_2m_min = temperature_2m_min;
        this.condition = condition;
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
                ", Weather Condition Code: " + weatherCode +
                ", Max Temperature: " + temperature_2m_max +
                ", Min Temperature: " + temperature_2m_min +
                ", Condition: " + condition;
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

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(int weatherCode) {
        this.condition = convertWeatherCodeToDescription(weatherCode);
    }

    // Convert the given weather code to its corresponding weather condition description
    public String convertWeatherCodeToDescription(int weatherCode) {
        switch (weatherCode) {
            case 0:
                return "Clear Sky";
            case 1:
                return "Mainly Clear";
            case 2:
                return "Partly Cloudy";
            case 3:
                return "Overcast";
            case 45:
                return "Fog";
            case 48:
                return "Depositing Rime Fog";
            case 51:
                return "Light Drizzle";
            case 53:
                return "Moderate Drizzle";
            case 55:
                return "Dense Drizzle";
            case 56:
                return "Light Freezing Drizzle";
            case 57:
                return "Dense Freezing Drizzle";
            case 61:
                return "Slight Rain";
            case 63:
                return "Moderate Rain";
            case 65:
                return "Heavy Rain";
            case 66:
                return "Light Freezing Rain";
            case 67:
                return "Heavy Freezing Rain";
            case 71:
                return "Slight Snowfall";
            case 73:
                return "Moderate Snowfall";
            case 75:
                return "Heavy Snowfall";
            case 77:
                return "Snow Grains";
            case 80:
                return "Slight Rain Showers";
            case 81:
                return "Moderate Rain Showers";
            case 82:
                return "Violent Rain Showers";
            case 85:
                return "Slight Snow Showers";
            case 86:
                return "Heavy Snow Showers";
            case 95:
                return "Slight Thunderstorm";
            case 96:
                return "Thunderstorm • Slight Hail";
            case 99:
                return "Thunderstorm • Heavy Hail";
            default:
                return "";
        }
    }

}
