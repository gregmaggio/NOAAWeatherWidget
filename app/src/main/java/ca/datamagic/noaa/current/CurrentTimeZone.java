package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.TimeZoneDTO;

public class CurrentTimeZone {
    private static TimeZoneDTO _timeZone = null;

    public static synchronized TimeZoneDTO getTimeZone() {
        return _timeZone;
    }

    public static synchronized void setTimeZone(TimeZoneDTO newVal) {
        _timeZone = newVal;
    }
}
