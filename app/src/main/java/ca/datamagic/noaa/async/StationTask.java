package ca.datamagic.noaa.async;

import java.util.Hashtable;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationTask extends AsyncTaskBase<Void, Void, StationDTO> {
    private Logger _logger = LogFactory.getLogger(StationTask.class);
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

    @Override
    protected AsyncTaskResult<StationDTO> doInBackground(Void... params) {
        _logger.info("Retrieving station...");
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
        _logger.info("...station retrieved.");
        if (result.getResult() != null) {
            setCachedItem(_point, result.getResult());
        }
        fireCompleted(result);
    }
}
