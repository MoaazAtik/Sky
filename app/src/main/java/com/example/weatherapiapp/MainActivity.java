package com.example.weatherapiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.DialogCompat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherapiapp.databinding.ActivityMainBinding;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

//#freeCodeCamp.org (YT) | REST API - Network Data
public class MainActivity extends AppCompatActivity {

    /*
     Get city latitude and longitude from nominatim.org, then get the city's weather from open-meteo.com
     I used city latitude and longitude instead of city id.
     */

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private MotionLayout mainMotionLayout, sunTimeMotionLayout, windMotionLayout;
    private AppCompatTextView txtMainCity, txtMainTemp, txtMainHTemp, txtMainLTemp, txtMainConditionDescription;
    private AppCompatTextView hour0Time, hour1Time, hour2Time, hour3Time, hour4Time, hour5Time, hour6Time, hour7Time;
    private AppCompatImageView hour0Condition, hour1Condition, hour2Condition, hour3Condition, hour4Condition, hour5Condition, hour6Condition, hour7Condition;
    private AppCompatTextView hour0Precipitation, hour1Precipitation, hour2Precipitation, hour3Precipitation, hour4Precipitation, hour5Precipitation, hour6Precipitation, hour7Precipitation;
    private AppCompatTextView hour0Temp, hour1Temp, hour2Temp, hour3Temp, hour4Temp, hour5Temp, hour6Temp, hour7Temp;

    private AppCompatTextView day0Time, day1Time, day2Time, day3Time, day4Time, day5Time, day6Time;
    private AppCompatImageView day0Condition, day1Condition, day2Condition, day3Condition, day4Condition, day5Condition, day6Condition;
    private AppCompatTextView day0Precipitation, day1Precipitation, day2Precipitation, day3Precipitation, day4Precipitation, day5Precipitation, day6Precipitation;
    private AppCompatTextView day0Temp, day1Temp, day2Temp, day3Temp, day4Temp, day5Temp, day6Temp;

    private AppCompatTextView txtUvValue, txtUvStatus, txtWidgetSunTimeTitle, txtSunTimePrimary, txtSunTimeSecondary, txtWindSpeed, txtRainPrimary, txtRainSecondary, txtFeelsLikeTemp, txtFeelsLikeDescription, txtHumidityValue, txtHumidityDewPoint, txtVisibility, txtVisibilityDescription;
    private SeekBar seekBarUvIndex;

    boolean firstFullSunTimeAnimation = true;
    boolean firstFullWindAnimation = true;
    private boolean firstWeatherParsing = true;
    float sunTimeProgress;
    private String sunTimePrimary;
    float windDirectionProgress;
    private String homeCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
        setContentView(R.layout.main);

        Log.d(TAG, "onCreate: ");

        mainMotionLayout = findViewById(R.id.main_motion_layout);
        sunTimeMotionLayout = findViewById(R.id.sun_time);
        windMotionLayout = findViewById(R.id.wind);

        AppCompatButton btnHourlyForecast = findViewById(R.id.btn_hourly_forecast);
        AppCompatButton btnDailyForecast = findViewById(R.id.btn_daily_forecast);
        HorizontalScrollView horizontalScrollViewHourly = findViewById(R.id.hourly_forecast_sv);
        HorizontalScrollView horizontalScrollViewDaily = findViewById(R.id.daily_forecast_sv);
        AppCompatImageView underline = findViewById(R.id.underline);
        ConstraintLayout tabBar = findViewById(R.id.tab_bar);
        AppCompatButton btnHomePlus = findViewById(R.id.btn_home_plus);
        AppCompatButton btnHomeExpand = findViewById(R.id.btn_home_expand);
        AppCompatButton btnHomeCitiesList = findViewById(R.id.btn_home_cities_list);

        txtMainCity = findViewById(R.id.txt_home_city);
        txtMainTemp = findViewById(R.id.txt_home_temp);
        txtMainHTemp = findViewById(R.id.txt_home_h_temp);
        txtMainLTemp = findViewById(R.id.txt_home_l_temp);
        txtMainConditionDescription = findViewById(R.id.txt_home_condition_description);

