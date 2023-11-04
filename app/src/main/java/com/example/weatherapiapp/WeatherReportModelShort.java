package com.example.weatherapiapp;

import androidx.annotation.NonNull;

public class WeatherReportModelShort {

    private float temperature_2m;
    private int weathercode;
    private float temperature_2m_max;
    private float temperature_2m_min;

    public WeatherReportModelShort(float temperature_2m, int weathercode, float temperature_2m_max, float temperature_2m_min) {
        this.temperature_2m = temperature_2m;
        this.weathercode = weathercode;
        this.temperature_2m_max = temperature_2m_max;
        this.temperature_2m_min = temperature_2m_min;
    }

    public WeatherReportModelShort() {
    }

    @NonNull
    @Override
    public String toString() {
//        return super.toString();
        return "Temperature: " + temperature_2m +
                ", Weather Condition Code: " + weathercode +
                ", Max Temperature: " + temperature_2m_max +
                ", Min Temperature: " + temperature_2m_min;
    }

    public float getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(float temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public int getWeathercode() {
        return weathercode;
    }

    public void setWeathercode(int weathercode) {
        this.weathercode = weathercode;
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

}
