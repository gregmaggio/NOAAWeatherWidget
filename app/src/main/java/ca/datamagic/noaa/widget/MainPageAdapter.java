package ca.datamagic.noaa.widget;

import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class MainPageAdapter extends FragmentStateAdapter {
    private static Logger _logger = LogFactory.getLogger(MainPageAdapter.class);
    public static final int ObservationIndex = 0;
    public static final int HourlyForecastIndex = 1;
    public static final int ForecastIndex = 2;
    public static final int DiscussionIndex = 3;
    public static final int RadarIndex = 4;
    public static final int SatelliteIndex = 5;
    private String[] _pageTitles = new String[6];
    private Fragment[] _fragments = new Fragment[6];

    public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        _pageTitles[ObservationIndex] = fragmentActivity.getBaseContext().getResources().getString(R.string.observation_page_title);
        _pageTitles[HourlyForecastIndex] = fragmentActivity.getBaseContext().getResources().getString(R.string.hourlyforecast_page_title);
        _pageTitles[ForecastIndex] = fragmentActivity.getBaseContext().getResources().getString(R.string.forecast_page_title);
        _pageTitles[DiscussionIndex] = fragmentActivity.getBaseContext().getResources().getString(R.string.discussion_page_title);
        _pageTitles[RadarIndex] = fragmentActivity.getBaseContext().getResources().getString(R.string.radar_page_title);
        _pageTitles[SatelliteIndex] = fragmentActivity.getBaseContext().getResources().getString(R.string.satellite_page_title);
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

    public CharSequence getPageTitle(int position) {
        return _pageTitles[position];
    }

    public Fragment getItem(int position) {
        if (_fragments[position] == null) {
            return createFragment(position);
        }
        return _fragments[position];
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
                case RadarIndex:
                    _fragments[position] = RadarFragment.newInstance();
                    break;
                case SatelliteIndex:
                    _fragments[position] = SatelliteFragment.newInstance();
                    break;
            }
        }
        return _fragments[position];
    }

    @Override
    public int getItemCount() {
        return _fragments.length;
    }
}
