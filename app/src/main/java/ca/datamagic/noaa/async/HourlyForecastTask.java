package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentFeature;
import ca.datamagic.noaa.current.CurrentStation;
import ca.datamagic.noaa.dao.APIDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.exception.MarineForecastNotSupportedException;
import ca.datamagic.noaa.logging.LogFactory;

public class HourlyForecastTask extends AsyncTaskBase<FeatureDTO> {
    private static Logger _logger = LogFactory.getLogger(HourlyForecastTask.class);
    private static APIDAO _dao = new APIDAO();

    public HourlyForecastTask() {
    }

    @Override
    protected AsyncTaskResult<FeatureDTO> doInBackground() {
        _logger.info("Loading Hourly Forecast...");
        try {
            FeatureDTO feature = CurrentFeature.getFeature();
            FeatureDTO hourlyFeature = null;
            StationDTO[] nearest = CurrentStation.getNearest();
            try {
                hourlyFeature = _dao.loadForecastHourly(feature);
                if (hourlyFeature == null) {
                    feature = null;
                }
            } catch (MarineForecastNotSupportedException ex) {
                _logger.warning("MarineForecastNotSupportedException");
                feature = null;
                hourlyFeature = null;
            }
            if (feature == null) {
                if (nearest != null) {
                    for (int ii = 0; ii < nearest.length; ii++) {
                        feature = _dao.loadFeature(nearest[ii].getLatitude(), nearest[ii].getLongitude());
                        if (feature != null) {
                            try {
                                hourlyFeature = _dao.loadForecastHourly(feature);
                                if (hourlyFeature == null) {
                                    feature = null;
                                }
                            } catch (MarineForecastNotSupportedException ex) {
                                _logger.warning("MarineForecastNotSupportedException");
                                feature = null;
                                hourlyFeature = null;
                            }
                        }
                        if (feature != null) {
                            break;
                        }
                    }
                }
            }
            if (feature != null) {
                String timeZone = "";
                FeaturePropertiesDTO featureProperties = feature.getProperties();
                if (featureProperties != null) {
                    timeZone = featureProperties.getTimeZone();
                }
                if (hourlyFeature != null) {
                    FeaturePropertiesDTO hourlyFeatureProperties = hourlyFeature.getProperties();
                    if (hourlyFeatureProperties != null) {
                        hourlyFeatureProperties.setTimeZone(timeZone);
                    }
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
