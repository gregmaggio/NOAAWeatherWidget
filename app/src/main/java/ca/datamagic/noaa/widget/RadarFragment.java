package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.RadarBitmapsTask;
import ca.datamagic.noaa.dto.BitmapsDTO;
import ca.datamagic.noaa.dto.StringListDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(RadarFragment.class);
    private boolean _downloading = false;
    private BitmapsDTO _backgroundBitmaps = null;
    private BitmapsDTO _radarBitmaps = null;
    private Timer _radarTimer = null;
    private RadarTimerTask _radarTimerTask = null;

    public static RadarFragment newInstance(StringListDTO backgroundImages, StringListDTO radarImages) {
        RadarFragment fragment = new RadarFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("backgroundImages", backgroundImages);
        bundle.putParcelable("radarImages", radarImages);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.radar_main;
    }

    public StringListDTO getBackgroundImages() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getParcelable("backgroundImages");
        }
        return null;
    }

    public void setBackgroundImages(StringListDTO newVal) {
        boolean backgroundImagesSet = false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            StringListDTO curVal = arguments.getParcelable("backgroundImages");
            if ((curVal != null) && (newVal != null)) {
                if (curVal != newVal) {
                    arguments.putParcelable("backgroundImages", newVal);
                    backgroundImagesSet = true;
                }
            } else if ((curVal == null) && (newVal != null)) {
                arguments.putParcelable("backgroundImages", newVal);
                backgroundImagesSet = true;
            } else if ((curVal != null) && (newVal == null)) {
                arguments.putParcelable("backgroundImages", newVal);
                backgroundImagesSet = true;
            }
        }
        if (backgroundImagesSet) {
            setBackgroundBitmaps(null);
        }
    }

    public StringListDTO getRadarImages() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getParcelable("radarImages");
        }
        return null;
    }

    public void setRadarImages(StringListDTO newVal) {
        boolean radarImagesSet = false;
        Bundle arguments = getArguments();
        if (arguments != null) {
            StringListDTO curVal = arguments.getParcelable("radarImages");
            if ((curVal != null) && (newVal != null)) {
                if (curVal != newVal) {
                    arguments.putParcelable("radarImages", newVal);
                    radarImagesSet = true;
                }
            } else if ((curVal == null) && (newVal != null)) {
                arguments.putParcelable("radarImages", newVal);
                radarImagesSet = true;
            } else if ((curVal != null) && (newVal == null)) {
                arguments.putParcelable("radarImages", newVal);
                radarImagesSet = true;
            }
        }
        if (radarImagesSet) {
            setRadarBitmaps(null);
        }
    }

    public BitmapsDTO getBackgroundBitmaps() {
        return _backgroundBitmaps;
    }

    public void setBackgroundBitmaps(BitmapsDTO newVal) {
        _backgroundBitmaps = newVal;
    }

    public BitmapsDTO getRadarBitmaps() {
        return _radarBitmaps;
    }

    public void setRadarBitmaps(BitmapsDTO newVal) {
        _radarBitmaps = newVal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radar_main, container, false);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        _logger.info("onSaveInstanceState");
        super.onSaveInstanceState(outState);
        cleanup();
    }

    @Override
    public void render() {
        View view = getView();
        if (view != null) {
            RadarView radarView = (RadarView)view.findViewById(R.id.radarView);
            StringListDTO backgroundImages = getBackgroundImages();
            BitmapsDTO backgroundBitmaps = getBackgroundBitmaps();
            if ((backgroundImages != null) && (backgroundBitmaps == null)) {
                initializeBackgroundBitmaps(backgroundImages);
                return;
            } else {
                radarView.setBackgroundBitmaps(backgroundBitmaps);
                radarView.invalidate();
            }

            StringListDTO radarImages = getRadarImages();
            BitmapsDTO radarBitmaps = getRadarBitmaps();
            if ((radarImages != null) && (radarBitmaps == null)) {
                initializeRadarBitmaps(radarImages);
            } else if ((radarBitmaps != null) && (radarBitmaps.size() > 0)) {
                initializeRadarTimer(radarView, radarBitmaps);
            }
        }
    }

    @Override
    public void cleanup() {
        if (_radarTimer != null) {
            _radarTimer.cancel();
            _radarTimer = null;
        }
        if (_radarTimerTask != null) {
            _radarTimerTask.cancel();
            _radarTimerTask = null;
        }
    }

    private void initializeBackgroundBitmaps(StringListDTO backgroundImages) {
        if (_downloading) {
            return;
        }
        _downloading = true;
        MainActivity.getThisInstance().startBusy();
        RadarBitmapsTask task = new RadarBitmapsTask(backgroundImages);
        task.addListener(new AsyncTaskListener<BitmapsDTO>() {
            @Override
            public void completed(AsyncTaskResult<BitmapsDTO> result) {
                _downloading = false;
                MainActivity.getThisInstance().stopBusy();
                if (result.getThrowable() != null) {
                    _logger.warning("Exception: " + result.getThrowable().getMessage());
                } else {
                    setBackgroundBitmaps(result.getResult());
                    try {
                        RadarView radarView = getView().findViewById(R.id.radarView);
                        radarView.setBackgroundBitmaps(getBackgroundBitmaps());
                    } catch (Throwable t) {
                        _logger.warning("Exception: " + result.getThrowable().getMessage());
                    }
                    StringListDTO radarImages = getRadarImages();
                    BitmapsDTO radarBitmaps = getRadarBitmaps();
                    if ((radarImages != null) && (radarBitmaps == null)) {
                        initializeRadarBitmaps(radarImages);
                    } else if ((radarBitmaps != null) && (radarBitmaps.size() > 0)) {
                        RadarView radarView = getView().findViewById(R.id.radarView);
                        initializeRadarTimer(radarView, radarBitmaps);
                    }
                }
            }
        });
        task.execute((Void)null);
    }

    private void initializeRadarBitmaps(StringListDTO radarImages) {
        if (_downloading) {
            return;
        }
        _downloading = true;
        MainActivity.getThisInstance().startBusy();
        RadarBitmapsTask task = new RadarBitmapsTask(radarImages);
        task.addListener(new AsyncTaskListener<BitmapsDTO>() {
            @Override
            public void completed(AsyncTaskResult<BitmapsDTO> result) {
                _downloading = false;
                MainActivity.getThisInstance().stopBusy();
                if (result.getThrowable() != null) {
                    _logger.warning("Exception: " + result.getThrowable().getMessage());
                } else {
                    setRadarBitmaps(result.getResult());
                    try {
                        RadarView radarView = getView().findViewById(R.id.radarView);
                        initializeRadarTimer(radarView, result.getResult());
                    } catch (Throwable t) {
                        _logger.warning("Exception: " + result.getThrowable().getMessage());
                    }
                }
            }
        });
        task.execute((Void)null);
    }

    private void initializeRadarTimer(RadarView radarView, BitmapsDTO radarBitmaps) {
        if (_radarTimer != null) {
            _radarTimer.cancel();
            _radarTimer = null;
        }
        if (_radarTimerTask != null) {
            _radarTimerTask.cancel();
            _radarTimerTask = null;
        }
        if ((radarBitmaps != null) && (radarBitmaps.size() > 0)) {
            _radarTimer = new Timer();
            _radarTimerTask = new RadarTimerTask(radarView, getRadarBitmaps());
            _radarTimer.scheduleAtFixedRate(_radarTimerTask, 1000, 1000);
        }
    }

    private class RadarTimerTask extends TimerTask {
        private RadarView _radarView = null;
        private BitmapsDTO _radarBitmaps = null;
        private int _index = 0;

        public RadarTimerTask(RadarView radarView, BitmapsDTO radarBitmaps) {
            _radarView = radarView;
            _radarBitmaps = radarBitmaps;
            _index = _radarBitmaps.size() - 1;
        }

        @Override
        public void run() {
            if (_index < 0) {
                _index = _radarBitmaps.size() - 1;
            }
            Bitmap radarBitmap = _radarBitmaps.get(_index);
            try {
                _radarView.setRadarBitmap(radarBitmap);
                _radarView.invalidate();
            } catch (Throwable t) {
                // TODO
            }
            --_index;
        }
    }
}
