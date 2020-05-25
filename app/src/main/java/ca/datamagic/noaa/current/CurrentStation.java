package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.StationDTO;

public class CurrentStation {
    private static StationDTO _station = null;

    public static synchronized StationDTO getStation() {
        return _station;
    }

    public static synchronized void setStation(StationDTO newVal) {
        _station = newVal;
    }
}
