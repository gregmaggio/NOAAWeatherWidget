package ca.datamagic.noaa.dto;

import org.json.JSONObject;

public class StationDTO {
    private String _stationId = null;
    private String _stationName = null;
    private String _state = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private Boolean _hasRadiosonde = null;

    public StationDTO() {

    }

    public StationDTO(JSONObject obj) {
        _stationId = obj.optString("stationId", "");
        _stationName = obj.optString("stationName", "");
        _state = obj.optString("state", "");
        _longitude = obj.optDouble("longitude", 0.0);
        _latitude = obj.optDouble("latitude", 0.0);
        _hasRadiosonde = obj.optBoolean("hasRadiosonde", false);
    }

    public String getStationId() {
        return _stationId;
    }

    public void setStationId(String newVal) {
        _stationId = newVal;
    }

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String newVal) {
        _stationName = newVal;
    }

    public String getState() {
        return _state;
    }

    public void setState(String newVal) {
        _state = newVal;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }

    public Boolean getHasRadiosonde() {
        return _hasRadiosonde;
    }

    public void setHasRadiosonde(Boolean newVal) {
        _hasRadiosonde = newVal;
    }
}