        hour0Time = findViewById(R.id.hour_0_time);
        hour1Time = findViewById(R.id.hour_1_time);
        hour2Time = findViewById(R.id.hour_2_time);
        hour3Time = findViewById(R.id.hour_3_time);
        hour4Time = findViewById(R.id.hour_4_time);
        hour5Time = findViewById(R.id.hour_5_time);
        hour6Time = findViewById(R.id.hour_6_time);
        hour7Time = findViewById(R.id.hour_7_time);

        hour0Precipitation = findViewById(R.id.hour_0_precipitation);
        hour1Precipitation = findViewById(R.id.hour_1_precipitation);
        hour2Precipitation = findViewById(R.id.hour_2_precipitation);
        hour3Precipitation = findViewById(R.id.hour_3_precipitation);
        hour4Precipitation = findViewById(R.id.hour_4_precipitation);
        hour5Precipitation = findViewById(R.id.hour_5_precipitation);
        hour6Precipitation = findViewById(R.id.hour_6_precipitation);
        hour7Precipitation = findViewById(R.id.hour_7_precipitation);

        hour0Condition = findViewById(R.id.hour_0_condition);
        hour1Condition = findViewById(R.id.hour_1_condition);
        hour2Condition = findViewById(R.id.hour_2_condition);
        hour3Condition = findViewById(R.id.hour_3_condition);
        hour4Condition = findViewById(R.id.hour_4_condition);
        hour5Condition = findViewById(R.id.hour_5_condition);
        hour6Condition = findViewById(R.id.hour_6_condition);
        hour7Condition = findViewById(R.id.hour_7_condition);

        hour0Temp = findViewById(R.id.hour_0_temp);
        hour1Temp = findViewById(R.id.hour_1_temp);
        hour2Temp = findViewById(R.id.hour_2_temp);
        hour3Temp = findViewById(R.id.hour_3_temp);
        hour4Temp = findViewById(R.id.hour_4_temp);
        hour5Temp = findViewById(R.id.hour_5_temp);
        hour6Temp = findViewById(R.id.hour_6_temp);
        hour7Temp = findViewById(R.id.hour_7_temp);


        day0Time = findViewById(R.id.day_0_time);
        day1Time = findViewById(R.id.day_1_time);
        day2Time = findViewById(R.id.day_2_time);
        day3Time = findViewById(R.id.day_3_time);
        day4Time = findViewById(R.id.day_4_time);
        day5Time = findViewById(R.id.day_5_time);
        day6Time = findViewById(R.id.day_6_time);

        day0Precipitation = findViewById(R.id.day_0_precipitation);
        day1Precipitation = findViewById(R.id.day_1_precipitation);
        day2Precipitation = findViewById(R.id.day_2_precipitation);
        day3Precipitation = findViewById(R.id.day_3_precipitation);
        day4Precipitation = findViewById(R.id.day_4_precipitation);
        day5Precipitation = findViewById(R.id.day_5_precipitation);
        day6Precipitation = findViewById(R.id.day_6_precipitation);

        day0Condition = findViewById(R.id.day_0_condition);
        day1Condition = findViewById(R.id.day_1_condition);
        day2Condition = findViewById(R.id.day_2_condition);
        day3Condition = findViewById(R.id.day_3_condition);
        day4Condition = findViewById(R.id.day_4_condition);
        day5Condition = findViewById(R.id.day_5_condition);
        day6Condition = findViewById(R.id.day_6_condition);

        day0Temp = findViewById(R.id.day_0_temp);
        day1Temp = findViewById(R.id.day_1_temp);
        day2Temp = findViewById(R.id.day_2_temp);
        day3Temp = findViewById(R.id.day_3_temp);
        day4Temp = findViewById(R.id.day_4_temp);
        day5Temp = findViewById(R.id.day_5_temp);
        day6Temp = findViewById(R.id.day_6_temp);


