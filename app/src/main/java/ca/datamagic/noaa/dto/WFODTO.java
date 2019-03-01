package ca.datamagic.noaa.dto;

import org.json.JSONObject;

public class WFODTO {
    private String _cwa = null;
    private String _wfo = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private String _region = null;
    private String _fullStationId = null;
    private String _cityState = null;
    private String _city = null;
    private String _state = null;
    private String _stateAbbreviation = null;
    private String _radar = null;

    public WFODTO() {
    }

    public WFODTO(JSONObject obj) {
        _cwa = obj.optString("cwa", "");
        _wfo = obj.optString("wfo", "");
        _longitude = obj.optDouble("longitude", 0.0);
        _latitude = obj.optDouble("latitude", 0.0);
        _region = obj.optString("region", "");
        _fullStationId = obj.optString("fullStationId", "");
        _cityState = obj.optString("cityState", "");
        _city = obj.optString("city", "");
        _stateAbbreviation = obj.optString("stateAbbreviation", "");
        _radar = obj.optString("radar", "");
    }

    public String getCWA() {
        return _cwa;
    }

    public String getWFO() {
        return _wfo;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public String getRegion() {
        return _region;
    }

    public String getFullStationId() {
        return _fullStationId;
    }

    public String getCityState() {
        return _cityState;
    }

    public String getCity() {
        return _city;
    }

    public String getState() {
        return _state;
    }

    public String getStateAbbreviation() {
        return _stateAbbreviation;
    }

    public String getRadar() {
        return _radar;
    }

    public void setCWA(String newVal) {
        _cwa = newVal;
    }

    public void setWFO(String newVal) {
        _wfo = newVal;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }

    public void setRegion(String newVal) {
        _region = newVal;
    }

    public void setFullStationId(String newVal) {
        _fullStationId = newVal;
    }

    public void setCityState(String newVal) {
        _cityState = newVal;
    }

    public void setCity(String newVal) {
        _city = newVal;
    }

    public void setState(String newVal) {
        _state = newVal;
    }

    public void setStateAbbreviation(String newVal) {
        _stateAbbreviation = newVal;
    }

    public void setRadar(String newVal) {
        _radar = newVal;
    }
}
