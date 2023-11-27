package com.example.weatherapiapp;

// Used in the Upper part of the bottom sheet for Hourly forecasts
public class WeatherReportModelHourly {

    private static final String TAG = "ModelHourly";

    private String time;
    private float temperature_2m;
    private int precipitation_probability;
    private int is_day; // 0: Night, 1: Day.
    private int weather_code; // WMO Weather Code

    private String conditionDescription;
    private int conditionImageId;

    public WeatherReportModelHourly(String time, float temperature_2m, int precipitation_probability, int is_day, int weather_code, String conditionDescription, int conditionImageId) {
        this.time = time;
        this.temperature_2m = temperature_2m;
        this.precipitation_probability = precipitation_probability;
        this.is_day = is_day;
        this.weather_code = weather_code;
        this.conditionDescription = conditionDescription;
    }

    public WeatherReportModelHourly() {

    }

    @Override
    public String toString() {
        return "WeatherReportModelHourly{" +
                "time='" + time + '\'' +
                ", temperature_2m=" + temperature_2m +
                ", precipitation_probability=" + precipitation_probability +
                ", is_day=" + is_day +
                ", weather_code=" + weather_code +
                ", conditionDescription='" + conditionDescription +
                ", conditionImageId='" + conditionImageId + '\'' +
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
     * Note: It should be called after is_day is assigned.
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