        // Initialize Lower bottom sheet fields (except Motion Layouts)
        txtUvValue = findViewById(R.id.txt_uv_value);
        txtUvStatus = findViewById(R.id.txt_uv_status);
        txtWidgetSunTimeTitle = findViewById(R.id.widget_sun_time_title);
        txtSunTimePrimary = findViewById(R.id.txt_sun_time_primary);
        txtSunTimeSecondary = findViewById(R.id.txt_sun_time_secondary);
        txtWindSpeed = findViewById(R.id.txt_wind_speed);
        txtRainPrimary = findViewById(R.id.txt_rain_primary);
        txtRainSecondary = findViewById(R.id.txt_rain_secondary);
        txtFeelsLikeTemp = findViewById(R.id.txt_feels_like_temp);
        txtFeelsLikeDescription = findViewById(R.id.txt_feels_like_description);
        txtHumidityValue = findViewById(R.id.txt_humidity_value);
        txtHumidityDewPoint = findViewById(R.id.txt_humidity_dew_point);
        txtVisibility = findViewById(R.id.txt_visibility);
        txtVisibilityDescription = findViewById(R.id.txt_visibility_description);
        seekBarUvIndex = findViewById(R.id.seekbar_uv_index);


        showHomeCityDialog();

        // Get current Home City Preference
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        homeCity = preferences.getString("homeCity", "rio");

        // btnHourlyForecast OnClickListener
        btnHourlyForecast.setOnClickListener(v -> {
            int currentScroll = horizontalScrollViewDaily.getScrollX();
            // to avoid unexplained delay of moving hourly and daily views
            int durationScrollAnimation = (int) (currentScroll * 1.1);
            ObjectAnimator animator = ObjectAnimator.ofInt(horizontalScrollViewDaily, "scrollX", 0);
            animator.setDuration(durationScrollAnimation)
                    .setInterpolator(new AccelerateInterpolator());
            animator.start();

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    int durationTranslation = 700;
                    horizontalScrollViewDaily.animate()
                            .translationX(0).alpha(0)
                            .setDuration(durationTranslation).setInterpolator(new LinearInterpolator());
                    horizontalScrollViewHourly.animate()
                            .translationX(0).alpha(1)
                            .setDuration(durationTranslation).setInterpolator(new LinearInterpolator())
                            /*
                            horizontalScrollViewDaily was not visible after multiple transitions and scrolling hourly forecast
                            until scrolling it by hand (and not with .scrollTo()).
                            Setting its visibility Gone in here, and resetting it after a btnDaily click fixed this issue.
                            */
                            .withEndAction(() -> horizontalScrollViewDaily.setVisibility(View.GONE));
                    underline.animate().translationX(0)
                            .setDuration(durationTranslation);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                }
            });
        });

        // btnDailyForecast OnClickListener
        btnDailyForecast.setOnClickListener(v -> {
            int widthHourlyLL = horizontalScrollViewHourly.getChildAt(0).getWidth();
            int widthHourlySV = horizontalScrollViewHourly.getWidth();
            int maxScroll = widthHourlyLL - widthHourlySV;
            int actualMaxScroll = maxScroll + horizontalScrollViewHourly.getPaddingStart() + horizontalScrollViewHourly.getPaddingEnd();
            // to avoid unexplained delay of moving hourly and daily views
            int currentScroll = horizontalScrollViewHourly.getScrollX();
            int durationScrollAnimation = (int) ((actualMaxScroll - currentScroll) * 1.1);

            ObjectAnimator animator = ObjectAnimator.ofInt(horizontalScrollViewHourly, "scrollX", actualMaxScroll);
            animator.setDuration(durationScrollAnimation)
                    .setInterpolator(new AccelerateInterpolator());
            animator.start();

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    /*
                    horizontalScrollViewDaily was not visible after the transition until scrolling it by hand (and not with .scrollTo()).
                    Setting its visibility at runtime here, and making its visibility gone in xml fixed this issue.
                    */
                    horizontalScrollViewDaily.setVisibility(View.VISIBLE);
                    float widthScreen = getResources().getDisplayMetrics().widthPixels;
                    int durationTranslation = 700;
                    horizontalScrollViewHourly.animate().
                            translationX(-widthScreen).alpha(0)
                            .setDuration(durationTranslation).setInterpolator(new LinearInterpolator());
                    horizontalScrollViewDaily.animate()
                            .translationX(-widthScreen).alpha(1)
                            .setDuration(durationTranslation).setInterpolator(new LinearInterpolator());
                    underline.animate().translationX((widthScreen / 2))
                            .setDuration(durationTranslation);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {
                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                }
            });
        });

        /*
        I added an OnTouchListener and returned 'true' to allow the tab bar to consume the touch,
        preventing the bottom sheet from opening when swiping the tab bar.
         */
        tabBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // MotionLayout TransitionListener
        mainMotionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
            }

            int lastStateOfMainMotionLayout = R.id.start;

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.end && currentId != lastStateOfMainMotionLayout) {
                    // Initial animation of sun time and wind indicator
                    lastStateOfMainMotionLayout = currentId;
                    firstFullSunTimeAnimation = true;
                    firstFullWindAnimation = true;
                    sunTimeMotionLayout.transitionToEnd();
                    windMotionLayout.transitionToEnd();
                } else if (currentId == R.id.start && currentId != lastStateOfMainMotionLayout) {
                    lastStateOfMainMotionLayout = currentId;
                    sunTimeMotionLayout.jumpToState(R.id.start);
                    windMotionLayout.jumpToState(R.id.start);
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
            }
        });

        // btnHomePlus OnClickListener
        btnHomePlus.setOnClickListener(v -> {
            Log.d(TAG, "onClick btnHomePlus");
        });

        // btnHomeExpand OnClickListener
        btnHomeExpand.setOnClickListener(v -> {
            Log.d(TAG, "onClick btnHomeExpand");
        });

        // btnHomeCitiesList OnClickListener
        btnHomeCitiesList.setOnClickListener(v -> {
            Log.d(TAG, "onClick btnHomeCitiesList");
        });


        //Old buttons of activity_main (getWeather...)
