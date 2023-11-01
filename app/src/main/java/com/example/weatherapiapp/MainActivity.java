package com.example.weatherapiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.HorizontalScrollView;

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

    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModel> citiesList;
    private MotionLayout mainMotionLayout, sunriseMotionLayout, windMotionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
        setContentView(R.layout.main);

        mainMotionLayout = findViewById(R.id.main_motion_layout);
        sunriseMotionLayout = findViewById(R.id.sunrise_sunset);
        windMotionLayout = findViewById(R.id.wind);

        AppCompatButton btnHourlyForecast = findViewById(R.id.btn_hourly_forecast);
        AppCompatButton btnDailyForecast = findViewById(R.id.btn_daily_forecast);
        ConstraintLayout tabBar = findViewById(R.id.tab_bar);
        AppCompatButton btnHomePlus = findViewById(R.id.btn_home_plus);
        AppCompatButton btnHomeExpand = findViewById(R.id.btn_home_expand);
        AppCompatButton btnHomeCitiesList = findViewById(R.id.btn_home_cities_list);

        // btnHourlyForecast OnClickListener
        btnHourlyForecast.setOnClickListener(v -> {
            Log.d(TAG, "onClick: btnHourlyForecast");
        });

        LinearLayoutCompat hourlyForecast = findViewById(R.id.hourly_forecast);
        LinearLayoutCompat dailyForecast = findViewById(R.id.daily_forecast);
//        LinearLayoutCompat hourlyDailyll = findViewById(R.id.hourly_daily_ll);

        Animation translateHourlyOutAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_hourly_out);
        Animation translateDailyInAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_daily_in);

//        LayoutTransition layoutTransition = hourlyDailyll.getLayoutTransition();
//        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
//        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
//        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
//        layoutTransition.enableTransitionType(LayoutTransition.APPEARING);
//        layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING);

//        HorizontalScrollView horizontalScrollView = findViewById(R.id.hourly_daily_forecast);
        HorizontalScrollView horizontalScrollViewHourly = findViewById(R.id.hourly_forecast_sv);
        HorizontalScrollView horizontalScrollViewDaily = findViewById(R.id.daily_forecast_sv);
