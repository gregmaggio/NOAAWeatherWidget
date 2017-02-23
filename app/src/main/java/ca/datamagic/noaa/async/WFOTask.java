package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Hashtable;
import java.util.List;

import ca.datamagic.noaa.dao.WFODAO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 1/3/2016.
 */
public class WFOTask extends AsyncTaskBase<Void, Void, List<WFODTO>> {
    private static Hashtable<PointDTO, List<WFODTO>> _cachedItems = new Hashtable<PointDTO, List<WFODTO>>();
    private Logger _logger = LogManager.getLogger(WFOTask.class);
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

    @Override
    protected AsyncTaskResult<List<WFODTO>> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            List<WFODTO> wfo = getCachedItem(_point);
            if (wfo == null) {
                wfo = _dao.read(_latitude, _longitude);
            }
            return new AsyncTaskResult<List<WFODTO>>(wfo);
        } catch (Throwable t) {
            return new AsyncTaskResult<List<WFODTO>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<WFODTO>> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Completed Task");
        }
        if (result.getResult() != null) {
            setCachedItem(_point, result.getResult());
        }
        fireCompleted(result);
    }
}
