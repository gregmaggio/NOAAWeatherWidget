package ca.datamagic.noaa.async;

import android.util.Log;

import ca.datamagic.noaa.dao.ForecastDAO;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 12/31/2015.
 */
public class ForecastTask extends AsyncTaskBase<Void, Void, DWMLDTO> {
    private static final String _tag = "ForecastTask";
    private static final int _maxTries = 5;
    private ForecastDAO _dao = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;
    private int _year = 0;
    private int _month = 0;
    private int _day = 0;
    private int _numDays = 0;
    private String _unit = null;
    private String _format = null;

    public  ForecastTask(double latitude, double longitude, int year, int month, int day, int numDays, String unit, String format) {
        _dao = new ForecastDAO();
        _latitude = latitude;
        _longitude = longitude;
        _year = year;
        _month = month;
        _day = day;
        _numDays = numDays;
        _unit = unit;
        _format = format;
    }

    private DWMLDTO load() {
        int tries = 0;
        while (tries < _maxTries) {
            try {
                return _dao.GetForecastByDay(_latitude, _longitude, _year, _month, _day, _numDays, _unit, _format);
            } catch (Throwable t) {
                Log.w(_tag, "Exception", t);
            }
            ++tries;
        }
        return null;
    }

    @Override
    protected AsyncTaskResult<DWMLDTO> doInBackground(Void... params) {
        Log.d(_tag, "Running Task");
        try {
            return new AsyncTaskResult<DWMLDTO>(load());
        } catch (Throwable t) {
            return new AsyncTaskResult<DWMLDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<DWMLDTO> result) {
        Log.d(_tag, "Completed Task");
        if (result.getThrowable() != null) {
            Log.e(_tag, "Exception", result.getThrowable());
        }
        FireCompleted(result);
    }
}
