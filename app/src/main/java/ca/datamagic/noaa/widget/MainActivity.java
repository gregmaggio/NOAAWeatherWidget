package ca.datamagic.noaa.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFormatter;
import ca.datamagic.noaa.logging.LogHandler;

public class MainActivity extends AppCompatActivity {
    private static final String _tag = "WeatherWidget";
    private static final Logger _logger = Logger.getLogger("MainActivity");
    private static final int _maxTries = 5;
    private double _latitude = 38.9967;
    private double _longitude = -76.9275;
    private int _year = Calendar.getInstance().get(Calendar.YEAR);
    private int _month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int _day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private int _numDays = 7;
    private String _unit = "e";
    private String _format = "24 hourly";
    private SharedPreferences _preferences = null;
    private MainPageAdapter _mainPageAdapter = null;
    private ObservationFragment _observationFragment = null;
    private ForecastFragment _forecastFragment = null;
    private DiscussionFragment _discussionFragment = null;
    private SkewTFragment _skewTFragment = null;
    private ViewPager _viewPager = null;
    private LocationManager _locationManager = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            String logPath = getBaseContext().getFilesDir().getAbsolutePath();
            System.out.println("logPath: " + logPath);
            String[] parts = logPath.split(File.pathSeparator);
            String path = "";
            for (int ii = 0; ii < parts.length; ii++) {
                path += "/" + parts[ii];
                (new File(path)).mkdir();
            }
            String logFile = MessageFormat.format("{0}/NOAAWeatherWidget.txt", logPath);
            LogHandler logHandler = new LogHandler(logFile, true);
            logHandler.setLevel(Level.ALL);
            logHandler.setFormatter(new LogFormatter());
            _logger.addHandler(logHandler);
        } catch (IOException ex) {
            // Do Nothing!
        }

        _preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        _mainPageAdapter = new MainPageAdapter(getSupportFragmentManager());
        _observationFragment = (ObservationFragment) _mainPageAdapter.getItem(MainPageAdapter.ObservationIndex);
        _forecastFragment = (ForecastFragment) _mainPageAdapter.getItem(MainPageAdapter.ForecastIndex);
        _discussionFragment = (DiscussionFragment) _mainPageAdapter.getItem(MainPageAdapter.DiscussionIndex);
        _skewTFragment = (SkewTFragment) _mainPageAdapter.getItem(MainPageAdapter.SkewTIndex);
        _viewPager = (ViewPager) findViewById(R.id.viewpager);
        _viewPager.setAdapter(_mainPageAdapter);
        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        readPreferences();
        actionRefresh();

        _logger.log(Level.INFO, "Created");
    }

    @Override
    protected void onStop() {
        Log.d(_tag, "Stop");
        writePreferences();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private static void executeAsyncTask(AsyncTask asyncTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        } else {
            asyncTask.execute((Void[]) null);
        }
    }

    private void myLocation() {
        try {
            String provider = _locationManager.getBestProvider(new Criteria(), false);
            Log.d(_tag, "provider: " + provider);

            Location location = _locationManager.getLastKnownLocation(provider);
            _latitude = location.getLatitude();
            _longitude = location.getLongitude();
            actionRefresh();
            writePreferences();
        } catch (SecurityException ex) {
            Log.e(_tag, "SecurityException", ex);
        } catch (Throwable t) {
            Log.e(_tag, "Exception", t);
        }
    }

    private void actionRefresh() {
        try {
            refreshObservation();
            refreshForecast();
            refreshWFO();
            refreshStation();
        } catch (Throwable t) {
            Log.e(_tag, "Exception", t);
        }
    }

    private void refreshObservation() {
        try {
            new ObservationRefresh(_latitude, _longitude, _unit, _observationFragment);
        } catch (Throwable t) {
            Log.e(_tag, "Exception", t);
        }
    }

    private void refreshForecast() {
        try {
            new ForecastRefresh(_latitude, _longitude, _year, _month, _day, _numDays, _unit, _format, _forecastFragment);
        } catch (Throwable t) {
            Log.e(_tag, "Exception", t);
        }
    }

    private void refreshWFO() {
        try {
            new WFORefresh(_latitude, _longitude, _discussionFragment);
        } catch (Throwable t) {
            Log.e(_tag, "Exception", t);
        }
    }

    private void refreshStation() {
        try {
            new StationRefresh(_latitude, _longitude, _skewTFragment);
        } catch (Throwable t) {
            Log.e(_tag, "Exception", t);
        }
    }
}
