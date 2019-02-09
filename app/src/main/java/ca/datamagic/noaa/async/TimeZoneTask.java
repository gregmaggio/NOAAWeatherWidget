package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.TimeZoneDAO;
import ca.datamagic.noaa.dto.TimeZoneDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class TimeZoneTask extends AsyncTaskBase<Void, Void, TimeZoneDTO> {
    private Logger _logger = LogFactory.getLogger(GooglePlaceTask.class);
    private TimeZoneDAO _dao = null;
    private double _latitude = 0;
    private double _longitude = 0;

    public TimeZoneTask(double latitude, double longitude) {
        _dao = new TimeZoneDAO();
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<TimeZoneDTO> doInBackground(Void... voids) {
        try {
            _logger.info("Loading TimeZOne...");
            TimeZoneDTO timeZone = _dao.loadTimeZone(_latitude, _longitude);
            return new AsyncTaskResult<TimeZoneDTO>(timeZone);
        } catch (Throwable t) {
            return new AsyncTaskResult<TimeZoneDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<TimeZoneDTO> result) {
        _logger.info("...TimeZone loaded.");
        fireCompleted(result);
    }
}
