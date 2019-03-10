package ca.datamagic.noaa.dto;

import org.json.JSONObject;

public class StationDTO {
    private String _stationId = null;
    private String _stationName = null;
    private String _state = null;
    private String _wfo = null;
    private String _radar = null;
    private Double _latitude = null;
    private Double _longitude = null;

    public StationDTO() {
    }

    public StationDTO(JSONObject obj) {
        _stationId = obj.optString("stationId", "");
        _stationName = obj.optString("stationName", "");
        _state = obj.optString("state", "");
        _wfo = obj.optString("wfo", "");
        _radar = obj.optString("radar", "");
        _longitude = obj.optDouble("longitude", 0.0);
        _latitude = obj.optDouble("latitude", 0.0);
    }

    public String getStationId() {
        return _stationId;
    }

    public String getStationName() {
        return _stationName;
    }

    public String getWFO() {
        return _wfo;
    }

    public String getRadar() {
        return _radar;
    }

    public String getState() {
        return _state;
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

    public void setWFO(String newVal) {
        _wfo = newVal;
    }

    public void setRadar(String newVal) {
        _radar = newVal;
    }

    public void setState(String newVal) {
        _state = newVal;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }
}
