package com.example.weatherapiapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class ModifiedMotionLayout extends MotionLayout {
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
    I designed the MotionLayout not to intercept and consume (not passing to other views) touches by returning false,
    except during swiping (scrolling/ACTION_MOVE), to allow the buttons on the Bottom Sheet to be clicked.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
}
