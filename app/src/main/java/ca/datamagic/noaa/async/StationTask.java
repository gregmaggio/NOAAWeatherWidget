package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class StationTask extends AsyncTaskBase<Void, Void, StationDTO> {
    private static Logger _logger = LogFactory.getLogger(StationTask.class);
    private static StationDAO _dao = new StationDAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public StationTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<StationDTO> doInBackground(Void... params) {
        try {
            _logger.info("Loading Station...");
            StationDTO station = _dao.read(_latitude, _longitude);
            return new AsyncTaskResult<StationDTO>(station);
        } catch (Throwable t) {
            return new AsyncTaskResult<StationDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<StationDTO> result) {
        _logger.info("...station loaded.");
        fireCompleted(result);
    }
}
