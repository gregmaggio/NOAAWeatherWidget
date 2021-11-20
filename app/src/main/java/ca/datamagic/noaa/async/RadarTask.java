package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarTask extends AsyncTaskBase<Void, Void, RadarDTO> {
    private static final Logger _logger = LogFactory.getLogger(RadarTask.class);
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public RadarTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<RadarDTO> doInBackground(Void... params) {
        try {
            _logger.info("Loading radar...");
            RadarDAO dao = new RadarDAO();
            RadarDTO radar = dao.loadNearest(_latitude, _longitude);
            return new AsyncTaskResult<RadarDTO>(radar);
        } catch (Throwable t) {
            return new AsyncTaskResult<RadarDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<RadarDTO> result) {
        _logger.info("...radar loaded.");
        fireCompleted(result);
    }
}
