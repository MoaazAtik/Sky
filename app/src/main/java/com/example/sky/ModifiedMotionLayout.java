package com.example.sky;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class ModifiedMotionLayout extends MotionLayout {

    private static final String TAG = "ModifiedMotionLayout";

    public ModifiedMotionLayout(@NonNull Context context) {
        super(context);
    }

    public ModifiedMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ModifiedMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    I designed the MotionLayout to refrain from intercepting and consuming touches (not passing them to other views) by returning false,
    except during swiping (scrolling/ACTION_MOVE). This allows the buttons on the Bottom Sheet to be clicked.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            Log.d(TAG, "onInterceptTouchEvent: return super.");
            return super.onInterceptTouchEvent(event);
        }
//        Log.d(TAG, "onInterceptTouchEvent: return false");
        return false;
    }
}
