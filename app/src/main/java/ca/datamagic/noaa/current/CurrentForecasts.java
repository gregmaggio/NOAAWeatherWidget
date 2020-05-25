package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.ForecastsDTO;

public class CurrentForecasts {
    private static ForecastsDTO _forecasts = null;

    public synchronized static ForecastsDTO getForecasts() {
        return _forecasts;
    }

    public synchronized static void setForecasts(ForecastsDTO newVal) {
        _forecasts = newVal;
    }
}
