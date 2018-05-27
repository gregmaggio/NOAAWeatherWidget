package ca.datamagic.noaa.async;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.util.Calendar;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.TimeZoneDAO;
import ca.datamagic.noaa.dto.SunriseSunsetDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class SunriseSunsetTask extends AsyncTaskBase<Void, Void, SunriseSunsetDTO> {
    private Logger _logger = LogFactory.getLogger(DWMLTask.class);
    private TimeZoneDAO _dao = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public SunriseSunsetTask(double latitude, double longitude) {
        _dao = new TimeZoneDAO();
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<SunriseSunsetDTO> doInBackground(Void... voids) {
        _logger.info("Loading Sunrise/Sunset...");
        try {
            String timeZone = _dao.getTimeZone(_latitude, _longitude);
            Calendar today = Calendar.getInstance();
            SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(new Location(Double.toString(_latitude), Double.toString(_longitude)), timeZone);
            String sunrise = calculator.getOfficialSunriseForDate(today);
            String sunset = calculator.getOfficialSunsetForDate(today);
            SunriseSunsetDTO dto = new SunriseSunsetDTO();
            dto.setSunrise(sunrise);
            dto.setSunset(sunset);
            return new AsyncTaskResult<SunriseSunsetDTO>(dto);
        } catch (Throwable t) {
            return new AsyncTaskResult<SunriseSunsetDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<SunriseSunsetDTO> result) {
        _logger.info("...Sunrise/Sunset loaded.");
        fireCompleted(result);
    }
}