//        horizontalScrollView.setSmoothScrollingEnabled(true);
//        horizontalScrollView.smoothScrollBy();
//        horizontalScrollView.scrollTo();
//        horizontalScrollView.scrollBy(30,0);
//        horizontalScrollView.scrollBy(hourlyForecast.getWidth(),0);

        btnHourlyForecast.setOnClickListener(v -> {
//            Log.d(TAG, "onCreate: " + v);
//            hourlyDailyll.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
//            ConstraintLayout.LayoutParams hourlyForecastLayoutParams = (ConstraintLayout.LayoutParams) hourlyForecast.getLayoutParams();
//            hourlyForecastLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
//            hourlyForecast.setLayoutParams(hourlyForecastLayoutParams);
//
//            ConstraintLayout.LayoutParams dailyForecastLayoutParams = (ConstraintLayout.LayoutParams) dailyForecast.getLayoutParams();
//            dailyForecastLayoutParams.startToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // This sets the view's left edge to the start of the parent.
//            dailyForecast.setLayoutParams(dailyForecastLayoutParams);

//            dailyForecast.setVisibility(View.GONE);
//            hourlyForecast.setVisibility(View.VISIBLE);

            float widthScreen = getResources().getDisplayMetrics().widthPixels;
            int widthDailyLL = horizontalScrollViewDaily.getChildAt(0).getWidth();
            Log.d(TAG, "child width widthDailyLL " + widthDailyLL); // 1443
            int widthDailySV = horizontalScrollViewDaily.getWidth(); // in this case equals widthScreen
            Log.d(TAG, "scrollview width widthDailySV "+ widthDailySV); // 1080
            int maxScroll = widthDailyLL - widthDailySV;
            Log.d(TAG, "max scroll " + maxScroll); // 363 // but actually reflected in currentScroll when the view is scrolled to end as 363 + padding px (42x2) = 447
            int m = horizontalScrollViewDaily.getMaxScrollAmount();
            Log.d(TAG, "getMaxScrollAmount m "+ m); //540
            int currentScroll = horizontalScrollViewDaily.getScrollX();
            Log.d(TAG, "currentScroll " + currentScroll);

//            int durationScrollAnimation = (int) (((maxScroll + 42) - currentScroll) * 1.5);
            int durationScrollAnimation = (int) (currentScroll * 1.5);
            Log.d(TAG, "durationScrollAnimation " + durationScrollAnimation); // max 670
            ObjectAnimator animator = ObjectAnimator.ofInt(horizontalScrollViewDaily, "scrollX", 0);
            animator.setDuration(durationScrollAnimation); // to avoid unexplained delay of moving hourly and daily views
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }
                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
//                    horizontalScrollViewHourly.setVisibility(View.VISIBLE);
                    horizontalScrollViewDaily.animate().translationX(0).setDuration(900).setInterpolator(new LinearInterpolator());
                    horizontalScrollViewHourly.animate().translationX(0).setDuration(900).setInterpolator(new LinearInterpolator());
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
            Log.d(TAG, "onClick: btnDailyForecast");
//            hourlyForecast.setTranslationX(10);
//            btnHomeExpand.setTranslationX(0);
//            btnHourlyForecast.animate().translationX(40).start();
//            dailyForecast.setTranslationX(-100);
//            dailyForecast.animate().translationXBy(-100);

//            hourlyDailyll.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
//            hourlyForecast.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
//            dailyForecast.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

//hourlyDailyll.startAnimation(tra);
//            hourlyForecast.startAnimation(translateHourlyOutAnimation);
//            dailyForecast.startAnimation(translateDailyInAnimation);

//        horizontalScrollView.setSmoothScrollingEnabled(true);
//        horizontalScrollView.smoothScrollBy();
//        horizontalScrollView.scrollTo();
//        horizontalScrollView.scrollBy(30,0);
//        horizontalScrollView.scrollBy(hourlyForecast.getWidth(),0);

//            hourlyDailyll.scrollBy(30,0); // can't be scrolled again by the user (wanted behavior), but can't be .smoothScrollTo
//            horizontalScrollView.scrollBy(30,0); // can be scrolled again by use (unwanted behavior), but can be .smoothScrollTo
//            horizontalScrollView.smoothScrollTo(90,0); // can be scrolled again by use (unwanted behavior)

//            hourlyDailyll.scrollBy(90,0);
//            horizontalScrollView.scrollTo(90,0);
//            hourlyDailyll.scrollTo(90,0); // = .setScrollX(90)
//            hourlyDailyll.setScrollX(90);


//            horizontalScrollView.smoothScrollTo(hourlyForecast.getWidth(),0);
//            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    hourlyForecast.setVisibility(View.GONE);
//                    dailyForecast.setVisibility(View.VISIBLE);
//                }
//            },2000);

            View bv = findViewById(R.id.whitev);

            float marginStartHourly = 16;
            float density = getResources().getDisplayMetrics().density;
            float px = marginStartHourly * density;
//            float a = (float) (horizontalScrollViewHourly.getWidth() + px);
            float widthScreen = getResources().getDisplayMetrics().widthPixels;
//            Log.d(TAG, "Hourly a " + horizontalScrollViewHourly.getWidth());
//            Log.d(TAG, "Hourly a " +horizontalScrollViewHourly.getMinimumWidth());
//            Log.d(TAG, "Hourly a " +horizontalScrollViewHourly.getMeasuredWidth());
////            Log.d(TAG, "onCreate: density " + density );
////            Log.d(TAG, "onCreate: px " + px);
//
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getWidth()); // 0
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getMinimumWidth());// 0
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getMeasuredWidth()); // 0

//            float b =  (bv.getWidth());
//            Log.d(TAG, "onCreate: b " + b);
//            Log.d(TAG, "onCreate: b " + bv.getMinimumWidth());
//            Log.d(TAG, "onCreate: b " + bv.getMeasuredWidth());
//            float c = tabBar.getWidth();
//            Log.d(TAG, "onCreate: c " + c);
//            Log.d(TAG, "onCreate: c " + tabBar.getMinimumWidth());
//            Log.d(TAG, "onCreate: c " + tabBar.getMeasuredWidth());
//            Log.d(TAG, "onCreate: c " + tabBar.getMaxWidth());
//            Log.d(TAG, "onCreate: c " + tabBar.getMeasuredWidthAndState());

            // horizontalScrollViewDaily was not visible after the transition until scrolling
            // it by hand and not with .scrollTo(). Setting its visibility at runtime here,
            // and making its visibility gone in xml fixed this issue.
            //horizontalScrollViewDaily.setVisibility(View.VISIBLE);

            /*
            After setting Start_toEndOf="@id/hourly_forecast_sv" to daily_forecast_sv,
            with layout_width="match_parent", daily_forecast_sv was drawn on top of hourly_forecast_sv
            when the app runs. Setting layout_width="0dp" fixed it.
             */

            //horizontalScrollViewHourly.animate().translationX(-a).setDuration(1100);
