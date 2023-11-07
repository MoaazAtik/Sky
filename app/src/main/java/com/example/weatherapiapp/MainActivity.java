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

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
        setContentView(R.layout.main);

        Intent citiesIntent = new Intent(MainActivity.this, CitiesActivity.class);
        MainActivity.this.startActivity(citiesIntent);

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
                Log.d(TAG, "onResponse: main acativiy");
            }
        });
    }

}