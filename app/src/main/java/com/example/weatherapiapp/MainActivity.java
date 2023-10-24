package com.example.weatherapiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.weatherapiapp.databinding.ActivityMainBinding;

import java.util.List;

//#freeCodeCamp.org (YT) | REST API - Network Data
public class MainActivity extends AppCompatActivity {

    /*
     Get city latitude and longitude from nominatim.org, then get the city's weather from open-meteo.com
     I used city latitude and longitude instead of city id.
     */

    private static final String TAG = "MainActivity";

    ActivityMainBinding binding;

    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModel> citiesList;
    private MotionLayout mainMotionLayout, sunriseMotionLayout, windMotionLayout;
    boolean notScrollable = true;
    private View bottomSheet;
    GestureDetector MotinLayoutGestureDetector;
    GestureDetector ScrollViewGestureDetector;
    GestureDetector DailyBtnGestureDetector;
    GestureDetector HomeIndicatorGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//        setContentView(R.layout.main_coordiator);
        setContentView(R.layout.main);

        mainMotionLayout = findViewById(R.id.main_motion_layout);
        sunriseMotionLayout = findViewById(R.id.sunrise_sunset);
        windMotionLayout = findViewById(R.id.wind);

        bottomSheet = findViewById(R.id.bottom_sheet);

        View tabbar = findViewById(R.id.tabbar);
        tabbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /*
        my ModifiedMotionLayout works fine. it doesn't consume the touches that are not meant to scroll (swipe)
        (ACTION_MOVE). So the daily btn is clickable, the transition with OnSwipe on homeIndicator works,
        and the scrollView works.
        the issue is that when i set OnTouchListener for scrollView and return true in onTouch to disable the
        scrolling when the bottom sheet is collapsed, the transition stops working, because scrollView is consuming
        all touches.
         */
        /*
        ModifiedMotionLayout, with NestedScrollView, with <OnSwipe> motion:touch Anchor Id="@id/modal",
        without OnTouchListener for scrollView works well.
         */
        // scroll view OnTouchListener
        NestedScrollView scrollView = findViewById(R.id.modal);
//        ScrollView scrollView = findViewById(R.id.modal);
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                Log.d(TAG, "onTouch: Scroll View");
//                ScrollViewGestureDetector.onTouchEvent(motionEvent);
//                return notScrollable; // = true
//
////                return false;
////                return ScrollViewGestureDetector.onTouchEvent(motionEvent);
//            }
//        });
        ScrollViewGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onDown: Scroll View");
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onShowPress: Scroll View");
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onSingleTapUp: Scroll View");
//                mainMotionLayout.setProgress(0.5f); // works
                return false;
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
//                Log.d(TAG, "onScroll: Scroll View");
//                mainMotionLayout.setProgress(v1);
//                mainMotionLayout.setProgress(v);
//                mainMotionLayout.setInterpolatedProgress(v1);
//                mainMotionLayout.setInterpolatedProgress(v);

//                // Calculate the progress based on the scroll distance
////                float scrollProgress = v1 / yourMaxScrollDistance; // Adjust as needed
//                float scrollProgress = v1 / 400; // Adjust as needed
//                // Ensure the progress is within the valid range (0 to 1)
//                scrollProgress = Math.max(0, Math.min(1, scrollProgress));
//                // Set the transition progress
//                mainMotionLayout.setProgress(scrollProgress);
//                return true;

                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onFling: Scroll View");
                return false;
            }
        });

        // daily btn OnClickListener
        Button daily = findViewById(R.id.btn_daily_forecast);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: daily btn");
            }
        });

        // daily btn OnTouchListener
//        daily.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: daily btn");
//                DailyBtnGestureDetector.onTouchEvent(motionEvent);
//                return false;
//            }
//        });
        DailyBtnGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onDown: daily");
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onShowPress: daily");
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onSingleTapUp: daily");
                return false;
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onFling: daily");
                return false;
            }
        }); // DailyBtnGestureDetector

        // homeIndicator OnTouchListener
        ImageView homeIndicator = findViewById(R.id.segmented_control_modal_open_background);
