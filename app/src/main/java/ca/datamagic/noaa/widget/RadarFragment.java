package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.RadarBackgroundTask;
import ca.datamagic.noaa.async.RadarImageTask;
import ca.datamagic.noaa.async.RadarTimeStampsTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.current.CurrentLocation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.RadarTimeDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(RadarFragment.class);
    private int _tile = -1;
    private ImageButton _playPauseButton = null;
    private TextView _radarTime = null;
    private SimpleDateFormat _radarTimeFormat = null;
    private LinearLayout _moveTileLayout = null;
    private LinearLayout _leftLayout = null;
    private ImageButton _leftButton = null;
    private LinearLayout _upLayout = null;
    private ImageButton _upButton = null;
    private LinearLayout _downLayout = null;
    private ImageButton _downButton = null;
    private LinearLayout _rightLayout = null;
    private ImageButton _rightButton = null;
    private RadarView _radarView = null;
    private List<RadarTimeDTO> _timeStamps = null;
    private Timer _timer = null;
    private Hashtable<String, Bitmap> _radarBitmaps = null;
    private int _currentIndex = 0;

    public static RadarFragment newInstance() {
        RadarFragment fragment = new RadarFragment();
        return fragment;
    }

    private void updateMoveTileButtons() {
        switch (_tile) {
            case 2:
            {
                _leftLayout.setVisibility(View.GONE);
                _upLayout.setVisibility(View.GONE);
                _downLayout.setVisibility(View.VISIBLE);
                _rightLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 3:
            {
                _leftLayout.setVisibility(View.VISIBLE);
                _upLayout.setVisibility(View.GONE);
                _downLayout.setVisibility(View.VISIBLE);
                _rightLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 4:
            {
                _leftLayout.setVisibility(View.VISIBLE);
                _upLayout.setVisibility(View.GONE);
                _downLayout.setVisibility(View.VISIBLE);
                _rightLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 5:
            {
                _leftLayout.setVisibility(View.VISIBLE);
                _upLayout.setVisibility(View.GONE);
                _downLayout.setVisibility(View.VISIBLE);
                _rightLayout.setVisibility(View.GONE);
                break;
            }
            case 8:
            {
                _leftLayout.setVisibility(View.GONE);
                _upLayout.setVisibility(View.VISIBLE);
                _downLayout.setVisibility(View.GONE);
                _rightLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 9:
            {
                _leftLayout.setVisibility(View.VISIBLE);
                _upLayout.setVisibility(View.VISIBLE);
                _downLayout.setVisibility(View.GONE);
                _rightLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 10:
            {
                _leftLayout.setVisibility(View.VISIBLE);
                _upLayout.setVisibility(View.VISIBLE);
                _downLayout.setVisibility(View.GONE);
                _rightLayout.setVisibility(View.VISIBLE);
                break;
            }
            case 11:
            {
                _leftLayout.setVisibility(View.VISIBLE);
                _upLayout.setVisibility(View.VISIBLE);
                _downLayout.setVisibility(View.GONE);
                _rightLayout.setVisibility(View.GONE);
                break;
            }
            default:
            {
                _moveTileLayout.setVisibility(View.GONE);
                break;
            }
        }
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
                _playPauseButton = view.findViewById(R.id.playPauseButton);
                _radarTime = view.findViewById(R.id.radarTime);
                _radarView = view.findViewById(R.id.radarView);
                _playPauseButton.setVisibility(View.GONE);
                _playPauseButton.setOnClickListener(new PlayPauseButtonListener());
                _moveTileLayout = view.findViewById(R.id.moveTileLayout);
                _leftLayout = view.findViewById(R.id.leftLayout);
                _leftButton = view.findViewById(R.id.leftButton);
                _leftButton.setOnClickListener(new MoveTileButtonListener());
                _upLayout = view.findViewById(R.id.upLayout);
                _upButton = view.findViewById(R.id.upButton);
                _upButton.setOnClickListener(new MoveTileButtonListener());
                _downLayout = view.findViewById(R.id.downLayout);
                _downButton = view.findViewById(R.id.downButton);
                _downButton.setOnClickListener(new MoveTileButtonListener());
                _rightLayout = view.findViewById(R.id.rightLayout);
                _rightButton = view.findViewById(R.id.rightButton);
                _rightButton.setOnClickListener(new MoveTileButtonListener());
                _radarTime.setText(null);
                LinearLayout radarLayout = view.findViewById(R.id.radarLayout);
                TextView radarViewNotAvailable = view.findViewById(R.id.radarViewNotAvailable);
                TextView radarViewNotAvailableForThisLocation = view.findViewById(R.id.radarViewNotAvailableForThisLocation);
                PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
                PreferencesDTO preferencesDTO = preferencesDAO.read();
                _radarTimeFormat = new SimpleDateFormat(preferencesDTO.getDateFormat() + " " + preferencesDTO.getTimeFormat());
                if ((preferencesDTO.isTextOnly() != null) && preferencesDTO.isTextOnly().booleanValue()) {
                    radarViewNotAvailable.setVisibility(View.VISIBLE);
                    radarLayout.setVisibility(View.GONE);
                    return;
                } else {
                    radarViewNotAvailable.setVisibility(View.GONE);
                    radarLayout.setVisibility(View.VISIBLE);
                }
                RadarDAO dao = new RadarDAO();
                _tile = dao.getRadarTile(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
                _logger.info("tile: " + _tile);
                this.updateMoveTileButtons();
                if (_tile < 1) {
                    radarViewNotAvailableForThisLocation.setVisibility(View.VISIBLE);
                    radarLayout.setVisibility(View.GONE);
                    return;
                } else {
                    radarViewNotAvailableForThisLocation.setVisibility(View.GONE);
                    radarLayout.setVisibility(View.VISIBLE);
                }
                RadarBackgroundTask task1 = new RadarBackgroundTask(_tile);
                task1.addListener(new AsyncTaskListener<Bitmap>() {
                    @Override
                    public void completed(AsyncTaskResult<Bitmap> result) {
                        try {
                            MainActivity.getThisInstance().stopBusy();
                            Bitmap bitmap = result.getResult();
                            if (bitmap != null) {
                                double width = bitmap.getWidth();
                                double height = bitmap.getHeight();
                                double aspectRatio = width / height;
                                _radarView.setAspectRatio(aspectRatio);
                                _radarView.setBackgroundBitmap(bitmap);
                            } else {
                                _radarView.setBackgroundBitmap(null);
                            }
                            _radarView.invalidate();
                        } catch (Throwable t) {
                            _logger.warning("Radar background task complete error: " + t.getMessage());
                        }
                    }
                });

                RadarTimeStampsTask task2 = new RadarTimeStampsTask();
                task2.addListener(new AsyncTaskListener<List<RadarTimeDTO>>() {
                    @Override
                    public void completed(AsyncTaskResult<List<RadarTimeDTO>> result) {
                        try {
                            _timeStamps = result.getResult();
                            _logger.info("timeStamps: " + _timeStamps);
                            if ((_timeStamps != null) && (_timeStamps.size() > 0)) {
                                _timer = new Timer();
                                _timer.scheduleAtFixedRate(new RadarTimerTask(), 500, 3000);
                                _radarBitmaps = new Hashtable<String, Bitmap>();
                                _moveTileLayout.setVisibility(View.VISIBLE);
                            }
                        } catch (Throwable t) {
                            _logger.warning("Radar timestamps task complete error: " + t.getMessage());
                        }
                    }
                });
                task1.execute((Void[])null);
                task2.execute((Void[])null);
                MainActivity.getThisInstance().startBusy();
            }
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute((Void[])null);
        }
    }

    @Override
    public void cleanup() {
        try {
            if (_timer != null) {
                _timer.cancel();
            }
        } catch (Throwable t) {
            _logger.warning("Radar cancel timer error: " + t.getMessage());
        }
        try {
            View view = getView();
            if (view != null) {
                RadarView radarView = (RadarView)view;
                radarView.setBackgroundBitmap(null);
                radarView.setRadarBitmap(null);
                radarView.resetScale();
            }
        } catch (Throwable t) {
            _logger.warning("Radar view reset scale error: " + t.getMessage());
        }
        _radarView = null;
        _timer = null;
    }

    private class PlayPauseButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                if (_playPauseButton != null) {
                    String tag = (String) _playPauseButton.getTag();
                    _logger.info("tag: " + tag);
                    if (tag != null) {
                        if (tag.compareToIgnoreCase("pause") == 0) {
                            _playPauseButton.setImageResource(R.drawable.play);
                            _playPauseButton.setTag("play");
                            if (_timer != null) {
                                _timer.cancel();
                            }
                            _timer = null;
                        } else if (tag.compareToIgnoreCase("play") == 0) {
                            _playPauseButton.setImageResource(R.drawable.pause);
                            _playPauseButton.setTag("pause");
                            _timer = new Timer();
                            _timer.scheduleAtFixedRate(new RadarTimerTask(), 500, 3000);
                        }
                    }
                }
            } catch (Throwable t) {
                if ((t != null) && (t.getMessage() != null)) {
                    _logger.warning(t.getMessage());
                }
                _logger.warning("Unexpected Exception in PlayPauseButtonListener.onClick.");
            }
        }
    }

    private class MoveTileButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                String tag = (String) view.getTag();
                _logger.info("tag: " + tag);
                if (tag != null) {
                    int newTile = _tile;
                    if (tag.compareToIgnoreCase("left") == 0) {
                        switch (_tile) {
                            case 2: newTile = 2; break;
                            case 3: newTile = 2; break;
                            case 4: newTile = 3; break;
                            case 5: newTile = 4; break;
                            case 8: newTile = 8; break;
                            case 9: newTile = 8; break;
                            case 10: newTile = 9; break;
                            case 11: newTile = 10; break;
                        }
                    } else if (tag.compareToIgnoreCase("up") == 0) {
                        switch (_tile) {
                            case 2: newTile = 2; break;
                            case 3: newTile = 3; break;
                            case 4: newTile = 4; break;
                            case 5: newTile = 5; break;
                            case 8: newTile = 2; break;
                            case 9: newTile = 3; break;
                            case 10: newTile = 4; break;
                            case 11: newTile = 5; break;
                        }
                    } else if (tag.compareToIgnoreCase("down") == 0) {
                        switch (_tile) {
                            case 2: newTile = 8; break;
                            case 3: newTile = 9; break;
                            case 4: newTile = 10; break;
                            case 5: newTile = 11; break;
                            case 8: newTile = 8; break;
                            case 9: newTile = 9; break;
                            case 10: newTile = 10; break;
                            case 11: newTile = 11; break;
                        }
                    } else if (tag.compareToIgnoreCase("right") == 0) {
                        switch (_tile) {
                            case 2: newTile = 3; break;
                            case 3: newTile = 4; break;
                            case 4: newTile = 5; break;
                            case 5: newTile = 5; break;
                            case 8: newTile = 9; break;
                            case 9: newTile = 10; break;
                            case 10: newTile = 11; break;
                            case 11: newTile = 11; break;
                        }
                    }
                    if (newTile != _tile) {
                        _tile = newTile;
                        if (_timer != null) {
                            _timer.cancel();
                        }
                        _timer = null;
                        _currentIndex = 0;
                        _radarBitmaps.clear();
                        _logger.info("tile: " + _tile);
                        updateMoveTileButtons();
                        RadarBackgroundTask task = new RadarBackgroundTask(_tile);
                        task.addListener(new AsyncTaskListener<Bitmap>() {
                            @Override
                            public void completed(AsyncTaskResult<Bitmap> result) {
                                try {
                                    MainActivity.getThisInstance().stopBusy();
                                    Bitmap bitmap = result.getResult();
                                    if (bitmap != null) {
                                        double width = bitmap.getWidth();
                                        double height = bitmap.getHeight();
                                        double aspectRatio = width / height;
                                        _radarView.setAspectRatio(aspectRatio);
                                        _radarView.setBackgroundBitmap(bitmap);
                                        _radarView.setRadarBitmap(null);
                                    } else {
                                        _radarView.setBackgroundBitmap(null);
                                        _radarView.setRadarBitmap(null);
                                    }
                                    _radarView.invalidate();
                                    _playPauseButton.setImageResource(R.drawable.pause);
                                    _playPauseButton.setTag("pause");
                                    _timer = new Timer();
                                    _timer.scheduleAtFixedRate(new RadarTimerTask(), 500, 3000);
                                } catch (Throwable t) {
                                    _logger.warning("Radar background task complete error: " + t.getMessage());
                                }
                            }
                        });
                        task.execute((Void[])null);
                        MainActivity.getThisInstance().startBusy();
                    }
                }
            } catch (Throwable t) {
                if ((t != null) && (t.getMessage() != null)) {
                    _logger.warning(t.getMessage());
                }
                _logger.warning("Unexpected Exception in MoveTileButtonListener.onClick.");
            }
        }
    }

    private class RadarTimerTask extends TimerTask {
        @Override
        public void run() {
            // Get the radar image for the timestamp
            final RadarTimeDTO radarTime = _timeStamps.get(_currentIndex);
            final String timeStamp = radarTime.getTimeStamp();
            final Date time = radarTime.getTime();
            _logger.info("timeStamp: " + timeStamp);
            if (_radarBitmaps.containsKey(timeStamp)) {
                _logger.info("found bitmap.");
                MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            _radarTime.setText(_radarTimeFormat.format(time));
                            _radarView.setRadarBitmap(_radarBitmaps.get(timeStamp));
                            _radarView.invalidate();
                        } catch (Throwable t) {
                            _logger.warning("Set radar bitmap on UI thread error: " + t.getMessage());
                        }
                    }
                });
                return;
            }
            RadarImageTask task = new RadarImageTask(timeStamp, _tile);
            task.addListener(new AsyncTaskListener<Bitmap>() {
                @Override
                public void completed(AsyncTaskResult<Bitmap> result) {
                    if (result.getResult() != null) {
                        _logger.info("downloaded bitmap.");
                        final Bitmap bitmap = result.getResult();
                        MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    _radarBitmaps.put(timeStamp, bitmap);
                                    _radarTime.setText(_radarTimeFormat.format(time));
                                    _radarView.setRadarBitmap(_radarBitmaps.get(timeStamp));
                                    _radarView.invalidate();
                                } catch (Throwable t) {
                                    _logger.warning("Radar bitmap set on UI thread error: " + t.getMessage());
                                }
                            }
                        });
                    }
                }
            });
            task.execute((Void[])null);
            if ((++_currentIndex) == _timeStamps.size()) {
                _currentIndex = 0;
            }
            if (_playPauseButton != null) {
                if (_playPauseButton.getVisibility() == View.GONE) {
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
        }
    }
}
