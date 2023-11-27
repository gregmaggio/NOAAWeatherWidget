package ca.datamagic.noaa.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.RadarImageTask;
import ca.datamagic.noaa.async.RadarSiteTask;
import ca.datamagic.noaa.async.RadarUrlsTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.current.CurrentLocation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.RadarSiteDTO;
import ca.datamagic.noaa.dto.RadarSiteInfoDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarFragment extends Fragment implements Renderer, NonSwipeableFragment, OnMapReadyCallback {
    private static final Logger _logger = LogFactory.getLogger(RadarFragment.class);
    private static final Pattern _dateTimePattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})_(\\d{2})(\\d{2})(\\d{2})", Pattern.CASE_INSENSITIVE);
    private final RadarSiteListener _radarSiteListener = new RadarSiteListener();
    private final RadarUrlsListener _radRadarUrlsListener = new RadarUrlsListener();
    private int _radarTotalMinutes = 60;
    private int _radarDelayMilliseconds = 2000;
    private ImageButton _playPauseButton = null;
    private Spinner _radarTime = null;
    private SimpleDateFormat _radarTimeFormat = null;
    private GoogleMap _map = null;
    private boolean _mapLocationInitialized = false;
    private RadarSiteDTO _radarSite = null;
    private String[] _urls = null;
    private String[] _radarTimes = null;
    private RadarSiteInfoDTO _metaData = null;
    private LatLngBounds _bounds = null;
    private int _currentIndex = 0;
    private RadarTimerTask _timerTask = null;
    private Timer _timer = null;
    private GroundOverlay _radarOverlay = null;

    public static RadarFragment newInstance() {
        RadarFragment fragment = new RadarFragment();
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.radar_main;
    }

    @Override
    public void onPause() {
        super.onPause();
        _logger.info("onPause");
        if (_timer != null) {
            try {
                _timer.cancel();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_timer != null) {
            try {
                _timer.purge();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_timerTask != null) {
            try {
                _timerTask.cancel();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        _timer = null;
        _timerTask = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: Resume not working - go to radar, go to discussion, go back to radar
        _logger.info("onResume");
        if ((_urls != null) && (_radarTimes != null)) {
            _timerTask = new RadarTimerTask();
            _timer = new Timer("Timer");
            _timer.scheduleAtFixedRate(_timerTask, _radarDelayMilliseconds, _radarDelayMilliseconds);
        }
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
    }

    @Override
    public void render() {
        try {
            if (!MainActivity.getThisInstance().isFragmentActive(this)) {
                return;
            }
            View view = getView();
            if (view != null)  {
                render(view);
            } else {
                RenderTask renderTask = new RenderTask(this);
                renderTask.execute();
            }
            (new AccountingTask("Radar", "Render")).execute();
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute();
        }
    }

    @Override
    public void cleanup() {
        if (_timer != null) {
            try {
                _timer.cancel();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_timer != null) {
            try {
                _timer.purge();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_timerTask != null) {
            try {
                _timerTask.cancel();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_map != null) {
            try {
                _map.clear();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        _playPauseButton = null;
        _radarTime = null;
        _radarTimeFormat = null;
        _map = null;
        _mapLocationInitialized = false;
        _radarSite = null;
        _urls = null;
        _radarTimes = null;
        _metaData = null;
        _bounds = null;
        _currentIndex = 0;
        _timerTask = null;
        _timer = null;
        _radarOverlay = null;
    }

    @Override
    public boolean canSwipe(float x, float y) {

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            _map = googleMap;
            UiSettings settings = this._map.getUiSettings();
            settings.setZoomControlsEnabled(true);

            RadarSiteTask task = new RadarSiteTask(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
            task.addListener(_radarSiteListener);
            task.execute();
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
            MainActivity.getThisInstance().stopBusy();
            Snackbar.make(getView(), "Error initializing map", Snackbar.LENGTH_LONG).show();
        }
    }

    private void resetForNewLocation(RadarSiteDTO radar) {
        if (_timer != null) {
            try {
                _timer.cancel();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_timer != null) {
            try {
                _timer.purge();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        if (_timerTask != null) {
            try {
                _timerTask.cancel();
            } catch (Throwable t) {
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
        _timerTask = null;
        _timer = null;

        try {
            Thread.sleep(_radarDelayMilliseconds);
        } catch (InterruptedException e) {
            _logger.warning("InterruptedException: " + e.getMessage());
        }

        // Check to make sure we are not already initializing for this location
        // Map move sends events fast and furious
        if (_radarSite != null) {
            if (_radarSite.equals(radar)) {
                return;
            }
        }
        _logger.info("Going to reset for ICAO " + radar.getICAO());
        radarSiteLoaded(radar);
        MainActivity.getThisInstance().startBusy();
    }

    private void render(View view) {
        PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
        PreferencesDTO preferencesDTO = preferencesDAO.read();
        _radarTotalMinutes = preferencesDTO.getRadarTotalMinutes();
        _radarDelayMilliseconds = preferencesDTO.getRadarDelaySeconds() * 1000;
        _playPauseButton = view.findViewById(R.id.playPauseButton);
        _playPauseButton.setVisibility(View.GONE);
        _playPauseButton.setOnClickListener(new PlayPauseButtonListener());
        _radarTime = view.findViewById(R.id.radarTime);
        _radarTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _logger.info("onItemSelected");
                _logger.info("i: " + i);
                _logger.info("l: " + l);
                if (_urls == null) {
                    return;
                }
                if ((_urls.length - 1) < i) {
                    return;
                }
                if (isTimerRunning()) {
                    return;
                }
                _currentIndex = i;
                String imageUrl = _urls[_currentIndex];
                String radarTime = _radarTimes[_currentIndex];
                RadarImageTask task = new RadarImageTask(imageUrl);
                task.addListener(new AsyncTaskListener<Bitmap>() {
                    @Override
                    public void completed(AsyncTaskResult<Bitmap> result) {
                        Bitmap bitmap = result.getResult();
                        if (bitmap != null) {
                            renderRadarImage(imageUrl, radarTime, bitmap);
                        }
                    }
                });
                task.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                _logger.info("onNothingSelected");
            }
        });
        LinearLayout radarLayout = view.findViewById(R.id.radarLayout);
        TextView radarViewNotAvailable = view.findViewById(R.id.radarViewNotAvailable);
        TextView radarViewNotAvailableForThisLocation = view.findViewById(R.id.radarViewNotAvailableForThisLocation);
        _radarTimeFormat = new SimpleDateFormat(preferencesDTO.getDateFormat() + " " + preferencesDTO.getTimeFormat());
        if ((preferencesDTO.isTextOnly() != null) && preferencesDTO.isTextOnly().booleanValue()) {
            radarViewNotAvailable.setVisibility(View.VISIBLE);
            radarLayout.setVisibility(View.GONE);
            return;
        } else {
            radarViewNotAvailable.setVisibility(View.GONE);
            radarLayout.setVisibility(View.VISIBLE);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            MainActivity.getThisInstance().startBusy();
        }
    }

    private void radarSiteLoaded(RadarSiteDTO radar) {
        _radarSite = radar;
        _urls = null;
        _radarTimes = null;
        _metaData = null;
        _bounds = null;
        _currentIndex = 0;
        if (_radarOverlay != null) {
            _radarOverlay.remove();
            _radarOverlay = null;
        }
        if (_radarSite == null) {
            MainActivity.getThisInstance().stopBusy();
            Snackbar.make(getView(), "Cannot find a radar for current location", Snackbar.LENGTH_LONG).show();
            return;
        }
        _metaData = radar.getSiteInfo();
        double[] upperCorner = _metaData.getUpperCorner();
        double[] lowerCorner = _metaData.getLowerCorner();
        _bounds = new LatLngBounds(new LatLng(lowerCorner[1], lowerCorner[0]), new LatLng(upperCorner[1], upperCorner[0]));

        if (!_mapLocationInitialized) {
            _map.moveCamera(CameraUpdateFactory.newLatLngBounds(_bounds, 50));
            _map.setOnCameraMoveListener(new CameraMoveListener());
            _mapLocationInitialized = true;
        }

        RadarUrlsTask task = new RadarUrlsTask(_radarSite.getICAO());
        task.addListener(_radRadarUrlsListener);
        task.execute();
    }

    private void radarUrlsLoaded(String[] urls) {
        _urls = urls;
        if ((_urls == null) || (_urls.length < 1)) {
            MainActivity.getThisInstance().stopBusy();
            Snackbar.make(getView(), "Cannot find radar images for current location", Snackbar.LENGTH_LONG).show();
            return;
        }
        _radarTimes = new String[_urls.length];
        int startIndex = -1;
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int ii = 0; ii < _urls.length; ii++) {
            _radarTimes[ii] = "";
            Matcher dateTimeMatcher = _dateTimePattern.matcher(_urls[ii]);
            if (dateTimeMatcher.find()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                calendar.set(Calendar.YEAR, Integer.parseInt(dateTimeMatcher.group(1)));
                calendar.set(Calendar.MONTH, Integer.parseInt(dateTimeMatcher.group(2)) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateTimeMatcher.group(3)));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateTimeMatcher.group(4)));
                calendar.set(Calendar.MINUTE, Integer.parseInt(dateTimeMatcher.group(5)));
                calendar.set(Calendar.SECOND, Integer.parseInt(dateTimeMatcher.group(6)));
                _radarTimes[ii] = _radarTimeFormat.format(calendar.getTime());
                int minutes = (int)((now.getTimeInMillis() - calendar.getTimeInMillis()) / 1000 / 60);
                if ((minutes < _radarTotalMinutes) && (startIndex == -1)) {
                    startIndex = ii;
                }
            }
        }
        if (startIndex == -1) {
            startIndex = _urls.length - 15 - 1;
            if (startIndex < 0) {
                startIndex = 0;
            }
        }
        int endIndex = _urls.length;
        _urls = Arrays.copyOfRange(_urls, startIndex, endIndex);
        _radarTimes = Arrays.copyOfRange(_radarTimes, startIndex, endIndex);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.radar_time, _radarTimes);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.radar_time);
        _radarTime.setAdapter(spinnerArrayAdapter);

        _currentIndex = 0;
        _timerTask = new RadarTimerTask();
        _timer = new Timer("Timer");
        _timer.scheduleAtFixedRate(_timerTask, _radarDelayMilliseconds, _radarDelayMilliseconds);
        _playPauseButton.setVisibility(View.VISIBLE);
        MainActivity.getThisInstance().stopBusy();
    }

    private void renderRadarImage(String imageUrl, String radarTime, Bitmap bitmap) {
        try {
            if (_radarOverlay == null) {
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                _radarOverlay = _map.addGroundOverlay(new GroundOverlayOptions()
                        .image(descriptor)
                        .positionFromBounds(_bounds));
            } else {
                _radarOverlay.setImage(BitmapDescriptorFactory.fromBitmap(bitmap));
            }
            Object selectedItem = _radarTime.getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.toString().compareToIgnoreCase(radarTime) == 0) {
                    return;
                }
            }
            _radarTime.setSelection(_currentIndex, true);
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        }
    }

    private boolean isTimerRunning() {
        try {
            if (_playPauseButton != null) {
                String tag = (String) _playPauseButton.getTag();
                _logger.info("tag: " + tag);
                if (tag != null) {
                    if (tag.compareToIgnoreCase("pause") == 0) {
                        return true;
                    } else if (tag.compareToIgnoreCase("play") == 0) {
                        return false;
                    }
                }
            }
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        }
        return false;
    }

    private void stopTimer() {
        try {
            _playPauseButton.setImageResource(R.drawable.play);
            _playPauseButton.setTag("play");
            if (_timer != null) {
                _timer.cancel();
            }
            if (_timer != null) {
                _timer.purge();
            }
            if (_timerTask != null) {
                _timerTask.cancel();
            }
            _timer = null;
            _timerTask = null;
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        }
    }

    private void startTimer() {
        try {
            _playPauseButton.setImageResource(R.drawable.pause);
            _playPauseButton.setTag("pause");
            _timer = new Timer();
            _timerTask = new RadarTimerTask();
            _timer.scheduleAtFixedRate(_timerTask, _radarDelayMilliseconds, _radarDelayMilliseconds);
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        }
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
                            stopTimer();
                        } else if (tag.compareToIgnoreCase("play") == 0) {
                            startTimer();
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

    private class RadarTimerTask extends TimerTask {
        private String _imageUrl = null;
        private String _radarTime = null;
        private RadarImageTask _task = null;

        @Override
        public void run() {
            if ((_urls == null) || (_radarTimes == null)) {
                return;
            }
            if (_task != null) {
                return;
            }
            _logger.info("currentIndex: " + _currentIndex);
            if ((_currentIndex < 0) || (_currentIndex > (_urls.length - 1))) {
                return;
            }
            _imageUrl = _urls[_currentIndex];
            _radarTime = _radarTimes[_currentIndex];
            _task = new RadarImageTask(_imageUrl);
            _task.addListener(new AsyncTaskListener<Bitmap>() {
                @Override
                public void completed(AsyncTaskResult<Bitmap> result) {
                    Bitmap bitmap = result.getResult();
                    if (bitmap != null) {
                        renderRadarImage(_imageUrl, _radarTime, bitmap);
                        if (_currentIndex < (_urls.length - 1)) {
                            ++_currentIndex;
                        } else {
                            _currentIndex = 0;
                        }
                    }
                    _task = null;
                }
            });
            _task.execute();
        }
    }

    private class CameraMoveListener implements GoogleMap.OnCameraMoveListener, AsyncTaskListener<RadarSiteDTO> {
        private boolean _loading = false;

        @Override
        public void onCameraMove() {
            //_logger.info("onCameraMove");
            if (_loading) {
                return;
            }
            if (_map != null) {
                CameraPosition cameraPosition = _map.getCameraPosition();
                if (cameraPosition != null) {
                    if (cameraPosition.target != null) {
                        this.loadRadar(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    }
                }
            }
        }

        private void loadRadar(double latitude, double longitude) {
            RadarSiteTask task = new RadarSiteTask(latitude, longitude);
            task.addListener(this);
            task.execute();
            _loading = true;
        }

        @Override
        public void completed(AsyncTaskResult<RadarSiteDTO> result) {
            _loading = false;
            if ((result != null) && (result.getResult() != null) && (_radarSite != null)) {
                String currICAO = _radarSite.getICAO();
                String newICAO = result.getResult().getICAO();
                if (currICAO.compareToIgnoreCase(newICAO) != 0) {
                    _logger.info("Moved from " + _radarSite.getICAO() + " to " + result.getResult().getICAO());
                    // Stop everything and re-initialize with the new ICAO
                    resetForNewLocation(result.getResult());
                }
            }
        }
    }

    private class RadarSiteListener implements AsyncTaskListener<RadarSiteDTO> {
        @Override
        public void completed(AsyncTaskResult<RadarSiteDTO> result) {
            RadarSiteDTO radarSite = result.getResult();
            if (radarSite != null) {
                if (_radarSite != null) {
                    String currICAO = _radarSite.getICAO();
                    String newICAO = radarSite.getICAO();
                    if (currICAO.compareToIgnoreCase(newICAO) != 0) {
                        _logger.info("Moved from " + _radarSite.getICAO() + " to " + radarSite.getICAO());
                    } else {
                        return;
                    }
                }
                resetForNewLocation(radarSite);
            }
        }
    }

    private class RadarUrlsListener implements  AsyncTaskListener<String[]> {
        @Override
        public void completed(AsyncTaskResult<String[]> result) {
            String[] urls = result.getResult();
            if (urls != null) {
                radarUrlsLoaded(urls);
            }
        }
    }
}
