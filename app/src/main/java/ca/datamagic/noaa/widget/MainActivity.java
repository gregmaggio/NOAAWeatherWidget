package ca.datamagic.noaa.widget;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import java.io.File;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.DWMLTask;
import ca.datamagic.noaa.async.DiscussionTask;
import ca.datamagic.noaa.async.RefreshTask;
import ca.datamagic.noaa.async.StationSearchTask;
import ca.datamagic.noaa.async.StationTask;
import ca.datamagic.noaa.async.WFOTask;
import ca.datamagic.noaa.async.Workflow;
import ca.datamagic.noaa.async.WorkflowStep;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.dto.WFODTO;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, SearchView.OnCloseListener, StationsAdapter.StationsAdapterListener {
    private static Logger _logger = LogManager.getLogger(MainActivity.class);
    private MainActivity _thisInstance = this;
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
    private WFODTO _wfo = null;
    private StationDTO _station = null;
    private List<StationDTO> _stations = null;
    private StationsHelper _stationsHelper = null;
    private StationDTO _selectedStation = null;
    private SharedPreferences _preferences = null;
    private DrawerLayout _drawerLayout = null;
    private ActionBarDrawerToggle _drawerToggle = null;
    private MainPageAdapter _mainPageAdapter = null;
    private ObservationFragment _observationFragment = null;
    private ForecastFragment _forecastFragment = null;
    private DiscussionFragment _discussionFragment = null;
    private SkewTFragment _skewTFragment = null;
    private ViewPager _viewPager = null;
    private GoogleApiClient _googleApiClient = null;
    private StationsAdapter _stationsAdapter = null;
    private SearchManager _manager = null;
    private SearchView _search = null;
    private Menu _mainMenu = null;
    private boolean _processing = false;

    private void readPreferences() {
        if (_preferences != null) {
            _latitude = _preferences.getFloat("latitude", 38.9967f);
            _longitude = _preferences.getFloat("longitude", -76.9275f);
            _numDays = (int) _preferences.getLong("numDays", 7);
            _unit = _preferences.getString("unit", "e");
            _format = _preferences.getString("format", "24 hourly");
        }
    }

    private void writePreferences() {
        if (_preferences != null) {
            _numDays = (int) _preferences.getLong("numDays", 7);
            _unit = _preferences.getString("unit", "e");
            _format = _preferences.getString("format", "24 hourly");
            SharedPreferences.Editor editor = _preferences.edit();
            editor.putFloat("latitude", (float)_latitude);
            editor.putFloat("longitude", (float)_longitude);
            editor.putLong("editor", _numDays);
            editor.putString("unit", _unit);
            editor.putString("format", _format);
            editor.commit();
        }
    }

    private void readCurrentState() {
        readPreferences();
        _stationsHelper = new StationsHelper(getBaseContext());
        _stationsAdapter = new StationsAdapter(this, _stationsHelper.readStations());
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        } else {
            initializeLogging();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, PackageManager.PERMISSION_GRANTED);
        }

        _preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        _mainPageAdapter = new MainPageAdapter(getSupportFragmentManager());
        _observationFragment = (ObservationFragment) _mainPageAdapter.getItem(MainPageAdapter.ObservationIndex);
        _forecastFragment = (ForecastFragment) _mainPageAdapter.getItem(MainPageAdapter.ForecastIndex);
        _discussionFragment = (DiscussionFragment) _mainPageAdapter.getItem(MainPageAdapter.DiscussionIndex);
        _skewTFragment = (SkewTFragment) _mainPageAdapter.getItem(MainPageAdapter.SkewTIndex);
        _viewPager = (ViewPager) findViewById(R.id.viewpager);
        _viewPager.setAdapter(_mainPageAdapter);

        readPreferences();
        actionRefresh();
        refreshStations();
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
            File extPath = getExternalFilesDir("NOAAWeatherWidget");
            String logPath = extPath.getAbsolutePath();
            String logFile = MessageFormat.format("{0}{1}NOAAWeatherWidget.txt", logPath, File.pathSeparator);

            // creates pattern layout
            EnhancedPatternLayout layout = new EnhancedPatternLayout();
            String conversionPattern = "%d [%t] %-5p %c - %m%n";
            layout.setConversionPattern(conversionPattern);

            // creates daily rolling file appender
            DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
            rollingAppender.setFile(logFile);
            rollingAppender.setDatePattern("'.'yyyy-MM-dd");
            rollingAppender.setLayout(layout);
            rollingAppender.activateOptions();
            rollingAppender.setAppend(true);
            rollingAppender.setImmediateFlush(true);

            // configures the root logger
            Logger rootLogger = Logger.getRootLogger();
            rootLogger.setLevel(Level.DEBUG);
            rootLogger.addAppender(rollingAppender);

            cleanOldLogs(logPath);
        } catch (Throwable t) {
            // Do Nothing
        }
    }

    private void cleanOldLogs(String logPath) {
        try {
            Calendar keepDate = Calendar.getInstance();
            keepDate.add(Calendar.DATE, -7);
            File logDirectory = new File(logPath);
            if (logDirectory.exists()) {
                File[] logFiles = logDirectory.listFiles();
                if (logFiles != null) {
                    for (int ii = 0; ii < logFiles.length; ii++) {
                        if (logFiles[ii].lastModified() < keepDate.getTimeInMillis()) {
                            logFiles[ii].delete();
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // Do Nothing
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int ii = 0; ii < permissions.length; ii++) {
            if (permissions[ii].compareToIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION) == 0) {
                initializeGoogleApiClient();
            } else if (permissions[ii].compareToIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                initializeLogging();
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

    private void loadStations(String query) {
        if (_stations != null) {
            String[] columns = new String[] { "_id", "text" };
            MatrixCursor cursor = new MatrixCursor(columns);
            for (int ii = 0; ii < _stations.size(); ii++) {
                StationDTO station = _stations.get(ii);
                String stationName = station.getStationName();
                if ((stationName != null) && (stationName.length() > 0)) {
                    if (stationName.toLowerCase().startsWith(query.toLowerCase())) {
                        Object[] temp = new Object[] { ii, stationName };
                        cursor.addRow(temp);
                        continue;
                    }
                }
                String city = station.getCity();
                if ((city != null) && (city.length() > 0)) {
                    if (city.toLowerCase().startsWith(query.toLowerCase())) {
                        Object[] temp = new Object[] { ii, stationName };
                        cursor.addRow(temp);
                        continue;
                    }
                }
                String zip = station.getZip();
                if ((zip != null) && (zip.length() > 0)) {
                    if (zip.toLowerCase().startsWith(query.toLowerCase())) {
                        Object[] temp = new Object[] { ii, stationName };
                        cursor.addRow(temp);
                        continue;
                    }
                }
            }
            _search.setSuggestionsAdapter(new StationsSearchAdapter(this, cursor, _stations));
        }
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
                // TODO
                return true;
            case R.id.action_exit:
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
                }
                actionRefresh();
            }
        } catch (SecurityException ex) {
            // TODO: Show Error
        } catch (Throwable t) {
            // TODO: Show Error
        }
    }

    public void actionRefresh() {
        if (_processing) {
            return;
        }
        try {
            _processing = true;
            _year = Calendar.getInstance().get(Calendar.YEAR);
            _month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            DWMLTask dwmlTask = new DWMLTask(_latitude, _longitude, _unit);
            AsyncTaskListener<DWMLDTO> dwmlListener = new AsyncTaskListener<DWMLDTO>() {
                @Override
                public void completed(AsyncTaskResult<DWMLDTO> result) {
                    if (result.getThrowable() != null) {
                        // TODO: Show Error
                    } else {
                        _observationFragment.render(result.getResult());
                        _forecastFragment.render(result.getResult());
                    }
                }
            };

            WFOTask wfoTask = new WFOTask(_latitude, _longitude);
            AsyncTaskListener<List<WFODTO>> wfoListener = new AsyncTaskListener<List<WFODTO>>() {
                @Override
                public void completed(AsyncTaskResult<List<WFODTO>> result) {
                    if (result.getThrowable() != null) {
                        // TODO: Show Error
                    } else {
                        if (result.getResult().size() > 0) {
                            _wfo = result.getResult().get(0);
                            refreshDiscussion();
                        }
                    }
                }
            };

            StationTask stationTask = new StationTask(_latitude, _longitude);
            AsyncTaskListener<StationDTO> stationListener = new AsyncTaskListener<StationDTO>() {
                @Override
                public void completed(AsyncTaskResult<StationDTO> result) {
                    if (result.getThrowable() != null) {
                        // TODO: Show Error
                    } else {
                        _station = result.getResult();
                        String skewTUrl = MessageFormat.format("http://weather.unisys.com/upper_air/skew/skew_{0}.gif", _station.getStationId());
                        _skewTFragment.render(skewTUrl);
                    }
                }
            };

            Workflow refreshWorkflow = new Workflow();
            refreshWorkflow.addStep(new WorkflowStep(dwmlTask, dwmlListener));
            refreshWorkflow.addStep(new WorkflowStep(wfoTask, wfoListener));
            refreshWorkflow.addStep(new WorkflowStep(stationTask, stationListener));
            refreshWorkflow.addListener(new Workflow.WorkflowListener() {
                @Override
                public void completed(boolean success) {
                    _processing = false;
                    if (!success) {
                        _latitude = _savedlatitude;
                        _longitude = _savedLongitude;
                        RefreshTask refreshTask = new RefreshTask(_thisInstance);
                        refreshTask.execute((Void)null);
                    } else {
                        writeCurrentState();
                    }
                }
            });
            refreshWorkflow.start();
        } catch (Throwable t) {
            // TODO: Show Error
            _processing = false;
        }
    }

    private void refreshDiscussion() {
        try {
            DiscussionTask task = new DiscussionTask(_wfo.getWFO());
            task.addListener(new AsyncTaskListener<String>() {
                @Override
                public void completed(AsyncTaskResult<String> result) {
                    if (result.getThrowable() != null) {
                        _discussionFragment.render("Discussion not available.");
                    } else {
                        _discussionFragment.render(result.getResult());
                    }
                }
            });
            task.execute((Void[]) null);
        } catch (Throwable t) {
            // TODO: Show Error
        }
    }

    private void refreshStations() {
        try {
            StationSearchTask task = new StationSearchTask(null, null);
            task.addListener(new AsyncTaskListener<List<StationDTO>>() {
                @Override
                public void completed(AsyncTaskResult<List<StationDTO>> result) {
                    if (result.getThrowable() != null) {
                        // TODO: Show error
                    } else {
                        _stations = result.getResult();
                    }
                }
            });
            task.execute((Void[]) null);
        } catch (Throwable t) {
            // TODO: Show Error
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

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
        loadStations(newText);
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Object suggestion = _search.getSuggestionsAdapter().getItem(position);
        Cursor cursor = (Cursor)suggestion;
        int index = cursor.getColumnIndex("_id");
        int id = cursor.getInt(index);
        _selectedStation = _stations.get(id);
        _savedlatitude = _latitude;
        _savedLongitude = _longitude;
        _latitude = _selectedStation.getLatitude();
        _longitude = _selectedStation.getLongitude();
        _mainMenu.findItem(R.id.search).collapseActionView();
        _mainMenu.close();
        _search.setIconified(true);
        _search.setQuery("", false);
        _search.clearFocus();
        _search.onActionViewCollapsed();
        _stationsAdapter.add(_selectedStation);
        actionRefresh();
        return true;
    }

    @Override
    public void onStationClick(StationDTO station) {
        _drawerLayout.closeDrawer(Gravity.LEFT);
        _selectedStation = station;
        _savedlatitude = _latitude;
        _savedLongitude = _longitude;
        _latitude = _selectedStation.getLatitude();
        _longitude = _selectedStation.getLongitude();
        actionRefresh();
    }
}
