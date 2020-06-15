package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.FeatureDTO;

public class CurrentDailyForecast {
    private static FeatureDTO _dailyForecastFeature = null;

    public static synchronized FeatureDTO getDailyForecastFeature() {
        return _dailyForecastFeature;
    }

    public static synchronized void setDailyForecastFeature(FeatureDTO newVal) {
        _dailyForecastFeature = newVal;
    }
}
