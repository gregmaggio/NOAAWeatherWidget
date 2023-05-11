package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.RadarSiteDTO;

public class CurrentRadarSite {
    private static RadarSiteDTO[] nearest = null;

    private static RadarSiteDTO radarSite = null;

    public static synchronized RadarSiteDTO[] getNearest() {
        return nearest;
    }

    public static synchronized void setNearest(RadarSiteDTO[] newVal) {
        nearest = newVal;
    }

    public static synchronized RadarSiteDTO getRadarSite() {
        return radarSite;
    }

    public static synchronized void setRadarSite(RadarSiteDTO newVal) {
        radarSite = newVal;
    }
}
