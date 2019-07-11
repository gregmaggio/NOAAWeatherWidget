package ca.datamagic.noaa.widget;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.DWMLTask;
import ca.datamagic.noaa.async.DiscussionTask;
import ca.datamagic.noaa.async.GooglePlaceTask;
import ca.datamagic.noaa.async.GooglePredictionsTask;
import ca.datamagic.noaa.async.HourlyForecastTask;
import ca.datamagic.noaa.async.RadarTask;
import ca.datamagic.noaa.async.TimeZoneTask;
import ca.datamagic.noaa.async.StationTask;
import ca.datamagic.noaa.async.Workflow;
import ca.datamagic.noaa.async.WorkflowStep;
import ca.datamagic.noaa.dao.ForecastsDAO;
import ca.datamagic.noaa.dao.GooglePlacesDAO;
import ca.datamagic.noaa.dao.ImageDAO;
import ca.datamagic.noaa.dao.ObservationDAO;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.dto.PredictionDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, SearchView.OnCloseListener, StationsAdapter.StationsAdapterListener {
    private Logger _logger = null;
    private static  MainActivity _thisInstance;
    private String _filesPath = null;
    private Double _deviceLatitude = null;
    private Double _deviceLongitude = null;
    private double _latitude = 38.9967;
    private double _longitude = -76.9275;
    private double _savedlatitude = _latitude;
    private double _savedLongitude = _longitude;
    private int _year = Calendar.getInstance().get(Calendar.YEAR);
    private int _month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int _numDays = 7;
    private String _unit = "e";
    private String _format = "24 hourly";
    private DWMLTask _dwmlTask = null;
    private HourlyForecastTask _hourlyForecastTask = null;
    private TimeZoneTask _timeZoneTask = null;
    private StationTask _stationTask = null;
    private RadarTask _radarTask = null;
    private DiscussionTask _discussionTask = null;
    private DWMLListener _dwmlListener = new DWMLListener();
    private HourlyForecastListener _hourlyForecastListener = new HourlyForecastListener();
    private TimeZoneListener _timeZoneListener = new TimeZoneListener();
    private StationListener _stationListener = new StationListener();
    private RadarListener _radarListener = new RadarListener();
    private DiscussionListener _discussionListener = new DiscussionListener();
    private DWMLDTO _dwml = null;
    private FeatureDTO _hourlyForecastFeature = null;
    private ObservationDTO _obervation = null;
    private ForecastsDTO _forecasts = null;
    private String _timeZoneId = null;
    private StationDTO _station = null;
    private RadarDTO _radar = null;
    private String _discussion = null;
    private StationsHelper _stationsHelper = null;
    private SharedPreferences _preferences = null;
    private DrawerLayout _drawerLayout = null;
    private ActionBarDrawerToggle _drawerToggle = null;
    private LinearLayout _header = null;
    private MainPageAdapter _mainPageAdapter = null;
    private NonSwipeableViewPager _viewPager = null;
    private GoogleApiClient _googleApiClient = null;
    private StationsAdapter _stationsAdapter = null;
    private SearchManager _manager = null;
    private SearchView _search = null;
    private GooglePredictionsTask _googlePredictionsTask = null;
    private GooglePlaceTask _googlePlaceTask = null;
    private Menu _mainMenu = null;
    private boolean _processing = false;
    private ProgressBar _spinner = null;
    private int _currentPage = 0;

    public MainActivity() {
        _thisInstance = this;
    }

    public static MainActivity getThisInstance() {
        return _thisInstance;
    }

    public String getFilesPath() {
        return _filesPath;
    }

    public Double getDeviceLatitude() {
        return _deviceLatitude;
    }

    public Double getDeviceLongitude() {
        return _deviceLongitude;
    }

    public MainPageAdapter getMainPageAdapter() {
        return _mainPageAdapter;
    }

    public NonSwipeableViewPager getViewPager() {
        return _viewPager;
    }

    public DWMLDTO getDWML() {
        return _dwml;
    }

    public FeatureDTO getHourlyForecastFeature() {
        return _hourlyForecastFeature;
    }

    public ObservationDTO getObervation() {
        return _obervation;
    }

    public ForecastsDTO getForecasts() {
        return _forecasts;
    }

    public String getTimeZoneId() {
        return _timeZoneId;
    }

    public StationDTO getStation() {
        return _station;
    }

    public RadarDTO getRadar() {
        return _radar;
    }

    public String getDiscussion() {
        return _discussion;
    }

    public void readPreferences() {
        PreferencesDAO dao = new PreferencesDAO(getBaseContext());
        PreferencesDTO dto = dao.read();
        _latitude = dto.getLatitude();
        _longitude = dto.getLongitude();
        _numDays = dto.getNumDays();
        _unit = dto.getUnit();
        _format = dto.getFormat();
    }

    public void writePreferences() {
        PreferencesDAO dao = new PreferencesDAO(getBaseContext());
        PreferencesDTO dto = dao.read();
        dto.setLatitude(_latitude);
        dto.setLongitude(_longitude);
        dto.setNumDays(_numDays);
        dto.setUnit(_unit);
        dto.setFormat(_format);
        dao.write(dto);
    }

    private void readCurrentState() {
        readPreferences();
        _stationsHelper = new StationsHelper(getBaseContext());
        LocationsHelper locationsHelper = new LocationsHelper(getBaseContext());
        OldStationsHelper oldStationsHelper = new OldStationsHelper(getBaseContext());

        HashMap<String, StationDTO> stations = new HashMap<String, StationDTO>();
        List<StationDTO> stations1 = _stationsHelper.readStations();
        List<StationDTO> stations2 = locationsHelper.readStations();
        List<StationDTO> stations3 = oldStationsHelper.readStations();
        if (stations1 != null) {
            for (int ii = 0; ii < stations1.size(); ii++) {
                StationDTO station = stations1.get(ii);
                String key = station.getStationName().toLowerCase();
                if (!stations.containsKey(key)) {
                    stations.put(key, station);
                }
            }
        }
        if (stations2 != null) {
            for (int ii = 0; ii < stations2.size(); ii++) {
                StationDTO station = stations2.get(ii);
                String key = station.getStationName().toLowerCase();
                if (!stations.containsKey(key)) {
                    stations.put(key, station);
                }
            }
        }
        if (stations3 != null) {
            for (int ii = 0; ii < stations3.size(); ii++) {
                StationDTO station = stations3.get(ii);
                String key = station.getStationName().toLowerCase();
                if (!stations.containsKey(key)) {
                    stations.put(key, station);
                }
            }
        }
        List<StationDTO> stationsList = new ArrayList<StationDTO>(stations.values());
        _stationsAdapter = new StationsAdapter(this, stationsList);
        _stationsAdapter.addListener(this);
    }

    private void writeCurrentState() {
        writePreferences();
        if ((_stationsHelper != null) && (_stationsAdapter != null)) {
            _stationsHelper.writeStations(_stationsAdapter.getStations());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GooglePlacesDAO.setApiKey(getResources().getString(R.string.google_maps_api_key));

        initializeLogging();

        _spinner = (ProgressBar)findViewById(R.id.progressBar);

        readCurrentState();

        ListView leftDrawer = (ListView)findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(_stationsAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        _drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        _drawerToggle.setDrawerIndicatorEnabled(true);
        _drawerLayout.addDrawerListener(_drawerToggle);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        } else {
            initializeGoogleApiClient();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, PackageManager.PERMISSION_GRANTED);
        }

        _preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        _header = (LinearLayout)findViewById(R.id.header);
        _mainPageAdapter = new MainPageAdapter(getSupportFragmentManager(), getBaseContext());

        for (int ii = 0; ii < _mainPageAdapter.getCount(); ii++) {
            String html = MessageFormat.format("<strong>{0}</strong>", _mainPageAdapter.getPageTitle(ii));
            TextView textView = new TextView(getBaseContext());
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(Html.fromHtml(html));
            textView.setPadding(10, 10, 10, 10);
            textView.setTag(ii);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _viewPager.setCurrentItem((int)view.getTag());
                }
            });
            _header.addView(textView);
        }

        _viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        _viewPager.setAdapter(_mainPageAdapter);
        _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                _logger.info("onPageSelected: " + position);
                _logger.info("currentPage: " + _currentPage);
                refreshView();
                updateHeader();
                _currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        _logger.info("SCROLL_STATE_DRAGGING");
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        _logger.info("SCROLL_STATE_IDLE");
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        _logger.info("SCROLL_STATE_SETTLING");
                        break;
                    default:
                        _logger.info("Unexpected: " + state);
                        break;
                }
            }
        });
        readPreferences();
        myLocation();
        updateHeader();
    }

    private void updateHeader() {
        for (int ii = 0; ii < _mainPageAdapter.getCount(); ii++) {
            int color = (_viewPager.getCurrentItem() == ii) ? Color.YELLOW : Color.WHITE;
            ((TextView)_header.getChildAt(ii)).setTextColor(color);
        }
    }

    private void initializeGoogleApiClient() {
        _googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        _googleApiClient.connect();
    }

    private void initializeLogging() {
        try {
            File intPath = getFilesDir();
            _filesPath = intPath.getAbsolutePath();
            LogFactory.initialize(Level.WARNING, _filesPath, true);
            ImageDAO.setFilesPath(_filesPath);
            _logger = LogFactory.getLogger(MainActivity.class);
        } catch (Throwable t) {
            // Do Nothing
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int ii = 0; ii < permissions.length; ii++) {
            if (permissions[ii].compareToIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION) == 0) {
                initializeGoogleApiClient();
            } else if (permissions[ii].compareToIgnoreCase(Manifest.permission.WRITE_SETTINGS) == 0) {

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (_googleApiClient != null) {
            if (_googleApiClient.isConnected()) {
                _googleApiClient.disconnect();
            }
        }
        writeCurrentState();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (_stationsHelper != null) {
            _stationsHelper.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        _mainMenu = menu;
        _manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        _search = (SearchView) menu.findItem(R.id.search).getActionView();
        _search.setSearchableInfo(_manager.getSearchableInfo(getComponentName()));
        _search.setIconified(true);
        _search.setIconifiedByDefault(true);
        _search.setFocusable(false);
        _search.setOnQueryTextListener(this);
        _search.setOnSuggestionListener(this);
        _search.setOnCloseListener(this);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.my_location:
                myLocation();
                return true;
            case R.id.action_refresh:
                actionRefresh();
                return true;
            case R.id.action_settings:
                actionSettings();
                return true;
            case R.id.action_senderror:
                actionSendError();
                return true;
            case R.id.action_exit:
                (new AccountingTask("Application", "Exit")).execute((Void[])null);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void myLocation() {
        try {
            if (_googleApiClient != null) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(_googleApiClient);
                if (lastLocation != null) {
                    _savedlatitude = _latitude;
                    _savedLongitude = _longitude;
                    _latitude = lastLocation.getLatitude();
                    _longitude = lastLocation.getLongitude();
                } else {
                    // TODO
                }
            }
        } catch (SecurityException ex) {
            // TODO: Show Error
            if (_logger != null) {
                _logger.log(Level.WARNING, "Security Exception in myLocation.", ex);
            }
        } catch (Throwable t) {
            // TODO: Show Error
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in myLocation.", t);
            }
        }
        actionRefresh();
        (new AccountingTask("MyLocation", "Select")).execute((Void[])null);
    }

    public void actionRefresh() {
        if (_processing) {
            return;
        }
        try {
            resetView();

            PreferencesDAO preferencesDAO = new PreferencesDAO(getBaseContext());
            PreferencesDTO preferencesDTO = preferencesDAO.read();

            _processing = true;
            _spinner.setVisibility(View.VISIBLE);
            _year = Calendar.getInstance().get(Calendar.YEAR);
            _month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            _dwmlTask = new DWMLTask(_latitude, _longitude);
            _hourlyForecastTask = new HourlyForecastTask(_latitude, _longitude);
            _timeZoneTask = new TimeZoneTask(_latitude, _longitude);
            _stationTask = new StationTask(_latitude, _longitude);
            _radarTask = new RadarTask();
            _discussionTask = new DiscussionTask();
            _mainPageAdapter.setBackgroundImages(null);
            _mainPageAdapter.setRadarImages(null);

            Workflow refreshWorkflow = new Workflow();
            refreshWorkflow.addStep(new WorkflowStep(_dwmlTask, _dwmlListener));
            refreshWorkflow.addStep(new WorkflowStep(_hourlyForecastTask, _hourlyForecastListener));
            refreshWorkflow.addStep(new WorkflowStep(_timeZoneTask, _timeZoneListener));
            refreshWorkflow.addStep(new WorkflowStep(_stationTask, _stationListener));
            if ((preferencesDTO.isTextOnly() == null) || !preferencesDTO.isTextOnly().booleanValue()) {
                refreshWorkflow.addStep(new WorkflowStep(_radarTask, _radarListener));
            }
            refreshWorkflow.addStep(new WorkflowStep(_discussionTask, _discussionListener));
            refreshWorkflow.addListener(new Workflow.WorkflowListener() {
                @Override
                public void completed(boolean success) {
                    writeCurrentState();
                    _processing = false;
                    _spinner.setVisibility(View.GONE);
                    refreshView();
                }
            });
            refreshWorkflow.start();
            (new AccountingTask("Refresh", "Current")).execute((Void[])null);
        } catch (Throwable t) {
            // TODO: Show Error
            _processing = false;
            _spinner.setVisibility(View.GONE);
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in refresh.", t);
            }
        }
    }

    public void startBusy() {
        if (_processing) {
            return;
        }
        try {
            _processing = true;
            _spinner.setVisibility(View.VISIBLE);
        } catch (Throwable t) {
            // TODO: Show Error
            _processing = false;
            _spinner.setVisibility(View.GONE);
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in startBusy.", t);
            }
        }
    }

    public void stopBusy() {
        if (!_processing) {
            return;
        }
        try {
            _processing = false;
            _spinner.setVisibility(View.GONE);
        } catch (Throwable t) {
            // TODO: Show Error
            _processing = false;
            _spinner.setVisibility(View.GONE);
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in startBusy.", t);
            }
        }
    }

    private void resetView() {
        try {
            if (_viewPager.getCurrentItem() != 0) {
                _viewPager.setCurrentItem(0);
                _mainPageAdapter.refreshPage(getSupportFragmentManager(), _viewPager.getCurrentItem());
            }
        } catch (Throwable t) {
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in send error.", t);
            }
            showError("Some Android weirdness occurred when rendering the widget. Wait a second and try refresh or my location from the menu.");
        }
    }

    private void refreshView() {
        try {
            _mainPageAdapter.refreshPage(getSupportFragmentManager(), _viewPager.getCurrentItem());
        } catch (Throwable t) {
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in send error.", t);
            }
            showError("Some Android weirdness occurred when rendering the widget. Wait a second and try refresh or my location from the menu.");
        }
    }

    private void showError(String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ErrorDialogTheme);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message);
        builder.setTitle("Error");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actionSettings() {
        try {
            SettingsDialog dialog = new SettingsDialog(this);
            dialog.show();
        } catch (Throwable t) {
            // TODO: Show Error
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in settings.", t);
            }
        }
    }

    private void actionSendError() {
        try {
            SendErrorDialog dialog = new SendErrorDialog(this);
            dialog.show();
        } catch (Throwable t) {
            // TODO: Show Error
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in send error.", t);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            if (_googleApiClient != null) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(_googleApiClient);
                if (lastLocation != null) {
                    _deviceLatitude = lastLocation.getLatitude();
                    _deviceLongitude = lastLocation.getLongitude();
                }
            }
        } catch (SecurityException ex) {
            if (_logger != null) {
                _logger.log(Level.WARNING, "Security Exception in onConnected.", ex);
            }
        } catch (Throwable t) {
            if (_logger != null) {
                _logger.log(Level.WARNING, "Unknown Exception in onConnected.", t);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if ((newText == null) || (newText.length() < 1)) {
            return true;
        }
        if (_googlePredictionsTask != null) {
            return true;
        }
        _googlePredictionsTask = new GooglePredictionsTask(newText);
        _googlePredictionsTask.addListener(new AsyncTaskListener<List<PredictionDTO>>() {
            @Override
            public void completed(AsyncTaskResult<List<PredictionDTO>> result) {
                if (result.getThrowable() != null) {
                    if (_logger != null) {
                        _logger.log(Level.WARNING, "Error retrieving predictions for text.", result.getThrowable());
                    }
                } else {
                    List<PredictionDTO> predictions = result.getResult();
                    _logger.info("predictions: " + predictions.size());
                    String[] columns = new String[]{"_id", "placeId", "description"};
                    MatrixCursor cursor = new MatrixCursor(columns);
                    for (int ii = 0; ii < predictions.size(); ii++) {
                        PredictionDTO prediction = predictions.get(ii);
                        Object[] temp = new Object[]
                                {
                                        ii,
                                        prediction.getPlaceId(),
                                        prediction.getDescription()
                                };
                        cursor.addRow(temp);
                    }
                    _search.setSuggestionsAdapter(new PredictionsSearchAdapter(getBaseContext(), cursor));
                    (new AccountingTask("Search", "Query")).execute((Void[])null);
                }
                _googlePredictionsTask = null;
            }
        });
        _googlePredictionsTask.execute((Void)null);
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        if (_googlePlaceTask != null) {
            return true;
        }
        Object suggestion = _search.getSuggestionsAdapter().getItem(position);
        Cursor cursor = (Cursor)suggestion;
        int index = cursor.getColumnIndex("placeId");
        String placeId = cursor.getString(index);

        _googlePlaceTask = new GooglePlaceTask(placeId);
        _googlePlaceTask.addListener(new AsyncTaskListener<PlaceDTO>() {
            @Override
            public void completed(AsyncTaskResult<PlaceDTO> result) {
                if (result.getThrowable() != null) {
                    if (_logger != null) {
                        _logger.log(Level.WARNING, "Error retrieving place for suggestion.", result.getThrowable());
                    }
                } else {
                    _savedlatitude = _latitude;
                    _savedLongitude = _longitude;
                    _latitude = result.getResult().getLatitude();
                    _longitude = result.getResult().getLongitude();
                    _mainMenu.findItem(R.id.search).collapseActionView();
                    _mainMenu.close();
                    _search.setIconified(true);
                    _search.setQuery("", false);
                    _search.clearFocus();
                    _search.onActionViewCollapsed();
                    actionRefresh();
                    (new AccountingTask("Search", "Select")).execute((Void[])null);
                }
                _googlePlaceTask = null;
            }
        });
        _googlePlaceTask.execute((Void)null);
        return true;
    }

    @Override
    public void onStationAdded(StationDTO stationn) {
        _stationsHelper.writeStations(_stationsAdapter.getStations());
        _stationsAdapter.notifyDataSetChanged();
        (new AccountingTask("Station", "Added")).execute((Void[])null);
    }

    @Override
    public void onStationSelect(StationDTO station) {
        _drawerLayout.closeDrawer(Gravity.LEFT);
        _savedlatitude = _latitude;
        _savedLongitude = _longitude;
        _latitude = station.getLatitude();
        _longitude = station.getLongitude();
        actionRefresh();
        (new AccountingTask("Station", "Select")).execute((Void[])null);
    }

    @Override
    public void onStationRemove(StationDTO station) {
        _stationsAdapter.remove(station);
        _stationsAdapter.notifyDataSetInvalidated();
        _stationsHelper.writeStations(_stationsAdapter.getStations());
        _drawerLayout.closeDrawer(Gravity.LEFT);
        (new AccountingTask("Station", "Remove")).execute((Void[])null);
    }

    private class DWMLListener implements AsyncTaskListener<DWMLDTO> {
        @Override
        public void completed(AsyncTaskResult<DWMLDTO> result) {
            _dwml = null;
            _obervation = null;
            _forecasts = null;
            if (result.getThrowable() != null) {
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving DWML.", result.getThrowable());
                }
                // TODO: Reset to last good state
            } else {
                _dwml = result.getResult();
                if (_dwml != null) {
                    _obervation = ObservationDAO.getObservation(_dwml);
                    _forecasts = ForecastsDAO.getForecasts(_dwml);
                }
            }
        }
    }

    private class HourlyForecastListener implements AsyncTaskListener<FeatureDTO> {
        @Override
        public void completed(AsyncTaskResult<FeatureDTO> result) {
            _hourlyForecastFeature = null;
            if (result.getThrowable() != null) {
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving hourly forecast.", result.getThrowable());
                }
            } else {
                _hourlyForecastFeature = result.getResult();
            }
        }
    }

    private class TimeZoneListener implements AsyncTaskListener<String> {
        @Override
        public void completed(AsyncTaskResult<String> result) {
            if (result.getThrowable() != null) {
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving time zone.", result.getThrowable());
                }
            } else {
                _logger.info("timeZoneId: " + _timeZoneId);
                _timeZoneId = result.getResult();
            }
        }
    }

    private class StationListener implements AsyncTaskListener<StationDTO> {
        @Override
        public void completed(AsyncTaskResult<StationDTO> result) {
            if (result.getThrowable() != null) {
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving station.", result.getThrowable());
                }
                _discussionTask.setWFO(null);
                _radarTask.setRadar(null);
            } else {
                _station = result.getResult();
                if (_station != null) {
                    _stationsAdapter.add(_station);
                    _radarTask.setRadar(_station.getRadar());
                    _discussionTask.setWFO(_station.getWFO());
                } else {
                    _discussionTask.setWFO(null);
                    _radarTask.setRadar(null);
                }
            }
        }
    }

    private class RadarListener implements AsyncTaskListener<RadarDTO> {
        @Override
        public void completed(AsyncTaskResult<RadarDTO> result) {
            if (result.getThrowable() != null) {
                _mainPageAdapter.setBackgroundImages(null);
                _mainPageAdapter.setRadarImages(null);
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving radar images.", result.getThrowable());
                }
                _radar = null;
            } else {
                _radar = result.getResult();
                if (_radar != null) {
                    _mainPageAdapter.setBackgroundImages(_radar.getBackgroundImages());
                    _mainPageAdapter.setRadarImages(_radar.getRadarImages());
                } else {
                    _mainPageAdapter.setBackgroundImages(null);
                    _mainPageAdapter.setRadarImages(null);
                }
            }
        }
    }

    private class DiscussionListener implements AsyncTaskListener<String> {
        @Override
        public void completed(AsyncTaskResult<String> result) {
            if (result.getThrowable() != null) {
                _discussion = null;
                if (_logger != null) {
                    _logger.log(Level.WARNING, "Error retrieving discussion.", result.getThrowable());
                }
            } else {
                _discussion = result.getResult();
            }
        }
    }
}
