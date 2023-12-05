package com.example.weatherapiapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
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
public class MyItemTouchHandler extends ItemTouchHelper.SimpleCallback implements RecyclerView.OnItemTouchListener, MenuItem.OnMenuItemClickListener {

    private static final String TAG = "ItemTouch";

    private GestureDetector gestureDetector;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;
    private List<WeatherReportModelShort> citiesList;

    private int currentViewPosition;

    public MyItemTouchHandler(Context context, RecyclerView recyclerView, CityListAdapter adapter, List<WeatherReportModelShort> citiesList) {
        super(0, ItemTouchHelper.START);
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.citiesList = citiesList;
//        this.recyclerView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: gesture");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null) {
                    int position = recyclerView.getChildAdapterPosition(childView);
                    showLongPressMessage(position);
                    setCurrentViewPosition(position);
//                    childView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);
//                    ((Activity) context).registerForContextMenu(childView);

////                    recyclerView.contextgetContext()
////                    onContextClick(e);
////                   recyclerView.showContextMenuForChild(recyclerView, e.getX(), e.getY());
                }
            }
        });

    }

    public MyItemTouchHandler() {
        super(0, ItemTouchHelper.START);

    }

    public View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            // take this to the handler or adapter/ no move it to the adapter
            menu.add(0, 0, 0, "Setd as home")
                    .setOnMenuItemClickListener(MyItemTouchHandler.this);
            menu.add(0, 0, 1, "Removed city")
                    .setOnMenuItemClickListener(MyItemTouchHandler.this);
        }
    };
    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        switch (item.getOrder()) {
            case 0:
                Log.d(TAG, "onContextItemSelected: 0");
                return true;
            case 1:
                Log.d(TAG, "onContextItemSelected: 1");
//                Log.d(TAG, "Current rv item position " + itemTouchHandler.getCurrentViewPosition());
//                removeItem(itemTouchHandler.getCurrentViewPosition());
                int p = adapter.getPosition();
                Log.d(TAG, "Current rv item position " + p);
                removeItem(p);
                return true;
        }
        Log.d(TAG, "onContextItemSelected: default");
        return false;
    }
    private void removeItem(int position) {
        citiesList.remove(position);
        adapter.notifyItemRemoved(position);
        // Show a confirmation message or perform other actions
    }
    //        holder.view.setOnCreateContextMenuListener { contextMenu, _, _ ->
//                contextMenu.add("Add").setOnMenuItemClickListener {
//            longToast("I'm pressed for the item at position => $position")
//            true
//        }
//        }
    MenuItem.OnMenuItemClickListener m = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            return false;
        }
    };

    private void setCurrentViewPosition(int currentViewPosition) {
        this.currentViewPosition = currentViewPosition;
    }

    public int getCurrentViewPosition() {
        return currentViewPosition;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        citiesList.remove(position);
        adapter.notifyItemRemoved(position);
        showSwipeMessage();
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
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

