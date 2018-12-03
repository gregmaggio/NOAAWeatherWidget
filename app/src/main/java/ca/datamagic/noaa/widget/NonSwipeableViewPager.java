package ca.datamagic.noaa.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class NonSwipeableViewPager extends ViewPager {
    private boolean _enabled = true;

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public boolean getEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean newVal) {
        _enabled = newVal;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!_enabled) {
            // If we are disabled, but the touch event is outside the current fragment view, then re-enable the pager
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                MainPageAdapter mainPageAdapter = mainActivity.getMainPageAdapter();
                if (mainPageAdapter != null) {
                    Fragment currentFragment = mainPageAdapter.getItem(getCurrentItem());
                    if ( currentFragment != null) {
                        if (!inViewInBounds(currentFragment.getView(), (int) ev.getX(), (int) ev.getY())) {
                            _enabled = true;
                        }
                    }
                }
            }
        }
        if (_enabled) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    private boolean inViewInBounds(View view, int x, int y) {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
}
