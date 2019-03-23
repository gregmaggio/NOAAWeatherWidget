package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.APIDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HourlyForecastTask extends AsyncTaskBase<Void, Void, FeatureDTO> {
    private static Logger _logger = LogFactory.getLogger(DWMLTask.class);
    private static APIDAO _dao = new APIDAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public HourlyForecastTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<FeatureDTO> doInBackground(Void... params) {
        _logger.info("Loading Hourly Forecast...");
        try {
            FeatureDTO feature = _dao.loadFeature(_latitude, _longitude);
            FeatureDTO hourlyFeature = _dao.loadForecastHourly(feature);
            return new AsyncTaskResult<FeatureDTO>(hourlyFeature);
        } catch (Throwable t) {
            return new AsyncTaskResult<FeatureDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<FeatureDTO> result) {
        _logger.info("...Hourly Forecast loaded.");
        fireCompleted(result);
    }
}
