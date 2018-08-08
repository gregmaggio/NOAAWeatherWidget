package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationTask extends AsyncTaskBase<Void, Void, StationDTO> {
    private static Logger _logger = LogFactory.getLogger(StationTask.class);
    private static StationDAO _dao = new StationDAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;
    private boolean _includeRadiosonde = true;

    public StationTask(double latitude, double longitude, boolean includeRadiosonde) {
        _latitude = latitude;
        _longitude = longitude;
        _includeRadiosonde = includeRadiosonde;
    }

    @Override
    protected AsyncTaskResult<StationDTO> doInBackground(Void... params) {
        _logger.info("Retrieving station...");
        try {
            StationDTO station = null;
            if (_includeRadiosonde) {
                station = _dao.nearestWithRadiosonde(_latitude, _longitude);
            } else {
                station = _dao.nearest(_latitude, _longitude);
            }
            return new AsyncTaskResult<StationDTO>(station);
        } catch (Throwable t) {
            return new AsyncTaskResult<StationDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<StationDTO> result) {
        _logger.info("...station retrieved.");
        fireCompleted(result);
    }
}
