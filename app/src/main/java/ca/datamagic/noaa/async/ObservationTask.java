package ca.datamagic.noaa.async;

import android.util.Log;

import ca.datamagic.noaa.dao.ObservationDAO;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 1/9/2016.
 */
public class ObservationTask extends AsyncTaskBase<Void, Void, DWMLDTO> {
    private static final String _tag = "ObservationTask";
    private static final int _maxTries = 5;
    private ObservationDAO _dao = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;
    private String _unit = "e";

    public ObservationTask(double latitude, double longitude, String unit) {
        _dao = new ObservationDAO();
        _latitude = latitude;
        _longitude = longitude;
        _unit = unit;
    }

    private DWMLDTO load() {
        int tries = 0;
        while (tries < _maxTries) {
            try {
                return _dao.GetCurrentObservation(_latitude, _longitude, _unit);
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
