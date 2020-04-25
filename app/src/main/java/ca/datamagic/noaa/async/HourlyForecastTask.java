package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.APIDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
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
            String timeZone = "";
            FeaturePropertiesDTO featureProperties = feature.getProperties();
            if (featureProperties != null) {
                timeZone = featureProperties.getTimeZone();
            }
            FeatureDTO hourlyFeature = _dao.loadForecastHourly(feature);
            FeaturePropertiesDTO hourlyFeatureProperties = hourlyFeature.getProperties();
            if (hourlyFeatureProperties != null) {
                hourlyFeatureProperties.setTimeZone(timeZone);
            }
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
