package com.thewhitewings.sky;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// Used in the Lower part of the bottom sheet
public class WeatherReportModelDetailed {

    private static final String TAG = "ModelDetailed";

    private float uv_index_max;
    private String uvDescription; // Relative to uv_index_max
    private int is_day; // 0: Night, 1: Day. From Current weather.
    private String time; // Current Time for Sun Time Animation
    private String sunrise;
    private String sunset;
    /*
    sunTimeProgress is a Calculated value as Progress Percentage Float for Sun Time Animation.
    Note: It should be set by calling setSunTimeProgress() manually after is_day, time, sunrise, and sunset is set.
     */
    private float sunTimeProgress;
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
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", sunTimeProgress=" + sunTimeProgress +
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

    /**
     * Set {@link #apparent_temperature} and call {@link #setApparent_temperatureDescription()} to set {@link #apparent_temperatureDescription}.
     * <p></p>
     * Important Note: it should be preceded by {@link #setTemperature_2m(float)} for {@link #setApparent_temperatureDescription()}.
     *
     * @param apparent_temperature apparent temperature float
     */
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

    public float getSunTimeProgress() {
        return sunTimeProgress;
    }

    /**
     * Sets the Sun Time Progress.
     * <p>
     * Duration Up: Duration When Sun is Up.
     * Duration Down: Duration When Sun is Down.
     * To get Duration Up, Get Duration from Sunrise to Sunset
     * To get Duration Down, Get Rest Duration by Subtracting Duration Up from a full day (24 Hours).
     * <p>
     * The following values are from the Animation of sun_time_scene.<p>
     * minProgress (0.17): Minimum value where the Moon is Fully Visible on Screen.
     * endDownProgress (0.23): The value where Sun is right Below Horizon at the Beginning of Sunrise.
     * startDownProgress (0.77): The value where Sun is right Below Horizon at the End of Sunset.
     * maxProgress (0.83): Maximum value where the Moon is Fully Visible on Screen.
     * fullProgressRange (0.66): The values where Moon (or Sun) is Fully Visible on Screen.
     * upProgressRange (0.54): The values where Sun is Up.
     * downProgressRange (0.12 = 0.06 * 2): The values where Sun is Down.
     * upAdding: The value that should be added to the value of progress at the Beginning of Sunrise.
     * downAdding: The value of that should be added to the value of progress at the Beginning of Sunset Or to minProgress.<p>
     * </p>
     * Notes:
     * 1. It should be called only after is_day, time, sunrise, and sunset is assigned.
     * 2. My getTime() that is defined in this class should not be confused with the one defined in java.util.Date.
     * 3. Setting TimeZone to UTC for timeFormat is fixing time in Milliseconds(Ms). It is adding TimezoneOffset to the time. It is needed for the Ms of the 24-hour full day.
     * 4. date.getTime() is getting time in Ms correctly and that's what I need, although the hours I get when logging the date object are not correct, and I don't need them.
     * 5. Api-parsed current time (time) is updated by Api every 15 minutes.
     * 6. I spread the Down Duration Evenly on the available Down Progress. In other words, the Maximum (or Minimum) progress value represents Half of Down Duration, and doesn't necessarily represent 24:00 (or 00:00).
     */
    public void setSunTimeProgress() {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date sunrise = timeFormat.parse(getSunrise());
            Date sunset = timeFormat.parse(getSunset());
            Date currentTime = timeFormat.parse(getTime());
            Date fullDay = timeFormat.parse("24:00 AM");

            long sunriseMs = sunrise.getTime();
            long sunsetMs = sunset.getTime();
            long currentTimeMs = currentTime.getTime();
            long fullDayMs = fullDay.getTime();

            long upDurationMs = sunsetMs - sunriseMs;
            long downDurationMs = fullDayMs - upDurationMs;

            float minProgress = 0.17f;
            float endDownProgress = 0.23f;
            float startDownProgress = 0.77f;
            float maxProgress = 0.83f;
            float fullProgressRange = maxProgress - minProgress; // 0.66
            float upProgressRange = startDownProgress - endDownProgress; // 0.54
            float downProgressRange = fullProgressRange - upProgressRange; // 0.12 (0.06 * 2)

            if (is_day == 1) { // Sun is Up
                long currentToSunriseDistanceMs = currentTimeMs - sunriseMs;
                float upAdding = (upProgressRange / upDurationMs) * currentToSunriseDistanceMs;

                sunTimeProgress = upAdding + endDownProgress;
            } else { // Sun is Down
                long currentToSunsetDistanceMs = currentTimeMs - sunsetMs;

                if (currentToSunsetDistanceMs < 0) { // Nearer to Sunrise
                    currentToSunsetDistanceMs = (fullDayMs - (currentToSunsetDistanceMs * -1)) - (downDurationMs / 2);
                    float downAdding = (downProgressRange / downDurationMs) * currentToSunsetDistanceMs;
                    sunTimeProgress = downAdding + minProgress;
                } else { // (currentToSunsetDistanceMs >= 0) // Nearer to Sunset
                    float downAdding = (downProgressRange / downDurationMs) * currentToSunsetDistanceMs;
                    sunTimeProgress = downAdding + startDownProgress;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "setSunTimeProgress: Catch " + e);
        }
    }

