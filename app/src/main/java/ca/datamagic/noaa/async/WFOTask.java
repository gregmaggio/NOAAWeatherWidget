package ca.datamagic.noaa.async;

import android.util.Log;

import java.util.Hashtable;
import java.util.List;

import ca.datamagic.noaa.dao.WFODAO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 1/3/2016.
 */
public class WFOTask extends AsyncTaskBase<Void, Void, List<WFODTO>> {
    private static final String _tag = "WFOTask";
    private static final int _maxTries = 5;
    private static Hashtable<PointDTO, List<WFODTO>> _cachedItems = new Hashtable<PointDTO, List<WFODTO>>();
    private WFODAO _dao = null;
    private PointDTO _point = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public WFOTask(double latitude, double longitude) {
        _dao = new WFODAO();
        _point = new PointDTO(latitude, longitude);
        _latitude = latitude;
        _longitude = longitude;
    }

    private static synchronized List<WFODTO> getCachedItem(PointDTO point) {
        if (_cachedItems.containsKey(point)) {
            return _cachedItems.get(point);
        }
        return null;
    }

    private static synchronized void setCachedItem(PointDTO point, List<WFODTO> wfo) {
        _cachedItems.put(point, wfo);
    }

    private List<WFODTO> load() {
        int tries = 0;
        while (tries < _maxTries) {
            try {
                return _dao.read(_latitude, _longitude);
            } catch (Throwable t) {
                Log.w(_tag, "Exception", t);
            }
            ++tries;
        }
        return null;
    }

    @Override
    protected AsyncTaskResult<List<WFODTO>> doInBackground(Void... params) {
        Log.d(_tag, "Running Task");
        try {
            List<WFODTO> wfo = getCachedItem(_point);
            if (wfo == null) {
                wfo = load();
            }
            return new AsyncTaskResult<List<WFODTO>>(wfo);
        } catch (Throwable t) {
            return new AsyncTaskResult<List<WFODTO>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<WFODTO>> result) {
        Log.d(_tag, "Completed Task");
        if (result.getResult() != null) {
            setCachedItem(_point, result.getResult());
        } else if (result.getThrowable() != null) {
            Log.e(_tag, "Exception", result.getThrowable());
        }
        FireCompleted(result);
    }
}
