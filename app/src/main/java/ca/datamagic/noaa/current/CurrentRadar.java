package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.RadarDTO;

public class CurrentRadar {
    private static RadarDTO _radar = null;

    public static synchronized RadarDTO getRadar() {
        return _radar;
    }

    public static synchronized void setRadar(RadarDTO newVal) {
        _radar = newVal;
    }
}