    /**
     * Logs and Sets the Sun Time Progress.
     * <p>
     * Duration Up: Duration When Sun is Up.
     * Duration Down: Duration When Sun is Down.
     * To get Duration Up, Get Duration from Sunrise to Sunset
     * To get Duration Down, Get Rest Duration by Subtracting Duration Up from a full day (24 Hours).
     * <p>
     * The following values are from the Animation of sun_time_scene.<p>
     * minProgress (0.17): Minimum value where the Moon is Fully Visible on Screen.
     * endDownProgress (0.23): The value where Sun is right Below Horizon at the Beginning of Sunrise.
     * startDownProgress (0.77): The value where Sun is right Below Horizon at the End of Sunset.
     * maxProgress (0.83): Maximum value where the Moon is Fully Visible on Screen.
     * fullProgressRange (0.66): The values where Moon (or Sun) is Fully Visible on Screen.
     * upProgressRange (0.54): The values where Sun is Up.
     * downProgressRange (0.12 = 0.06 * 2): The values where Sun is Down.
     * upAdding: The value that should be added to the value of progress at the Beginning of Sunrise.
     * downAdding: The value of that should be added to the value of progress at the Beginning of Sunset Or to minProgress.<p>
     * </p>
     * Notes:
     * 1. It should be called only after is_day, time, sunrise, and sunset is assigned.
     * 2. My getTime() that is defined in this class should not be confused with the one defined in java.util.Date.
     * 3. Setting TimeZone to UTC for timeFormat is fixing time in Milliseconds(Ms). It is adding TimezoneOffset to the time. It is needed for the Ms of the 24-hour full day.
     * 4. date.getTime() is getting time in Ms correctly and that's what I need, although the hours I get when logging the date object are not correct, and I don't need them.
     * 5. Api-parsed current time (time) is updated by Api every 15 minutes.
     * 6. I spread the Down Duration Evenly on the available Down Progress. In other words, the Maximum (or Minimum) progress value represents Half of Down Duration, and doesn't necessarily represent 24:00 (or 00:00).
     */
    public void setSunTimeProgressLog() {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date sunrise = timeFormat.parse("06:00 AM");
            Date sunset = timeFormat.parse("08:00 PM");
            Date currentTime = timeFormat.parse("07:00 AM");
//            Date currentTime = timeFormat.parse("09:00 PM");
//            Date currentTime = timeFormat.parse("05:00 AM");
            Date fullDay = timeFormat.parse("24:00 AM");

//            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
//            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//            Date sunrise = timeFormat.parse(getSunrise());
//            Date sunset = timeFormat.parse(getSunset());
//            Date currentTime = timeFormat.parse(getTime());
//            Date fullDay = timeFormat.parse("24:00 AM");

            Log.d(TAG, "getSunrise() " + getSunrise());
            Log.d(TAG, "getSunset() " + getSunset());
            Log.d(TAG, "getTime() " + getTime());

            long sunriseMs = sunrise.getTime();
            long sunsetMs = sunset.getTime();
            long currentTimeMs = currentTime.getTime();
            long fullDayMs = fullDay.getTime();

            Log.d(TAG, "sunriseMs " + sunriseMs);
            Log.d(TAG, "sunsetMs " + sunsetMs);
            Log.d(TAG, "currentTimeMs " + currentTimeMs);
            Log.d(TAG, "fullDayMs " + fullDayMs);

            long upDurationMs = sunsetMs - sunriseMs;
            long downDurationMs = fullDayMs - upDurationMs;
            Log.d(TAG, "upDurationMs " + upDurationMs);
            Log.d(TAG, "fullDayMs " + fullDayMs);
            Log.d(TAG, "downDurationMs " + downDurationMs);

            long sunriseHours = sunriseMs / (60 * 60 * 1000); // for log
            long sunriseMinutes = (sunriseMs % (60 * 60 * 1000)) / (60 * 1000); // for log
            String sunriseString = String.format("%02d:%02d", sunriseHours, sunriseMinutes); // for log. Needed for Zero padding
            Log.d(TAG, "sunriseString " + sunriseString);

            long sunsetHours = sunsetMs / (60 * 60 * 1000); // for log
            long sunsetMinutes = (sunsetMs % (60 * 60 * 1000)) / (60 * 1000); // for log
            String sunsetString = String.format("%02d:%02d", sunsetHours, sunsetMinutes); // for log.
            Log.d(TAG, "sunsetString " + sunsetString);

            long currentTimeHours = currentTimeMs / (60 * 60 * 1000); // for log
            long currentTimeMinutes = (currentTimeMs % (60 * 60 * 1000)) / (60 * 1000); // for log
            String currentTimeString = String.format("%02d:%02d", currentTimeHours, currentTimeMinutes); // for log.
            Log.d(TAG, "currentTimeString " + currentTimeString);

            long fullDayHours = fullDayMs / (60 * 60 * 1000); // for log
            long fullDayMinutes = (fullDayMs % (60 * 60 * 1000)) / (60 * 1000); // for log
            String fullDayString = String.format("%02d:%02d", fullDayHours, fullDayMinutes); // for log
            Log.d(TAG, "fullDayString " + fullDayString);

            long upDurationHours = upDurationMs / (60 * 60 * 1000); // for log
            long upDurationMinutes = (upDurationMs % (60 * 60 * 1000)) / (60 * 1000); // for log
            String upDurationString = String.format("%02d:%02d", upDurationHours, upDurationMinutes); // for log
            Log.d(TAG, "upDurationString " + upDurationString);

            long downDurationHours = downDurationMs / (60 * 60 * 1000); //for log
            long downDurationMinutes = (downDurationMs % (60 * 60 * 1000)) / (60 * 1000); //for log
            String downDurationString = String.format("%02d:%02d", downDurationHours, downDurationMinutes); //for log
            Log.d(TAG, "downDurationString " + downDurationString);

            float minProgress = 0.17f;
            float endDownProgress = 0.23f;
            float startDownProgress = 0.77f;
            float maxProgress = 0.83f;
            float fullProgressRange = maxProgress - minProgress; // 0.66
            float upProgressRange = startDownProgress - endDownProgress; // 0.54
            float downProgressRange = fullProgressRange - upProgressRange; // 0.12 (0.06 * 2)


            is_day = 0; //for log
            Log.d(TAG, "is_day " + is_day);
            if (is_day == 1) { // Sun is Up
                long currentToSunriseDistanceMs = currentTimeMs - sunriseMs;
                long hoursCurrentToSunriseDistance = currentToSunriseDistanceMs / (60 * 60 * 1000); //for log
                long minutesCurrentToSunriseDistance = (currentToSunriseDistanceMs % (60 * 60 * 1000)) / (60 * 1000); //for log
                String currentToSunriseDistanceString = String.format("%02d:%02d", hoursCurrentToSunriseDistance, minutesCurrentToSunriseDistance); //for log
                Log.d(TAG, "currentToSunriseDistanceMs " + currentToSunriseDistanceMs);
                Log.d(TAG, "currentToSunriseDistanceString " + currentToSunriseDistanceString);
                float upAdding = (upProgressRange / upDurationMs) * currentToSunriseDistanceMs;
                Log.d(TAG, "upAdding " + upAdding);

                sunTimeProgress = upAdding + endDownProgress;
            } else { // Sun is Down
                long currentToSunsetDistanceMs = currentTimeMs - sunsetMs;

                if (currentToSunsetDistanceMs < 0) { // Nearer to Sunrise
                    currentToSunsetDistanceMs = (fullDayMs - (currentToSunsetDistanceMs * -1)) - (downDurationMs / 2);
                    float downAdding = (downProgressRange / downDurationMs) * currentToSunsetDistanceMs;
                    Log.d(TAG, "currentToSunsetDistanceMs was < 0");
                    Log.d(TAG, "downAdding " + downAdding);
                    Log.d(TAG, "minProgress " + minProgress);
                    Log.d(TAG, "endDownProgress " + endDownProgress);
                    sunTimeProgress = downAdding + minProgress;
                } else { // (currentToSunsetDistanceMs >= 0) // Nearer to Sunset
                    float downAdding = (downProgressRange / downDurationMs) * currentToSunsetDistanceMs;
                    Log.d(TAG, "currentToSunsetDistanceMs already >= 0");
                    Log.d(TAG, "downAdding " + downAdding);
                    Log.d(TAG, "startDownProgress " + startDownProgress);
                    Log.d(TAG, "maxProgress " + maxProgress);
                    sunTimeProgress = downAdding + startDownProgress;
                }
                long hoursCurrentToSunsetDistance = currentToSunsetDistanceMs / (60 * 60 * 1000); //for log
                long minutesCurrentToSunsetDistance = (currentToSunsetDistanceMs % (60 * 60 * 1000)) / (60 * 1000); //for log
                String currentToSunsetDistanceString = String.format("%02d:%02d", hoursCurrentToSunsetDistance, minutesCurrentToSunsetDistance); //for log
                Log.d(TAG, "currentToSunsetDistanceMs " + currentToSunsetDistanceMs);
                Log.d(TAG, "currentToSunsetDistanceString " + currentToSunsetDistanceString);
            }
            Log.d(TAG, "sunTimeProgress " + sunTimeProgress);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "setSunTimeProgress: Catch " + e);
        }
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
     * Set the description of the apparent temperature {@link #apparent_temperatureDescription} by comparing {@link #apparent_temperature} to the current temperature {@link #temperature_2m}.
     * They are compared as Integers as they are shown to the user.
     * <p></p>
     * Note: It will be called automatically by {@link #setApparent_temperature}.
     */
    private void setApparent_temperatureDescription() {
        int apparent_temperatureInt = (int) apparent_temperature;
        int temperature_2mInt = (int) temperature_2m;
        if (apparent_temperatureInt < temperature_2mInt)
            apparent_temperatureDescription = "Cooler" + " than the actual temperature.";
        else if (apparent_temperatureInt > temperature_2mInt)
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
