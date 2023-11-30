package com.example.weatherapiapp;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * This class is used by the Recycle View of CitiesActivity to handle item interactions.
 * It includes: 1. Callback of ItemTouchHelper to handle item swipes. <p>
 *              2. OnItemTouchListener with GestureDetector to handle item Long presses.
 */
public class MyItemTouchHandler extends ItemTouchHelper.SimpleCallback implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "ItemTouch";

    private GestureDetector gestureDetector;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModelShort> citiesList;

    public MyItemTouchHandler(Context context, RecyclerView recyclerView, CityListAdapter adapter, List<WeatherReportModelShort> citiesList) {
        super(0, ItemTouchHelper.START);
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.citiesList = citiesList;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: ");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = recyclerView.getChildAdapterPosition(childView);
                    showLongPressMessage(position);
                }
            }
        });
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped: ");
        int position = viewHolder.getAdapterPosition();
        citiesList.remove(position);
        adapter.notifyItemRemoved(position);
        showSwipeMessage();
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        gestureDetector.onTouchEvent(e);
        return false;
//        return true;
//        return super.onInterceptTouchEvent(recyclerView, e);
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private void showLongPressMessage(int position) {
        Snackbar snackbar = Snackbar.make(recyclerView, "Long press on item " + position, BaseTransientBottomBar.LENGTH_SHORT);
        snackbar.show();
    }

    private void showSwipeMessage() {
        Snackbar snackbar = Snackbar.make(recyclerView, "City deleted", BaseTransientBottomBar.LENGTH_LONG);
        snackbar.show();
    }
}

