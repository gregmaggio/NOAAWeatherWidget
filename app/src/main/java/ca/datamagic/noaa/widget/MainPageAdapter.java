package ca.datamagic.noaa.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 1/10/2016.
 */
public class MainPageAdapter extends FragmentPagerAdapter {
    public static final int ObservationIndex = 0;
    public static final int ForecastIndex = 1;
    public static final int DiscussionIndex = 2;
    public static final int SkewTIndex = 3;
    public static final int DebugIndex = 4;
    private List<Fragment> _fragments = new ArrayList<Fragment>();

    public MainPageAdapter(FragmentManager manager) {
        super(manager);
        _fragments.add(new ObservationFragment());
        _fragments.add(new ForecastFragment());
        _fragments.add(new DiscussionFragment());
        _fragments.add(new SkewTFragment());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case ObservationIndex: return  "Current";
            case ForecastIndex: return  "Forecast";
            case DiscussionIndex: return  "Discussion";
            case SkewTIndex: return "SkewT";
        }
        return "";
    }

    @Override
    public Fragment getItem(int position) {
        return _fragments.get(position);
    }
    @Override
    public int getCount() {
        return _fragments.size();
    }
}
