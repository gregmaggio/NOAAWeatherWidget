package ca.datamagic.noaa.dto;

import org.json.JSONObject;

import java.util.Comparator;

public class StationDTO implements Comparable<StationDTO> {
    private String _stationId = null;
    private String _stationName = null;
    private String _streetNumber = null;
    private String _streetName = null;
    private String _city = null;
    private String _stateCode = null;
    private String _stateName = null;
    private String _zip = null;
    private String _countryCode = null;
    private String _countryName = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private Boolean _hasRadiosonde = null;

    public StationDTO() {

    }

    public StationDTO(JSONObject obj) {
        _stationId = obj.optString("stationId", "");
        _stationName = obj.optString("stationName", "");
        _streetNumber = obj.optString("streetNumber", "");
        _streetName = obj.optString("streetName", "");
        _city = obj.optString("city", "");
        _stateCode = obj.optString("stateCode", "");
        _stateName = obj.optString("stateName", "");
        _zip = obj.optString("zip", "");
        _countryCode = obj.optString("country_code", "");
        _countryName = obj.optString("country_name", "");
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

    public String getStreetNumber() {
        return _streetNumber;
    }

    public void setStreetNumber(String newVal) {
        _streetNumber = newVal;
    }

    public String getStreetName() {
        return _streetName;
    }

    public void setStreetName(String newVal) {
        _streetName = newVal;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String newVal) {
        _city = newVal;
    }

    public String getStateCode() {
        return _stateCode;
    }

    public void setStateCode(String newVal) {
        _stateCode = newVal;
    }

    public String getStateName() {
        return _stateName;
    }

    public void setStateName(String newVal) {
        _stateName = newVal;
    }

    public String getZip() {
        return _zip;
    }

    public void setZip(String newVal) {
        _zip = newVal;
    }

    public String getCountryCode() {
        return _countryCode;
    }

    public void setCountryCode(String newVal) {
        _countryCode = newVal;
    }

    public String getCountryName() {
        return _countryName;
    }

    public void setCountryName(String newVal) {
        _countryName = newVal;
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

    @Override
    public int compareTo(StationDTO another) {
        if ((_stationName != null) && (another != null) && (another._stationName != null)) {
            return _stationName.compareToIgnoreCase(another._stationName);
        }
        return 0;
    }
}
