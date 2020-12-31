package ca.datamagic.noaa.widget;

import android.content.Context;
import android.os.Parcelable;
import android.view.ViewGroup;

import java.util.logging.Logger;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class MainPageAdapter extends FragmentStatePagerAdapter {
    private static Logger _logger = LogFactory.getLogger(MainPageAdapter.class);
    public static final int ObservationIndex = 0;
    public static final int HourlyForecastIndex = 1;
    public static final int ForecastIndex = 2;
    public static final int DiscussionIndex = 3;
    public static final int SatelliteIndex = 4;
    private String[] _pageTitles = new String[5];
    private Fragment[] _fragments = new Fragment[5];

    public MainPageAdapter(FragmentManager manager, Context context) {
        super(manager);
        _pageTitles[ObservationIndex] = context.getResources().getString(R.string.observation_page_title);
        _pageTitles[HourlyForecastIndex] = context.getResources().getString(R.string.hourlyforecast_page_title);
        _pageTitles[ForecastIndex] = context.getResources().getString(R.string.forecast_page_title);
        _pageTitles[DiscussionIndex] = context.getResources().getString(R.string.discussion_page_title);
        _pageTitles[SatelliteIndex] = context.getResources().getString(R.string.satellite_page_title);
    }

    public void performCleanup(int position) {
        Fragment currentFragment = getItem(position);
        if (currentFragment != null) {
            Renderer renderer = (Renderer) currentFragment;
            renderer.cleanup();
        }
    }

    public void refreshPage(FragmentManager manager, int position) {
        Fragment currentFragment = getItem(position);
        if (currentFragment != null) {
            Renderer renderer = (Renderer) currentFragment;
            renderer.render();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _pageTitles[position];
    }

    public int getLayoutId(int position) {
        switch (position) {
            case ObservationIndex: return R.layout.observation_main;
            case HourlyForecastIndex: return R.layout.forecast_main;
            case ForecastIndex: return R.layout.forecast_main;
            case DiscussionIndex: return R.layout.discussion_main;
            case SatelliteIndex: return R.layout.satellite_main;
        }
        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        if (_fragments[position] == null) {
            switch (position) {
                case ObservationIndex:
                    _fragments[position] = ObservationFragment.newInstance();
                    break;
                case HourlyForecastIndex:
                    _fragments[position] = HourlyForecastFragment.newInstance();
                    break;
                case ForecastIndex:
                    _fragments[position] = ForecastFragment.newInstance();
                    break;
                case DiscussionIndex:
                    _fragments[position] = DiscussionFragment.newInstance();
                    break;
                case SatelliteIndex:
                    _fragments[position] = SatelliteFragment.newInstance();
                    break;
            }
        } else {
            switch (position) {
                case ObservationIndex:
                    break;
                case HourlyForecastIndex:
                    break;
                case ForecastIndex:
                    break;
                case DiscussionIndex:
                    break;
                case SatelliteIndex:
                    break;
            }
        }
        return _fragments[position];
    }

    @Override
    public void startUpdate(ViewGroup container) {
        _logger.info("startUpdate");
        super.startUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        _logger.info("instantiateItem. position: " + position);
        Object item = super.instantiateItem(container, position);
        if (item == null) {
            _logger.info("item is null");
            _fragments[position] = null;
        } else {
            _logger.info("item type: " + item.getClass().getName());
            _fragments[position] = (Fragment)item;
        }
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        _logger.info("destroyItem. position: " + position);
        _fragments[position] = null;
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
        return _fragments.length;
    }
}
