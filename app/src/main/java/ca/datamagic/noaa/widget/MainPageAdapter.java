package ca.datamagic.noaa.widget;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class MainPageAdapter extends FragmentPagerAdapter {
    private static Logger _logger = LogFactory.getLogger(MainPageAdapter.class);
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
    public void startUpdate(ViewGroup container) {
        _logger.info("startUpdate");
        super.startUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        _logger.info("instantiateItem");
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        _logger.info("destroyItem");
        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        _logger.info("finishUpdate");
        super.finishUpdate(container);
    }

    @Override
    public Parcelable saveState() {
        _logger.info("saveState");
        return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        _logger.info("restoreState");
        super.restoreState(state, loader);
    }

    @Override
    public int getCount() {
        return _fragments.size();
    }
}
