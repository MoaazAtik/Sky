package com.example.weatherapiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

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

//    private ConstraintLayout citiesLayout;
//    private RecyclerView recyclerView;
//    private CityListAdapter adapter;
//    private List<WeatherReportModel> citiesList;
    private MotionLayout mainMotionLayout, sunriseMotionLayout, windMotionLayout;
    private AppCompatTextView txtMainCity, txtMainTemp, txtMainHTemp, txtMainLTemp, txtMainCondition;
    private AppCompatTextView hour0Time, hour1Time, hour2Time, hour3Time, hour4Time, hour5Time, hour6Time, hour7Time;
    private AppCompatImageView hour0Condition, hour1Condition, hour2Condition, hour3Condition, hour4Condition, hour5Condition, hour6Condition, hour7Condition;
    private AppCompatTextView hour0Precipitation, hour1Precipitation, hour2Precipitation, hour3Precipitation, hour4Precipitation, hour5Precipitation, hour6Precipitation, hour7Precipitation;
    private AppCompatTextView hour0Temp, hour1Temp, hour2Temp, hour3Temp, hour4Temp, hour5Temp, hour6Temp, hour7Temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
        setContentView(R.layout.main);

        // Open Cities Activity
//        Intent citiesIntent = new Intent(MainActivity.this, CitiesActivity.class);
//        MainActivity.this.startActivity(citiesIntent);

        mainMotionLayout = findViewById(R.id.main_motion_layout);
        sunriseMotionLayout = findViewById(R.id.sunrise_sunset);
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
        txtMainCondition = findViewById(R.id.txt_home_condition);

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


        Log.d(TAG, "onCreate: ");
        getForecastShort();
        getForecastHourly();

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

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.end) {
                    // Initial animation of sunrise and wind indicator
                    sunriseMotionLayout.transitionToEnd();
                    windMotionLayout.transitionToEnd();
                }
                if (currentId == R.id.start) {
                    sunriseMotionLayout.jumpToState(R.id.start);
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

    // getForecastShort(). Get short forecast for the main weather details in the middle of the main screen.
    private void getForecastShort() {

        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

        weatherDataService.getForecastByName("rio",
                0, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelShort>() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
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
                        String city = weatherReportModelShort.getCity() + ", " + weatherReportModelShort.getCountry();
                        txtMainCity.setText(city);
                        String condition = weatherReportModelShort.getCondition();
                        txtMainCondition.setText(condition);

//                        citiesList.add(weatherReportModelShort);
//                        adapter.notifyDataSetChanged();
                    }
                });
    } // getForecastShort

    private void getForecastHourly() {
        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
//        WeatherDataService weatherDataService = new WeatherDataService(this);

        weatherDataService.getForecastByName("rio", 1, new WeatherDataService.ListenerGetForecastByLatL<WeatherReportModelHourly>() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<WeatherReportModelHourly> weatherReportModels) {
                Log.d(TAG, "onResponse: main activity " + "getForecastHourly");

                getAndAssignHourlyOrDailyValues(0, weatherReportModels);

            } // onResponse
        });
    } // getForecastHourly

    /**
     *
     * @param hourlyOrDaily 0 = hourly forecast, 1 = daily forecast
     * @param weatherReportModels List of weatherReportModels
     * @param <T> WeatherReportModelHourly or WeatherReportModelDaily
     */