//        binding.btnGetCityLatL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                weatherDataService.getCityLatL(binding.etDataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
//                    @Override
//                    public void onError(String message) {
//
//                    }
//
//                    @Override
//                    public void onResponse(float cityLat2, float cityLon) {
//                        //↑ this doesn't do anything if it's not called from Api response
//                        /*
//                        public void getCityLatL(String cityName, VolleyResponseListener volleyResponseListener) {...
//
//                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//
//                                @Override
//                                public void onResponse(JSONArray response) {
//
//                           ***         volleyResponseListener.onResponse(cityLat, cityLon);
//                         */
//
//                        Toast.makeText(MainActivity.this, "Lat: " + cityLat2 + "\nLon: " + cityLon, Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//            }//onClick
//        });//setOnClickListener btn_getCityLatL
//
//
//        binding.btnGetWeatherByLatL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                float cityLat = 0, cityLon = 0;
//
//                try {
//                    cityLat = Float.parseFloat(binding.etDataInput.getText().toString().substring(0, binding.etDataInput.getText().toString().indexOf(",")).trim());
//                    cityLon = Float.parseFloat(binding.etDataInput.getText().toString().substring(binding.etDataInput.getText().toString().indexOf(",") + 1).trim());
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                }
//
//
//                weatherDataService.getCityForecastByLatL(cityLat, cityLon, new WeatherDataService.ForecastByLatLResponse() {
//                    @Override
//                    public void onError(String message) {
//
//                    }
//
//                    @Override
//                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                        binding.lvWeatherReports.setAdapter(arrayAdapter);
//                    }
//                });
//
//            }//onClick
//        });//setOnClickListener  btn_getWeatherByLatL
//
//
//        binding.btnGetWeatherByCityName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                weatherDataService.getCityForecastByName(binding.etDataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
//                    @Override
//                    public void onError(String message) {
//
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
//
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                        binding.lvWeatherReports.setAdapter(arrayAdapter);
//
//                    }
//                });
//
//            }
//        });//setOnClickListener btn_getWeatherByName

    }//onCreate

    @Override
    protected void onResume() {
        super.onResume();

        // Check if Home city Preference is changed after returning from Cities Activity
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String newHomeCity = preferences.getString("homeCity", "rio");

        if (firstWeatherParsing || !newHomeCity.equals(homeCity)) {
            homeCity = newHomeCity;
            getWeather(homeCity);
            firstWeatherParsing = false;
        }
    }

    //showHomeCityDialog()
    private void showHomeCityDialog() {

        // Check if the dialog should be shown based on the preference
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean shouldNotShowDialog = preferences.getBoolean("dontShowHomeCityDialog", false);

        if (shouldNotShowDialog) {
            Log.d(TAG, "shouldNotShowDialog " + shouldNotShowDialog);
            return;
        }


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        // Inflate a custom layout for the dialog content
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_home_city, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Find the checkbox in the custom layout
        CheckBox dontShowAgainCheckbox = dialogView.findViewById(R.id.checkbox_dont_show_again);

        // Handle the positive button (btn_fix) in the custom layout
        Button btnFix = (Button) dialogView.findViewById(R.id.btn_fix);
        btnFix.setOnClickListener(v -> {
            Intent citiesIntent = new Intent(MainActivity.this, CitiesActivity.class);
            MainActivity.this.startActivity(citiesIntent);
        });

        // Handle the negative button (btn_cancel) in the custom layout
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            // Check if the dialog should show again
            if (dontShowAgainCheckbox.isChecked()) {
                // Save a preference "dontShowHomeCityDialog" to not show the dialog again
                SharedPreferences preferences1 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                preferences1.edit().putBoolean("dontShowHomeCityDialog", true).apply();
            }

//            dialog.dismiss();
        });

