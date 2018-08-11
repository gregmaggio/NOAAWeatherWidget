package ca.datamagic.noaa.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.logging.Logger;

import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.StringListDTO;
import ca.datamagic.noaa.dto.SunriseSunsetDTO;
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
    public static final int RadarIndex = 4;
    private SunriseSunsetDTO _sunriseSunset = null;
    private ObservationDTO _observation = null;
    private ForecastsDTO _forecasts = null;
    private StringListDTO _backgroundImages = null;
    private StringListDTO _radarImages = null;
    private String _discussion = null;
    private String _skewTStation = null;
    private String[] _pageTitles = new String[5];
    private Fragment[] _fragments = new Fragment[5];

    public MainPageAdapter(FragmentManager manager, Context context) {
        super(manager);
        _pageTitles[ObservationIndex] = context.getResources().getString(R.string.observation_page_title);
        _pageTitles[ForecastIndex] = context.getResources().getString(R.string.forecast_page_title);
        _pageTitles[DiscussionIndex] = context.getResources().getString(R.string.discussion_page_title);
        _pageTitles[SkewTIndex] = context.getResources().getString(R.string.skewt_page_title);
        _pageTitles[RadarIndex] = context.getResources().getString(R.string.radar_page_title);
    }

    public SunriseSunsetDTO getSunriseSunset() {
        return _sunriseSunset;
    }

    public void setSunriseSunset(SunriseSunsetDTO newVal) {
        _sunriseSunset = newVal;
    }

    public ObservationDTO getObservation() {
        return _observation;
    }

    public void  setObservation(ObservationDTO newVal) {
        _observation = newVal;
    }

    public ForecastsDTO getForecasts() {
        return _forecasts;
    }

    public void  setForecasts(ForecastsDTO newVal) {
        _forecasts = newVal;
    }

    public StringListDTO getBackgroundImages() {
        return _backgroundImages;
    }

    public void setBackgroundImages(StringListDTO newVal) {
        _backgroundImages = newVal;
    }

    public StringListDTO getRadarImages() {
        return _radarImages;
    }

    public void setRadarImages(StringListDTO newVal) {
        _radarImages = newVal;
    }

    public String getDiscussion() {
        return _discussion;
    }

    public void setDiscussion(String newVal) {
        _discussion = newVal;
    }

    public String getSkewTStation() {
        return _skewTStation;
    }

    public void setSkewTStation(String newVal) {
        _skewTStation = newVal;
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
            case ForecastIndex: return R.layout.forecast_main;
            case DiscussionIndex: return R.layout.discussion_main;
            case SkewTIndex: return R.layout.skewt_main;
            case RadarIndex: return R.layout.radar_main;
        }
        return -1;
    }

    @Override
    public Fragment getItem(int position) {
        if (_fragments[position] == null) {
            switch (position) {
                case ObservationIndex:
                    _fragments[position] = ObservationFragment.newInstance(getObservation(), getSunriseSunset());
                    break;
                case ForecastIndex:
                    _fragments[position] = ForecastFragment.newInstance(getForecasts());
                    break;
                case DiscussionIndex:
                    _fragments[position] = DiscussionFragment.newInstance(getDiscussion());
                    break;
                case SkewTIndex:
                    _fragments[position] = SkewTFragment.newInstance(getSkewTStation());
                    break;
                case RadarIndex:
                    _fragments[position] = RadarFragment.newInstance(getBackgroundImages(), getRadarImages());
                    break;
            }
        } else {
            switch (position) {
                case ObservationIndex:
                    ((ObservationFragment)_fragments[position]).setObservation(getObservation());
                    ((ObservationFragment)_fragments[position]).setSunriseSunset(getSunriseSunset());
                    break;
                case ForecastIndex:
                    ((ForecastFragment)_fragments[position]).setForecasts(getForecasts());
                    break;
                case DiscussionIndex:
                    ((DiscussionFragment)_fragments[position]).setDiscussion(getDiscussion());
                    break;
                case SkewTIndex:
                    ((SkewTFragment)_fragments[position]).setSkewTStation(getSkewTStation());
                    break;
                case RadarIndex:
                    ((RadarFragment)_fragments[position]).setBackgroundImages(getBackgroundImages());
                    ((RadarFragment)_fragments[position]).setRadarImages(getRadarImages());
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
