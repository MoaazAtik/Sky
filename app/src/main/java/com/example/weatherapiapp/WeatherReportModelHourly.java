package com.example.weatherapiapp;

// Used in the Upper part of the bottom sheet for Hourly forecasts
public class WeatherReportModelHourly {

    private String time;
    private float temperature_2m;
    private int precipitation_probability;
    private int weather_code;
    private String condition;

    public WeatherReportModelHourly(String time, float temperature_2m, int precipitation_probability, int weather_code, String condition) {
        this.time = time;
        this.temperature_2m = temperature_2m;
        this.precipitation_probability = precipitation_probability;
        this.weather_code = weather_code;
        this.condition = condition;
    }

    public WeatherReportModelHourly() {

    }

    @Override
    public String toString() {
        return "WeatherReportModelHourly{" +
                "time='" + time + '\'' +
                ", temperature_2m=" + temperature_2m +
                ", precipitation_probability=" + precipitation_probability +
                ", weather_code=" + weather_code +
                ", condition='" + condition + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(float temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public int getPrecipitation_probability() {
        return precipitation_probability;
    }

    public void setPrecipitation_probability(int precipitation_probability) {
        this.precipitation_probability = precipitation_probability;
    }

    public int getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(int weather_code) {
        this.weather_code = weather_code;
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