//            Log.d(TAG, "Hourly a " + horizontalScrollViewHourly.getWidth());
//            Log.d(TAG, "Hourly a " +horizontalScrollViewHourly.getMinimumWidth());
//            Log.d(TAG, "Hourly a " +horizontalScrollViewHourly.getMeasuredWidth());
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getWidth()); // 0
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getMinimumWidth());// 0
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getMeasuredWidth()); // 0
////            horizontalScrollViewDaily.setVisibility(View.VISIBLE);
            //horizontalScrollViewDaily.animate().translationX(-a).setDuration(1100);
////            horizontalScrollViewDaily.setVisibility(View.VISIBLE);
//            Log.d(TAG, "Hourly a " + horizontalScrollViewHourly.getWidth());
//            Log.d(TAG, "Hourly a " +horizontalScrollViewHourly.getMinimumWidth());
//            Log.d(TAG, "Hourly a " +horizontalScrollViewHourly.getMeasuredWidth());
//            // Why equals 0...(?)
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getWidth()); // Why equals 0...?
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getMinimumWidth());// 0
//            Log.d(TAG, "Daily " + horizontalScrollViewDaily.getMeasuredWidth()); // 0

//            horizontalScrollViewHourly.smoothScrollBy(horizontalScrollViewHourly.getMaxScrollAmount(), 0);
            int widthHourlyLL = horizontalScrollViewHourly.getChildAt(0).getWidth();
            Log.d(TAG, "child width widthHourlyLL " + widthHourlyLL); // 1644
            int widthHourlySV = horizontalScrollViewHourly.getWidth();
            Log.d(TAG, "scrollview width widthHourlySV "+ widthHourlySV); // 1080
            int maxScroll = widthHourlyLL - widthHourlySV;
            int actualMaxScroll = maxScroll + horizontalScrollViewHourly.getPaddingStart() + horizontalScrollViewHourly.getPaddingEnd();
            Log.d(TAG, "max scroll " + maxScroll); // 564 // but actually 564 + padding px (42x2) = 648
            Log.d(TAG, "paddingStart: "+ horizontalScrollViewHourly.getPaddingStart());
            Log.d(TAG, "paddingEnd: "+ horizontalScrollViewHourly.getPaddingEnd());

//            int m = horizontalScrollViewHourly.getMaxScrollAmount();
//            Log.d(TAG, "getMaxScrollAmount m "+ m); //540
            int currentScroll = horizontalScrollViewHourly.getScrollX();
            Log.d(TAG, "currentScroll " + currentScroll);
            Log.d(TAG, "padding start 16 px " + px);//42
//            Interpolator interpolator = new Interpolator(9);
//            horizontalScrollViewHourly.smoothScrollTo(max+42, 0);
//            ObjectAnimator.ofObject()
//
//            horizontalScrollViewDaily.setVisibility(View.VISIBLE);
//            horizontalScrollViewHourly.animate().translationX(-a).setDuration(1100).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(700);
//            horizontalScrollViewDaily.animate().translationX(-a).setDuration(1100).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(700);
//            int ii = (max + 42) * 900;
            int durationScrollAnimation = (int) ((actualMaxScroll - currentScroll) * 1.5);
            Log.d(TAG, "durationScrollAnimation " + durationScrollAnimation); // max 910
            ObjectAnimator animator = ObjectAnimator.ofInt(horizontalScrollViewHourly, "scrollX", actualMaxScroll);
            animator.setDuration(durationScrollAnimation); // to avoid unexplained delay of moving hourly and daily views
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }
                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    horizontalScrollViewDaily.setVisibility(View.VISIBLE);
                    horizontalScrollViewHourly.animate().translationX(-widthScreen).setDuration(900).setInterpolator(new LinearInterpolator());
                    horizontalScrollViewDaily.animate().translationX(-widthScreen).setDuration(900).setInterpolator(new LinearInterpolator());
                }
                @Override
                public void onAnimationCancel(@NonNull Animator animation) {

                }
                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {

                }
            });

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {

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

        //cities layout
//        setContentView(R.layout.cities);
//        recyclerView = (RecyclerView) findViewById(R.id.rv_cities);
//        citiesList = new ArrayList<>();
//        adapter = new CityListAdapter(this, citiesList);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//
//        prepareData();
//
//        findViewById(R.id.btn_cities_add_city).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addCity();
//            }
//        });


        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

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

    // Cities layout methods
    private void prepareData() {
        String[] times = new String[]{
                "11", "222"
        };

        WeatherReportModel weatherReportModel = new WeatherReportModel();
        weatherReportModel.setTime(times[0]);
        citiesList.add(weatherReportModel);

        adapter.notifyDataSetChanged();
    }

    private void addCity() {
        WeatherReportModel weatherReportModel = new WeatherReportModel();
        weatherReportModel.setTime("333");
        citiesList.add(weatherReportModel);

        adapter.notifyDataSetChanged();
    }
}