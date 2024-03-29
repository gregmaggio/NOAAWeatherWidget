package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.StationDTO;

public class CurrentStation {
    private static StationDTO[] _nearest = null;

    private static StationDTO _station = null;

    public static synchronized StationDTO[] getNearest() {
        return _nearest;
    }

    public static synchronized void setNearest(StationDTO[] newVal) {
        _nearest = newVal;
    }

    public static synchronized StationDTO getStation() {
        return _station;
    }

    public static synchronized void setStation(StationDTO newVal) {
        _station = newVal;
    }
}
