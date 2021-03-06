package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentFeature;
import ca.datamagic.noaa.dao.APIDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HourlyForecastTask extends AsyncTaskBase<Void, Void, FeatureDTO> {
    private static Logger _logger = LogFactory.getLogger(HourlyForecastTask.class);
    private static APIDAO _dao = new APIDAO();

    public HourlyForecastTask() {
    }

    @Override
    protected AsyncTaskResult<FeatureDTO> doInBackground(Void... params) {
        _logger.info("Loading Hourly Forecast...");
        try {
            FeatureDTO feature = CurrentFeature.getFeature();
            FeatureDTO hourlyFeature = null;
            if (feature != null) {
                String timeZone = "";
                FeaturePropertiesDTO featureProperties = feature.getProperties();
                if (featureProperties != null) {
                    timeZone = featureProperties.getTimeZone();
                }
                hourlyFeature = _dao.loadForecastHourly(feature);
                FeaturePropertiesDTO hourlyFeatureProperties = hourlyFeature.getProperties();
                if (hourlyFeatureProperties != null) {
                    hourlyFeatureProperties.setTimeZone(timeZone);
                }
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
