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

public class DailyForecastTask extends AsyncTaskBase<Void, Void, FeatureDTO> {
    private static Logger _logger = LogFactory.getLogger(DailyForecastTask.class);
    private static APIDAO _dao = new APIDAO();

    public DailyForecastTask() {
    }

    @Override
    protected AsyncTaskResult<FeatureDTO> doInBackground(Void... params) {
        _logger.info("Loading Daily Forecast...");
        try {
            FeatureDTO feature = CurrentFeature.getFeature();
            FeatureDTO dailyFeature = null;
            if (feature != null) {
                String timeZone = "";
                FeaturePropertiesDTO featureProperties = feature.getProperties();
                if (featureProperties != null) {
                    timeZone = featureProperties.getTimeZone();
                }
                try {
                    dailyFeature = _dao.loadForecast(feature);
                } catch (MarineForecastNotSupportedException ex) {
                    _logger.warning("MarineForecastNotSupportedException");
                    if ((feature.getProperties() != null) && (feature.getProperties().getRadarStation() != null)) {
                        RadarDAO radarDAO = new RadarDAO();
                        RadarDTO radar = radarDAO.load(feature.getProperties().getRadarStation());
                        if (radar != null) {
                            FeatureDTO radarFeature = _dao.loadFeature(radar.getLatitude(), radar.getLongitude());
                            if (radarFeature != null) {
                                dailyFeature = _dao.loadForecast(radarFeature);
                            }
                        }
                    }
                }
                if (dailyFeature != null) {
                    FeaturePropertiesDTO dailyFeatureProperties = dailyFeature.getProperties();
                    if (dailyFeatureProperties != null) {
                        dailyFeatureProperties.setTimeZone(timeZone);
                    }
                }
            }
            return new AsyncTaskResult<FeatureDTO>(dailyFeature);
        } catch (Throwable t) {
            return new AsyncTaskResult<FeatureDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<FeatureDTO> result) {
        _logger.info("...Hourly Daily loaded.");
        fireCompleted(result);
    }
}
