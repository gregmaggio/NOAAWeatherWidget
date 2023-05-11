package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentRadarSites;
import ca.datamagic.noaa.dao.RadarSiteDAO;
import ca.datamagic.noaa.dto.RadarSiteDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarSiteTask extends AsyncTaskBase<Void, Void, RadarSiteDTO> {
    private static final Logger _logger = LogFactory.getLogger(RadarSiteTask.class);
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public RadarSiteTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<RadarSiteDTO> doInBackground(Void... params) {
        try {
            _logger.info("Loading radar...");
            RadarSiteDAO dao = CurrentRadarSites.getRadarSiteDAO();
            RadarSiteDTO site = null;
            if (dao != null) {
                site = dao.readNearest(_latitude, _longitude);
            }
            return new AsyncTaskResult<RadarSiteDTO>(site);
        } catch (Throwable t) {
            return new AsyncTaskResult<RadarSiteDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<RadarSiteDTO> result) {
        _logger.info("...radar loaded.");
        fireCompleted(result);
    }
}
