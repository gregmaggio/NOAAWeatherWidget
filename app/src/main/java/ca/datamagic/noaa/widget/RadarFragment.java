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
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.RadarBitmapsTask;
import ca.datamagic.noaa.async.RadarTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.async.Workflow;
import ca.datamagic.noaa.async.WorkflowStep;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.BitmapsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.dto.StringListDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.NumberUtils;

public class RadarFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(RadarFragment.class);
    private RadarBitmapsTask _backgroundBitmapsTask = null;
    private RadarBitmapsTask _radarBitmapsTask = null;
    private BitmapsDTO _backgroundBitmaps = null;
    private StringListDTO _radarImages = null;
    private BitmapsDTO _radarBitmaps = null;
    private Timer _radarTimer = null;
    private RadarTimerTask _radarTimerTask = null;
    private SimpleDateFormat _dateFormat = null;
    private TextView _radarTime = null;
    private ImageButton _playPauseButton = null;
    private RadarView _radarView = null;

    public static RadarFragment newInstance() {
        RadarFragment fragment = new RadarFragment();
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.radar_main;
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
        try {
            if (!MainActivity.getThisInstance().isFragmentActive(this)) {
                return;
            }
            View view = getView();
            if (view != null) {
                LinearLayout radarLayout = (LinearLayout) view.findViewById(R.id.radarLayout);
                _radarTime = (TextView) view.findViewById(R.id.radarTime);
                _playPauseButton = (ImageButton) getView().findViewById(R.id.playPauseButton);
                _radarView = (RadarView) view.findViewById(R.id.radarView);
                _playPauseButton.setVisibility(View.INVISIBLE);
                _radarTime.setText(null);

                TextView radarViewNotAvailable = (TextView) view.findViewById(R.id.radarViewNotAvailable);
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

                if ((_backgroundBitmaps == null) || (_radarBitmaps == null)) {
                    RadarTask radarTask = new RadarTask();
                    RadarListener radarListener = new RadarListener();
                    _backgroundBitmapsTask = new RadarBitmapsTask(true);
                    _radarBitmapsTask = new RadarBitmapsTask(false);
                    BackgroundBitmapsListener backgroundBitmapsListener = new BackgroundBitmapsListener();
                    RadarBitmapsListener radarBitmapsListener = new RadarBitmapsListener();
                    Workflow radarWorkflow = new Workflow();
                    radarWorkflow.addStep(new WorkflowStep(radarTask, radarListener));
                    radarWorkflow.addStep(new WorkflowStep(_backgroundBitmapsTask, backgroundBitmapsListener));
                    radarWorkflow.addStep(new WorkflowStep(_radarBitmapsTask, radarBitmapsListener));
                    radarWorkflow.addListener(new Workflow.WorkflowListener() {
                        @Override
                        public void completed(boolean success) {
                            MainActivity.getThisInstance().stopBusy();
                            _radarView.setBackgroundBitmaps(_backgroundBitmaps);
                            _radarView.invalidate();
                            initializeRadarTimer(_radarTime, _playPauseButton, _radarView, _radarBitmaps);
                        }
                    });
                    MainActivity.getThisInstance().startBusy();
                    radarWorkflow.start();
                    return;
                }

                _radarView.setBackgroundBitmaps(_backgroundBitmaps);
                _radarView.invalidate();
                initializeRadarTimer(_radarTime, _playPauseButton, _radarView, _radarBitmaps);
            }
            (new AccountingTask("Radar", "Render")).execute((Void[]) null);
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute((Void[])null);
        }
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

        try {
            if (_playPauseButton != null) {
                _playPauseButton.setVisibility(View.INVISIBLE);
            }
        } catch (Throwable t) {
            _logger.warning("play pause button set visiblity: " + t.getMessage());
        }

        try {
            if (_radarTime != null) {
                _radarTime.setText("");
            }
        } catch (Throwable t) {
            _logger.warning("radar time set text: " + t.getMessage());
        }

        _backgroundBitmapsTask = null;
        _radarBitmapsTask = null;
        _backgroundBitmaps = null;
        _radarImages = null;
        _radarBitmaps = null;
        _radarTimer = null;
        _radarTimerTask = null;
        _dateFormat = null;
        _radarTime = null;
        _playPauseButton = null;
        _radarView = null;
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
            playPauseButton.setVisibility(View.INVISIBLE);
            initializeRadarTimer(radarTime, playPauseButton, radarView, _radarBitmaps, radarBitmaps.size() - 1);
        } else {
            playPauseButton.setVisibility(View.GONE);
            radarTime.setText("No radar images found for this location!");
        }
    }

    private void initializeRadarTimer(TextView radarTime, ImageButton playPauseButton, RadarView radarView, BitmapsDTO radarBitmaps, int index) {
        if ((radarBitmaps != null) && (radarBitmaps.size() > 0)) {
            String tag = (String) playPauseButton.getTag();
            _logger.info("tag: " + tag);
            if (tag != null) {
                if (tag.compareToIgnoreCase("pause") == 0) {
                    _radarTimer = new Timer();
                    _radarTimerTask = new RadarTimerTask(radarTime, playPauseButton, radarView, radarBitmaps, index);
                    _radarTimer.scheduleAtFixedRate(_radarTimerTask, 1000, 1000);
                }
            }
        }
    }

    private class RadarListener implements AsyncTaskListener<RadarDTO> {
        @Override
        public void completed(AsyncTaskResult<RadarDTO> result) {
            if (result.getResult() != null) {
                _backgroundBitmapsTask.setRadarImages(result.getResult().getBackgroundImages());
                _radarImages = result.getResult().getRadarImages();
                _radarBitmapsTask.setRadarImages(result.getResult().getRadarImages());
            }
        }
    }

    private class BackgroundBitmapsListener implements AsyncTaskListener<BitmapsDTO> {
        @Override
        public void completed(AsyncTaskResult<BitmapsDTO> result) {
            if (result.getResult() != null) {
                _backgroundBitmaps = result.getResult();
            }
        }
    }

    private class RadarBitmapsListener implements AsyncTaskListener<BitmapsDTO> {
        @Override
        public void completed(AsyncTaskResult<BitmapsDTO> result) {
            if (result.getResult() != null) {
                _radarBitmaps = result.getResult();
            }
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
            if (_radarBitmaps == null) {
                return;
            }
            if (_index < 0) {
                _index = _radarBitmaps.size() - 1;
            }
            Bitmap radarBitmap = _radarBitmaps.get(_index);
            if ((_dateFormat != null) && (_radarImages != null) && (_index < _radarImages.size()) && (_radarTime != null)) {
                String radarImage = _radarImages.get(_index);
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
            if (_playPauseButton != null) {
                if (_playPauseButton.getVisibility() == View.INVISIBLE) {
                    MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (_playPauseButton != null) {
                                _playPauseButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
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
