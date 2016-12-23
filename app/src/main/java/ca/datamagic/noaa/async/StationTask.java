package ca.datamagic.noaa.async;

import android.util.Log;

import java.util.Hashtable;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationTask extends AsyncTaskBase<Void, Void, StationDTO> {
    private static final String _tag = "StationTask";
    private static final int _maxTries = 5;
    private static Hashtable<PointDTO, StationDTO> _cachedItems = new Hashtable<PointDTO, StationDTO>();
    private StationDAO _dao = null;
    private PointDTO _point = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public StationTask(double latitude, double longitude) {
        _dao = new StationDAO();
        _point = new PointDTO(latitude, longitude);
        _latitude = latitude;
        _longitude = longitude;
    }

    private static synchronized StationDTO getCachedItem(PointDTO point) {
        if (_cachedItems.containsKey(point)) {
            return _cachedItems.get(point);
        }
        return null;
    }

    private static synchronized void setCachedItem(PointDTO point, StationDTO station) {
        _cachedItems.put(point, station);
    }

    private StationDTO load() {
        int tries = 0;
        while (tries < _maxTries) {
            try {
                return _dao.nearestWithRadiosonde(_latitude, _longitude);
            } catch (Throwable t) {
                Log.w(_tag, "Exception", t);
            }
            ++tries;
        }
        return null;
    }

    @Override
    protected AsyncTaskResult<StationDTO> doInBackground(Void... params) {
        Log.d(_tag, "Running Task");
        try {
            StationDTO station = getCachedItem(_point);
            if (station == null) {
                station = load();
            }
            return new AsyncTaskResult<StationDTO>(station);
        } catch (Throwable t) {
            return new AsyncTaskResult<StationDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<StationDTO> result) {
        Log.d(_tag, "Completed Task");
        if (result.getResult() != null) {
            setCachedItem(_point, result.getResult());
        } else if (result.getThrowable() != null) {
            Log.e(_tag, "Exception", result.getThrowable());
        }
        FireCompleted(result);
    }
}
