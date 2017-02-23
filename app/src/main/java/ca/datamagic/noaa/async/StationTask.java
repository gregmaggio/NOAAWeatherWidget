package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Hashtable;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationTask extends AsyncTaskBase<Void, Void, StationDTO> {
    private static Hashtable<PointDTO, StationDTO> _cachedItems = new Hashtable<PointDTO, StationDTO>();
    private Logger _logger = LogManager.getLogger(StationTask.class);
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

    @Override
    protected AsyncTaskResult<StationDTO> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            StationDTO station = getCachedItem(_point);
            if (station == null) {
                station = _dao.nearestWithRadiosonde(_latitude, _longitude);
            }
            return new AsyncTaskResult<StationDTO>(station);
        } catch (Throwable t) {
            return new AsyncTaskResult<StationDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<StationDTO> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Completed Task");
        }
        if (result.getResult() != null) {
            setCachedItem(_point, result.getResult());
        }
        fireCompleted(result);
    }
}
