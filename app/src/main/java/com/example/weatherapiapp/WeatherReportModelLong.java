package com.example.weatherapiapp;

public class WeatherReportModelLong {

    private String time;
    private float temperature_2m_max;
    private float temperature_2m_min;
    private float windspeed_10m_max;

    public WeatherReportModelLong(String time, float temperature_2m_max, float temperature_2m_min, float windspeed_10m_max) {
        this.time = time;
        this.temperature_2m_max = temperature_2m_max;
        this.temperature_2m_min = temperature_2m_min;
        this.windspeed_10m_max = windspeed_10m_max;
    }

    public WeatherReportModelLong() {

    }

    @Override
    public String toString() {
        return "Time: " + time +
                ", Max Temperature: " + temperature_2m_max +
                ", Min Temperature: " + temperature_2m_min +
                ", Max Windspeed: " + windspeed_10m_max;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public float getWindspeed_10m_max() {
        return windspeed_10m_max;
    }

    public void setWindspeed_10m_max(float windspeed_10m_max) {
        this.windspeed_10m_max = windspeed_10m_max;
    }
}