//        // set OnDismissListener for the dialog
//        // onDismiss will be called when btnCancel, device's back button, or outside the dialog box is clicked.
//        // Eventually showMoreInformationDialog() is called in all situations even if btnFix is clicked.
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                showMoreInformationDialog();
//            }
//        });
    }

    /**
     * Parse all Weather types for Home screen
     * @param homeCity The Home city which its weather would be shown in all Home screen
     */
    private void getWeather(String homeCity) {
        getForecastShort(homeCity);
        getForecastHourly(homeCity);
        getForecastDaily(homeCity);
        getForecastDetailed(homeCity);
    }

    /**
     * Get Short forecast for the main weather details in the middle of the home screen.
     */
    private void getForecastShort(String homeCity) {
        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
        weatherDataService.getForecastByName(homeCity,
                0, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelShort>() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onError: getForecastShort " + message);
                    }

                    @Override
                    public void onResponse(List<WeatherReportModelShort> weatherReportModels) {

                        Toast.makeText(MainActivity.this, "O  K", Toast.LENGTH_SHORT).show();
                        WeatherReportModelShort weatherReportModelShort = weatherReportModels.get(0);

                        String temp = (int) weatherReportModelShort.getTemperature_2m() + "°";
                        txtMainTemp.setText(temp);
                        String hTemp = "H:" + (int) weatherReportModelShort.getTemperature_2m_max() + "°";
                        txtMainHTemp.setText(hTemp);
                        String lTemp = "L:" + (int) weatherReportModelShort.getTemperature_2m_min() + "°";
                        txtMainLTemp.setText(lTemp);
                        String cityCountry = weatherReportModelShort.getCity() + ", " + weatherReportModelShort.getCountry();
                        txtMainCity.setText(cityCountry);
                        String conditionDescription = weatherReportModelShort.getConditionDescription();
                        txtMainConditionDescription.setText(conditionDescription);
                    }
                });
    } // getForecastShort

    /**
     * Get Hourly forecast for the upper bottom sheet.
     */
    private void getForecastHourly(String homeCity) {
        WeatherDataService weatherDataService = new WeatherDataService(this);
        weatherDataService.getForecastByName(homeCity, 1, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelHourly>() {
            @Override
            public void onError(String message) {
                Log.d(TAG, "onError: getForecastHourly " + message);
            }

            @Override
            public void onResponse(List<WeatherReportModelHourly> weatherReportModels) {
                Log.d(TAG, "onResponse: " + "getForecastHourly");
                getAndAssignHourlyOrDailyValues(0, weatherReportModels);
            } // onResponse
        });
    } // getForecastHourly

    /**
     * Get Daily forecast for the upper bottom sheet.
     */
    private void getForecastDaily(String homeCity) {
        WeatherDataService weatherDataService = new WeatherDataService(this);
        weatherDataService.getForecastByName(homeCity, 2, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelDaily>() {
            @Override
            public void onError(String message) {
                Log.d(TAG, "onError: getForecastDaily " + message);
            }

            @Override
            public void onResponse(List<WeatherReportModelDaily> weatherReportModels) {
                Log.d(TAG, "onResponse: " + "getForecastDaily");
                getAndAssignHourlyOrDailyValues(1, weatherReportModels);
            } // onResponse
        });
    } // getForecastDaily

    /**
     * getAndAssignHourlyOrDailyValues
     *
     * @param hourlyOrDaily       0 = hourly forecast,
     *                            1 = daily forecast.
     * @param weatherReportModels List of weatherReportModels
     * @param <T>                 WeatherReportModelHourly or WeatherReportModelDaily
     */
    private <T> void getAndAssignHourlyOrDailyValues(int hourlyOrDaily, List<T> weatherReportModels) {

        Object currentModel;
        String time = null;
        int conditionImageId = 0;
        String precipitation = null;
        String temp = null;

        String valueNameSuffix = null;
        String valueProperty = null;
        String value = null;

        for (int i = 0; i < weatherReportModels.size(); i++) {

//            currentModel = (T) weatherReportModels.get(i); // do I need the casting?
            currentModel = weatherReportModels.get(i);

            if (hourlyOrDaily == 0) {

                time = ((WeatherReportModelHourly) currentModel).getTime();
                conditionImageId = ((WeatherReportModelHourly) currentModel).getConditionImageId();
                precipitation = (int) ((WeatherReportModelHourly) currentModel).getPrecipitation_probability() + "%";
                temp = (int) ((WeatherReportModelHourly) currentModel).getTemperature_2m() + "°";
                valueNameSuffix = "hour";

            } else { //if (hourlyOrDaily == 1)

                time = ((WeatherReportModelDaily) currentModel).getTime();
                conditionImageId = ((WeatherReportModelDaily) currentModel).getConditionImageId();
                precipitation = (int) ((WeatherReportModelDaily) currentModel).getPrecipitation_probability_max() + "%";
                temp = (int) ((WeatherReportModelDaily) currentModel).getTemperature_2m() + "°";
                valueNameSuffix = "day";

            } // if statement

            for (HourlyDailyFields field : HourlyDailyFields.values()) {
                switch (field) {
                    case TIME:
                        valueProperty = "Time";
                        value = time;
                        break;
                    case CONDITION:
                        valueProperty = "Condition";
                        value = String.valueOf(conditionImageId);
                        break;
                    case PRECIPITATION:
                        valueProperty = "Precipitation";
                        value = precipitation;
                        break;
                    case TEMP:
                        valueProperty = "Temp";
                        value = temp;
                        break;
                } // switch
                assignHourlyDailyValue(valueNameSuffix, i, valueProperty, value);
            } // for (field)
        } // for (weatherReportModels)
    }

    private void assignHourlyDailyValue(String valueNameSuffix, int position, String valueProperty, String value) {

        String currentViewName;
        Field field;

        try {

            if (Objects.equals(valueProperty, "Condition")) {

                currentViewName = valueNameSuffix + position + valueProperty;
                field = getClass().getDeclaredField(currentViewName);
                AppCompatImageView currentConditionImage = (AppCompatImageView) field.get(this);

                Glide.with(this).load(Integer.parseInt(value)).into(currentConditionImage);
                return;
            }

            currentViewName = valueNameSuffix + position + valueProperty;
            field = getClass().getDeclaredField(currentViewName);
            AppCompatTextView currentView = (AppCompatTextView) field.get(this);
            currentView.setText(value);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.d(TAG, "assignHourlyDailyValue: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get Detailed forecast for the Lower bottom sheet.
     */
    private void getForecastDetailed(String homeCity) {
        WeatherDataService weatherDataService = new WeatherDataService(this);
        weatherDataService.getForecastByName(homeCity, 3, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelDetailed>() {
            @Override
            public void onError(String message) {
                Log.d(TAG, "onError: getForecastDetailed");
            }

            @Override
            public void onResponse(List<WeatherReportModelDetailed> weatherReportModels) {
                Log.d(TAG, "onResponse: getForecastDetailed");
                getAndAssignDetailedValues(weatherReportModels);
            }
        });
    }


    private <T> void getAndAssignDetailedValues(List<T> weatherReportModels) {

        WeatherReportModelDetailed weatherReportModelDetailed = (WeatherReportModelDetailed) weatherReportModels.get(0);

        txtUvValue.setText(String.valueOf((int) weatherReportModelDetailed.getUv_index_max()));
        txtUvStatus.setText(weatherReportModelDetailed.getUvDescription());
        seekBarUvIndex.setProgress((int) weatherReportModelDetailed.getUv_index_max());
        sunTimeProgress = weatherReportModelDetailed.getTimePercentage();
        txtWidgetSunTimeTitle.setText(weatherReportModelDetailed.getSunTimeTitle());
        sunTimePrimary = weatherReportModelDetailed.getSunTimePrimary();
        txtSunTimePrimary.setText(sunTimePrimary);
        txtSunTimeSecondary.setText(weatherReportModelDetailed.getSunTimeSecondary());
        txtWindSpeed.setText(String.valueOf((int) weatherReportModelDetailed.getWind_speed_10m()));
        windDirectionProgress = weatherReportModelDetailed.getWindDirectionPercentage();
        String rainPrimary = weatherReportModelDetailed.getRain() + " mm";
        txtRainPrimary.setText(rainPrimary);
        String rainSecondary = weatherReportModelDetailed.getRain_sum() + " mm expected in next 24h.";
        txtRainSecondary.setText(rainSecondary);
        String feelsLike = (int) weatherReportModelDetailed.getApparent_temperature() + "°";
        txtFeelsLikeTemp.setText(feelsLike);
        txtFeelsLikeDescription.setText(weatherReportModelDetailed.getApparent_temperatureDescription());
        String humidityValue = weatherReportModelDetailed.getRelative_humidity_2m() + "%";
        txtHumidityValue.setText(humidityValue);
        String dewPoint = "The dew point is " + (int) weatherReportModelDetailed.getDew_point_2m() + " right now.";
        txtHumidityDewPoint.setText(dewPoint);
        String visibility = (int) weatherReportModelDetailed.getVisibility() + " km";
        txtVisibility.setText(visibility);
        txtVisibilityDescription.setText(weatherReportModelDetailed.getVisibilityDescription());

        sunTimeMotionLayout.setTransitionListener(sunTimeTransitionListener);
        windMotionLayout.setTransitionListener(windTransitionListener);

    }

    /**
     * Transition Listener for Sun Time animation.<p>
     * 1. The animation will be Full performed once when the bottom sheet is opened, aka. after mainMotionLayout's animation is complete.
     * 2. Then the animation will start again from the beginning and its progress will stop relative to current Sun Time value in the weatherReportModelDetailed.
     * 3. When the bottom sheet is collapsed the animation will jump to the starting position, so the previous behaviors will be achievable again.
     */
    private final MotionLayout.TransitionListener sunTimeTransitionListener = new MotionLayout.TransitionListener() {
        @Override
        public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
        }

        @Override
        public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
            if (!firstFullSunTimeAnimation && progress > sunTimeProgress) {
                sunTimeMotionLayout.setProgress(sunTimeProgress);
            }
        }

        @Override
        public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
            if (mainMotionLayout.getCurrentState() == R.id.end) {
                sunTimeMotionLayout.jumpToState(R.id.start);
                sunTimeMotionLayout.transitionToEnd();
                firstFullSunTimeAnimation = false;
            }
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
        }
    };

    /**
     * Transition Listener for Wind animation.
     * 1. The animation will be Full performed once when the bottom sheet is opened, aka. after mainMotionLayout's animation is complete.
     * 2. Then the animation will start again from the beginning and its progress will stop relative to current Wind Angle value in the weatherReportModelDetailed.
     * 3. When the bottom sheet is collapsed the animation will jump to the starting position, so the previous behaviors will be achievable again.
     */
    private final MotionLayout.TransitionListener windTransitionListener = new MotionLayout.TransitionListener() {
        @Override
        public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
        }

        @Override
        public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
            /*
             firstFullWindAnimation is needed instead of firstFullSunTimeAnimation because otherwise:
             1. After one cycle of wind transition the wind indicator is blinking one time in the wanted wind angle before starting to animate to it.
             2. If the wind transition duration is less than sun time's, sun time transition can't complete the first cycle
             */
            
            if (!firstFullWindAnimation && progress > windDirectionProgress) {
                    windMotionLayout.setProgress(windDirectionProgress);
            }
        }

        @Override
        public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
            if (mainMotionLayout.getCurrentState() == R.id.end) {
                windMotionLayout.jumpToState(R.id.start);
                windMotionLayout.transitionToEnd();
                firstFullWindAnimation = false;
            }
        }

        @Override
        public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
        }
    };

    private enum HourlyDailyFields {
        TIME,
        CONDITION,
        PRECIPITATION,
        TEMP
    }

}