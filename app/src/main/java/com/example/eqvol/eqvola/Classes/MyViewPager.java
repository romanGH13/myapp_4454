package com.example.eqvol.eqvola.Classes;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.eqvol.eqvola.Adapters.RegistrationPagerAdapter;
import com.example.eqvol.eqvola.fragments.Registration.FirstStepFragment;

/**
 * Created by eqvol on 25.12.2017.
 */

public class MyViewPager extends ViewPager {

    private boolean enabled;

    public MyViewPager(Context context) {
        super(context);
        enabled = false;
    }


    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        enabled = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    // To enable/disable swipe
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled && detectSwipeToRight(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled && detectSwipeToRight(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    // Detects the direction of swipe. Right or left.
    // Returns true if swipe is in right direction
    public boolean detectSwipeToRight(MotionEvent event){

        int initialXValue = 0; // as we have to detect swipe to right
        final int SWIPE_THRESHOLD = 100; // detect swipe
        boolean result = false;

        try {
            float diffX = event.getX() - initialXValue;

            if (Math.abs(diffX) > SWIPE_THRESHOLD ) {
                if (diffX > 0) {
                    // swipe from left to right detected ie.SwipeRight
                    result = false;
                } else {
                    // swipe from right to left detected ie.SwipeLeft
                    result = true;
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }
}

