package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.RadarBitmapsTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.BitmapsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.StringListDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.NumberUtils;

public class RadarFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(RadarFragment.class);
    private boolean _downloading = false;
    private BitmapsDTO _backgroundBitmaps = null;
    private BitmapsDTO _radarBitmaps = null;
    private Timer _radarTimer = null;
    private RadarTimerTask _radarTimerTask = null;
    private SimpleDateFormat _dateFormat = null;

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
            LinearLayout radarLayout = (LinearLayout)view.findViewById(R.id.radarLayout);
            TextView radarTime = (TextView)view.findViewById(R.id.radarTime);
            ImageButton playPauseButton = (ImageButton)getView().findViewById(R.id.playPauseButton);
            RadarView radarView = (RadarView)view.findViewById(R.id.radarView);
            TextView radarViewNotAvailable = (TextView)view.findViewById(R.id.radarViewNotAvailable);
            PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
            PreferencesDTO preferencesDTO = preferencesDAO.read();
            String dateFormat = preferencesDTO.getDateFormat();
            String timeFormat = preferencesDTO.getTimeFormat();
            if ((dateFormat != null) && (dateFormat.length() > 0) && (timeFormat != null) && (timeFormat.length() > 0)) {
                _dateFormat = new SimpleDateFormat(dateFormat + " " + timeFormat);
            }
            if ((preferencesDTO.isTextOnly() != null) && preferencesDTO.isTextOnly().booleanValue()) {
                radarViewNotAvailable.setVisibility(View.VISIBLE);
                radarLayout.setVisibility(View.GONE);
                return;
            } else {
                radarViewNotAvailable.setVisibility(View.GONE);
                radarLayout.setVisibility(View.VISIBLE);
            }

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
                initializeRadarTimer(radarTime, playPauseButton, radarView, radarBitmaps);
            }
        }
        (new AccountingTask("Radar", "Render")).execute((Void[])null);
    }

    @Override
    public void cleanup() {
        if (_radarTimer != null) {
            try {
                _radarTimer.cancel();
            } catch (Throwable t) {
                _logger.warning("Cancel radar timer error: " + t.getMessage());
            }
            _radarTimer = null;
        }
        if (_radarTimerTask != null) {
            try {
                _radarTimerTask.cancel();
            } catch (Throwable t) {
                _logger.warning("Cancel radar timer task error: " + t.getMessage());
            }
            _radarTimerTask = null;
        }
        try {
            View view = getView();
            if (view != null) {
                RadarView radarView = (RadarView) view.findViewById(R.id.radarView);
                radarView.resetScale();
            }
        } catch (Throwable t) {
            _logger.warning("Radar view reset scale error: " + t.getMessage());
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
                        TextView radarTime = getView().findViewById(R.id.radarTime);
                        ImageButton playPauseButton = getView().findViewById(R.id.playPauseButton);
                        RadarView radarView = getView().findViewById(R.id.radarView);
                        initializeRadarTimer(radarTime, playPauseButton, radarView, radarBitmaps);
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
                        TextView radarTime = getView().findViewById(R.id.radarTime);
                        ImageButton playPauseButton = getView().findViewById(R.id.playPauseButton);
                        RadarView radarView = getView().findViewById(R.id.radarView);
                        initializeRadarTimer(radarTime, playPauseButton, radarView, result.getResult());
                    } catch (Throwable t) {
                        _logger.warning("Exception: " + result.getThrowable().getMessage());
                    }
                }
            }
        });
        task.execute((Void)null);
    }

    private void initializeRadarTimer(TextView radarTime, ImageButton playPauseButton, RadarView radarView, BitmapsDTO radarBitmaps) {
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
            _radarTimerTask = new RadarTimerTask(radarTime, playPauseButton, radarView, getRadarBitmaps(), radarBitmaps.size() - 1);
            _radarTimer.scheduleAtFixedRate(_radarTimerTask, 1000, 1000);
        }
    }

    private void initializeRadarTimer(TextView radarTime, ImageButton playPauseButton, RadarView radarView, BitmapsDTO radarBitmaps, int index) {
        if ((radarBitmaps != null) && (radarBitmaps.size() > 0)) {
            _radarTimer = new Timer();
            _radarTimerTask = new RadarTimerTask(radarTime, playPauseButton, radarView, getRadarBitmaps(), index);
            _radarTimer.scheduleAtFixedRate(_radarTimerTask, 1000, 1000);
        }
    }

    private class RadarTimerTask extends TimerTask implements View.OnClickListener {
        private TextView _radarTime = null;
        private RadarView _radarView = null;
        private BitmapsDTO _radarBitmaps = null;
        private ImageButton _playPauseButton = null;
        private int _index = 0;

        public RadarTimerTask(TextView radarTime, ImageButton playPauseButton, RadarView radarView, BitmapsDTO radarBitmaps, int index) {
            _radarTime = radarTime;
            _playPauseButton = playPauseButton;
            _radarView = radarView;
            _radarBitmaps = radarBitmaps;
            _index = index;
            _playPauseButton.setOnClickListener(this);
        }

        @Override
        public void run() {
            if (_index < 0) {
                _index = _radarBitmaps.size() - 1;
            }
            Bitmap radarBitmap = _radarBitmaps.get(_index);
            StringListDTO radarImages = getRadarImages();
            if ((_dateFormat != null) && (radarImages != null) && (_index < radarImages.size()) && (_radarTime != null)) {
                String radarImage = getRadarImages().get(_index);
                _logger.info("radarImage: " + radarImage);
                int lastSlash = radarImage.lastIndexOf('/');
                if (lastSlash > -1) {
                    String radarFile = radarImage.substring(lastSlash + 1);
                    _logger.info("radarFile: " + radarFile);
                    String[] radarFileParts = radarFile.split("_");
                    if (radarFileParts.length > 2) {
                        String date = radarFileParts[1];
                        _logger.info("date: " + date);
                        Integer year = null;
                        Integer month = null;
                        Integer day = null;
                        if (date.length() > 7) {
                            year = NumberUtils.toInteger(date.substring(0, 4));
                            month = NumberUtils.toInteger(date.substring(4, 6));
                            day = NumberUtils.toInteger(date.substring(6));
                        }
                        String time = radarFileParts[2];
                        _logger.info("time: " + time);
                        Integer hour = null;
                        Integer minute = null;
                        if (time.length() > 3) {
                            hour = NumberUtils.toInteger(time.substring(0, 2));
                            minute = NumberUtils.toInteger(time.substring(2));
                        }
                        if ((year != null) && (month != null) && (day != null) && (hour != null) && (minute != null)) {
                            final Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month.intValue() - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, day);
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    _radarTime.setText(_dateFormat.format(calendar.getTime()));
                                }
                            });
                        }
                    }
                }
            }
            if (_playPauseButton.getVisibility() == View.INVISIBLE) {
                MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _playPauseButton.setVisibility(View.VISIBLE);
                    }
                });
            }
            try {
                _radarView.setRadarBitmap(radarBitmap);
                _radarView.invalidate();
            } catch (Throwable t) {
                // TODO
            }
            --_index;
        }

        @Override
        public void onClick(View v) {
            if (_playPauseButton != null) {
                String tag = (String) _playPauseButton.getTag();
                _logger.info("tag: " + tag);
                if (tag != null) {
                    if (tag.compareToIgnoreCase("pause") == 0) {
                        _playPauseButton.setImageResource(R.drawable.play);
                        _playPauseButton.setTag("play");
                        if (_radarTimer != null) {
                            _radarTimer.cancel();
                        }
                        _radarTimer = null;
                        _radarTimerTask = null;
                    } else if (tag.compareToIgnoreCase("play") == 0) {
                        _playPauseButton.setImageResource(R.drawable.pause);
                        _playPauseButton.setTag("pause");
                        initializeRadarTimer(_radarTime, _playPauseButton, _radarView, _radarBitmaps, _index);
                    }
                }
            }
        }
    }
}
