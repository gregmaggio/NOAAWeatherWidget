package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentStations;
import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class StationTask extends AsyncTaskBase<StationDTO[]> {
    private static final Logger _logger = LogFactory.getLogger(StationTask.class);
    private static final double distance = 75;
    private static final String units = "statute miles";
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public StationTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<StationDTO[]> doInBackground() {
        try {
            _logger.info("Loading Station...");
            StationDTO[] nearest = null;
            StationDAO dao = CurrentStations.getStationDAO();
            if (dao != null) {
                nearest = dao.readNearest(_latitude, _longitude, distance, units);
                if ((nearest == null) || (nearest.length < 1)) {
                    StationDTO nearestStation = dao.readNearest(_latitude, _longitude);
                    if (nearestStation != null) {
                        nearest = new StationDTO[] { nearestStation };
                    }
                }
            }
            return new AsyncTaskResult<StationDTO[]>(nearest);
        } catch (Throwable t) {
            return new AsyncTaskResult<StationDTO[]>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<StationDTO[]> result) {
        _logger.info("...station loaded.");
        fireCompleted(result);
    }
}