//    private <T> void getAndAssignHourlyDailyForecast(int hourlyOrDaily, List<T> weatherReportModels, HourlyDailyFields fieldName) {
    private <T> void getAndAssignHourlyOrDailyValues(int hourlyOrDaily, List<T> weatherReportModels) {

        Object currentModel;
        String time = null;
        String condition = null;
        String precipitation = null;
        String temp = null;

        String valueNameSuffix = null;
        String valueProperty = null;
        String value = null;

        for (int i = 0; i < weatherReportModels.size(); i++) {

            currentModel = (T) weatherReportModels.get(i); // do I need the casting?

            if (hourlyOrDaily == 0) {

//                String time = "L:" + weatherReportModelHourly.getTime();
                time = "12 AM";
                condition = ((WeatherReportModelHourly) currentModel).getCondition();
                precipitation = (int) ((WeatherReportModelHourly) currentModel).getPrecipitation_probability() + "%";
                temp = (int) ((WeatherReportModelHourly) currentModel).getTemperature_2m() + "°";
                valueNameSuffix = "hour";

            } else { //if (hourlyOrDaily == 1)

//                String time = "L:" + weatherReportModelHourly.getTime();
//                time = "12 AM";
//                condition = ((WeatherReportModelDaily) currentModel).getCondition();
//                precipitation = (int) ((WeatherReportModelDaily) currentModel).getPrecipitation_probability() + "%";
//                temp = (int) ((WeatherReportModelDaily) currentModel).getTemperature_2m() + "°";
//                valueNameSuffix = "day";

            } // if statement

            for (HourlyDailyFields field : HourlyDailyFields.values()) {
                switch (field) {
                    case TIME:
                        valueProperty = "Time";
                        value = time;
                        break;
                    case CONDITION:
                        valueProperty = "Condition";
                        value = condition;
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
//            currentConditionImage.setImageResource(R.drawable.arrow_back);
//                Log.d(TAG, "assignHourlyDailyTextValues: current condition image");

                return;
            }

            currentViewName = valueNameSuffix + position + valueProperty;
            field = getClass().getDeclaredField(currentViewName);
            AppCompatTextView currentView = (AppCompatTextView) field.get(this);
            currentView.setText(value);
//            Log.d(TAG, "assignHourlyDailyTextValues: " + currentViewName);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


//        WeatherReportModelHourly weatherReportModelHourly = new WeatherReportModelHourly();
//
//    Object currentModel;
//    WeatherReportModelHourly weatherReportModelHourly;
//        switch (hourlyOrDaily) {
//        case 0:
////                currentModel = new WeatherReportModelHourly();
//
//            for (int i = 0; i < weatherReportModels.size(); i++) {
////            weatherReportModelHourly = (WeatherReportModelHourly) weatherReportModels.get(i);
//                currentModel = (WeatherReportModelHourly) weatherReportModels.get(i);
//
////                    String time = "L:" + weatherReportModelHourly.getTime();
//                String time = "12 AM";
//                String condition;
//                condition = weatherReportModelHourly.getCondition();
//                condition = weatherReportModelHourly.getCondition();
//                String precipitation;
//                precipitation = (int) weatherReportModelHourly.getPrecipitation_probability() + "%";
//                T = weatherReportModelHourly;
//                String temp = (int) weatherReportModelHourly T.getTemperature_2m() + "°";
//                String currentViewName;
//                Field field;
//                AppCompatTextView currentView;
//                AppCompatImageView currentConditionImage;
//                try {
//                    currentViewName = "hour" + i + "Time";
//                    field = getClass().getDeclaredField(currentViewName);
//                    currentView = (AppCompatTextView) field.get(this);
//                    currentView.setText(time);
//
//                    currentViewName = "hour" + i + "Condition";
//                    field = getClass().getDeclaredField(currentViewName);
//                    currentConditionImage = (AppCompatImageView) field.get(this);
//                    currentConditionImage.setImageResource(R.drawable.arrow_back);
////                        currentConditionImage.setImageResource(R.drawable.arrow_back);
//
//                    currentViewName = "hour" + i + "Precipitation";
//                    field = getClass().getDeclaredField(currentViewName);
//                    currentView = (AppCompatTextView) field.get(this);
//                    currentView.setText(precipitation);
//
//                    currentViewName = "hour" + i + "Temp";
//                    field = getClass().getDeclaredField(currentViewName);
//                    currentView = (AppCompatTextView) field.get(this);
//                    currentView.setText(temp);
//                } catch (NoSuchFieldException | IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//
//            } // for
//            break;
//        case 1:
//            break;
//    }


//    currentViewName = valueNameSuffix + i + valueProperty;
//    field = getClass().getDeclaredField(currentViewName);
//    currentView = (AppCompatTextView) field.get(this);
//                currentView.setText(time);
//
//    currentViewName = valueNameSuffix + i + valueProperty;
//    field = getClass().getDeclaredField(currentViewName);
//    currentConditionImage = (AppCompatImageView) field.get(this);
//                currentConditionImage.setImageResource(R.drawable.arrow_back);
////                        currentConditionImage.setImageResource(R.drawable.arrow_back);
//
//    currentViewName = valueNameSuffix + i + valueProperty;
//    field = getClass().getDeclaredField(currentViewName);
//    currentView = (AppCompatTextView) field.get(this);
//                currentView.setText(precipitation);
//
//    currentViewName = valueNameSuffix + i + valueProperty;
//    field = getClass().getDeclaredField(currentViewName);
//    currentView = (AppCompatTextView) field.get(this);
//                currentView.setText(temp);

//
//                switch (fieldName) {
//        case TIME:
//            valueProperty = "Time";
//            value = time;
//            break;
//        case CONDITION:
//            valueProperty = "Condition";
//            value = condition;
//            break;
//        case PRECIPITATION:
//            valueProperty = "Precipitation";
//            value = precipitation;
//            break;
//        case TEMP:
//            valueProperty = "Temp";
//            value = temp;
//            break;
//    } // switch


    private enum HourlyDailyFields {
        TIME,
        CONDITION,
        PRECIPITATION,
        TEMP
    }

}