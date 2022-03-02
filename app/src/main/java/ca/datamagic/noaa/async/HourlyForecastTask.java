package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentFeature;
import ca.datamagic.noaa.dao.APIDAO;
import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.exception.MarineForecastNotSupportedException;
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
                try {
                    hourlyFeature = _dao.loadForecastHourly(feature);
                } catch (MarineForecastNotSupportedException ex) {
                    _logger.warning("MarineForecastNotSupportedException");
                    if ((feature.getProperties() != null) && (feature.getProperties().getRadarStation() != null)) {
                        RadarDAO radarDAO = new RadarDAO();
                        RadarDTO radar = radarDAO.load(feature.getProperties().getRadarStation());
                        if (radar != null) {
                            FeatureDTO radarFeature = _dao.loadFeature(radar.getLatitude(), radar.getLongitude());
                            if (radarFeature != null) {
                                hourlyFeature = _dao.loadForecastHourly(radarFeature);
                            }
                        }
                    }
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
