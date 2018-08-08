package ca.datamagic.noaa.dto;

public class RadarDTO {
    private StringListDTO _backgroundImages = null;
    private StringListDTO _radarImages = null;

    public StringListDTO getBackgroundImages() {
        return _backgroundImages;
    }

    public void setBackgroundImages(StringListDTO newVal) {
        _backgroundImages = newVal;
    }

    public StringListDTO getRadarImages() {
        return _radarImages;
    }

    public void setRadarImages(StringListDTO newVal) {
        _radarImages = newVal;
    }
}
