package ca.datamagic.noaa.dto;

public class StationDTO {
    private String _stationId = null;
    private String _stationName = null;
    private String _state = null;
    private String _wfo = null;
    private String _radar = null;
    private String _timeZoneId = null;
    private Double _latitude = null;
    private Double _longitude = null;

    public StationDTO() {
    }

    public String getStationId() {
        return _stationId;
    }

    public String getStationName() {
        return _stationName;
    }

    public String getState() {
        return _state;
    }

    public String getWFO() {
        return _wfo;
    }

    public String getRadar() {
        return _radar;
    }

    public String getTimeZoneId() {
        return _timeZoneId;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public void setStationId(String newVal) {
        _stationId = newVal;
    }

    public void setStationName(String newVal) {
        _stationName = newVal;
    }

    public void setState(String newVal) {
        _state = newVal;
    }

    public void setWFO(String newVal) {
        _wfo = newVal;
    }

    public void setRadar(String newVal) {
        _radar = newVal;
    }

    public void setTimeZoneId(String newVal) {
        _timeZoneId = newVal;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Station(");
        builder.append("stationId=" + _stationId);
        builder.append(",stationName=" + _stationName);
        builder.append(",state=" + _state);
        builder.append(",wfo=" + _wfo);
        builder.append(",radar=" + _radar);
        builder.append(",timeZoneId=" + _timeZoneId);
        builder.append(",latitude=" + _latitude);
        builder.append(",longitude=" + _longitude);
        builder.append(")");
        return builder.toString();
    }
}