//        homeIndicator.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d(TAG, "onTouch: home Indicator");
//                HomeIndicatorGestureDetector.onTouchEvent(motionEvent);
//                return false;
//            }
//        });
//        HomeIndicatorGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener())
        HomeIndicatorGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onDown: Home Indicator");
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) { }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onSingleTapUp: Home Indicator");
                return false;
            }
            @Override
            public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onScroll: Home Indicator");
                return false;
            }
            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {            }

            @Override
            public boolean onFling(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onFling: Home Indicator");
                return false;
            }
        });

        // + MotionLayout OnTouchListener
        ImageView house = findViewById(R.id.house);
//        MotionLayout myMainMotionLayoutObj = new MotionLayout();
        //try these
//        mainMotionLayout.overScroll
//        mainMotionLayout.nestedScrol

        // Motion Layout's OnTouchListener
//        mainMotionLayout.setOnTouchListener(new View.OnTouchListener() {   // working
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
////                if (view == daily) {
////                }
////                    mainMotionLayout.setInteractionEnabled(false);
//                Log.d(TAG, "onTouch: motion layout " + motionEvent);
//                MotinLayoutGestureDetector.onTouchEvent(motionEvent);
//
////                    mainMotionLayout.onInterceptTouchEvent(motionEvent);
////                    mainMotionLayout.requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//            //try implementing performClick()
////            @Override
////            public boolean performClick(){} //try this too
//        });
        MotinLayoutGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
//                mainMotionLayout.transitionToEnd();
                Log.d(TAG, "onDown: motion layout");
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {}

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onSingleTapUp: motion layout");
                return false;
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
//                mainMotionLayout.transitionToEnd();
//                    mainMotionLayout.setInterpolatedProgress(0.5f);  ///try this
                Log.d(TAG, "onScroll: motion layout");

                //try creating OnSwipe object
//                mainMotionLayout.getTransition(R.id.transition1).setInterpolatorInfo();
//                OnSwipe monSwipe = new OnSwipe();
//                monSwipe.
//                mainMotionLayout.getTransition(R.id.transition1).setOnSwipe(monSwipe);
                return false;
            }
            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {

            }
            @Override
            public boolean onFling(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onFling: motion layout");
                return false;
            }
        });

        // MotionLayout's TransitionListener
        mainMotionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                Log.d(TAG, "onTransitionStarted: ");
            }
            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
//                Log.d(TAG, "onTransitionChange: ");
                ///try this
//                    mainMotionLayout.setInterpolatedProgress(0.5f);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.end) {
                    sunriseMotionLayout.transitionToEnd();
                    windMotionLayout.transitionToEnd();
                    notScrollable = false;
                    Log.d(TAG, "onTransitionCompleted: End. Not scrollable" + notScrollable);
                }
                if (currentId == R.id.start) {
                    sunriseMotionLayout.jumpToState(R.id.start);
                    windMotionLayout.jumpToState(R.id.start);
                    notScrollable = true;
                    Log.d(TAG, "onTransitionCompleted: Start. Not scrollable" + notScrollable);
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
                Log.d(TAG, "onTransitionTrigger: ");
                // Triggered during the animation
//                if (triggerId == R.id.your_swipe_trigger && positive) {
                    // Programmatically initiate the transition
//                    motionLayout.transitionToEnd(); // Or another appropriate method
//                }
            }
        });


        //bottom sheet Old
//        BottomSheetDragHandleView sheet = findViewById(R.id.modal);
//        sheet.

//        View bottomSheet = findViewById(R.id.modal);
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        bottomSheetBehavior.setPeekHeight(500);
//        bottomSheetBehavior.setHideable(false);

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
        String[] times = new String[] {
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