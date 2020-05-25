package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.FeatureDTO;

public class CurrentHourlyForecast {
    private static FeatureDTO _hourlyForecastFeature = null;

    public static synchronized FeatureDTO getHourlyForecastFeature() {
        return _hourlyForecastFeature;
    }

    public static synchronized void setHourlyForecastFeature(FeatureDTO newVal) {
        _hourlyForecastFeature = newVal;
    }
}
