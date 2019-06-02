package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.TimeZoneDAO;
import ca.datamagic.noaa.logging.LogFactory;

public class TimeZoneTask extends AsyncTaskBase<Void, Void, String> {
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
    protected AsyncTaskResult<String> doInBackground(Void... voids) {
        try {
            _logger.info("Loading TimeZOne...");
            String timeZoneId = _dao.loadTimeZone(_latitude, _longitude);
            return new AsyncTaskResult<String>(timeZoneId);
        } catch (Throwable t) {
            return new AsyncTaskResult<String>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        _logger.info("...TimeZone loaded.");
        fireCompleted(result);
    }
}
