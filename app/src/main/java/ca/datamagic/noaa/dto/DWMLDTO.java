package ca.datamagic.noaa.dto;

/**
 * Created by Greg on 1/1/2016.
 */
public class DWMLDTO {
    public static final String NodeName = "dwml";
    public static final String VersionAttribute = "version";
    private String _version = null;
    private DataDTO _forecast = null;
    private DataDTO _observation = null;

    public String getVersion() {
        return _version;
    }

    public void setVersion(String newVal) {
        _version = newVal;
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
