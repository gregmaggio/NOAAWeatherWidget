package ca.datamagic.noaa.dto;

/**
 * Created by Greg on 1/1/2016.
 */
public class DWMLDTO {
    public static final String NodeName = "dwml";
    public static final String VersionAttribute = "version";
    private boolean _cached = false;
    private String _version = null;
    private HeadDTO _head = null;
    private DataDTO _forecast = null;
    private DataDTO _observation = null;

    public boolean isCached() {
        return _cached;
    }

    public void setCached(boolean newVal) {
        _cached = newVal;
    }

    public String getVersion() {
        return _version;
    }

    public void setVersion(String newVal) {
        _version = newVal;
    }

    public HeadDTO getHead() {
        return _head;
    }

    public void setHead(HeadDTO newVal) {
        _head = newVal;
    }

    public DataDTO getForecast() {
        return _forecast;
    }

    public void setForecast(DataDTO newVal) {
        _forecast = newVal;
    }

    public DataDTO getObservation() {
        return _observation;
    }

    public void setObservation(DataDTO newVal) {
        _observation = newVal;
    }
}
