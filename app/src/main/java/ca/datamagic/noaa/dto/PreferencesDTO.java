package ca.datamagic.noaa.dto;

public class PreferencesDTO {
    private Double _latitude = null;
    private Double _longitude = null;
    private Integer _numDays = null;
    private String _unit = null;
    private String _format = null;
    private String _temperatureUnits = null;
    private String _windSpeedUnits = null;
    private String _pressureUnits = null;
    private String _visibilityUnits = null;
    private String _heightUnits = null;

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public Integer getNumDays() {
        return _numDays;
    }

    public String getUnit() {
        return _unit;
    }

    public String getFormat() {
        return _format;
    }

    public String getTemperatureUnits() {
        return _temperatureUnits;
    }

    public String getWindSpeedUnits() {
        return _windSpeedUnits;
    }

    public String getPressureUnits() {
        return _pressureUnits;
    }

    public String getVisibilityUnits() {
        return _visibilityUnits;
    }

    public String getHeightUnits() {
        return _heightUnits;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }

    public void setNumDays(Integer newVal) {
        _numDays = newVal;
    }

    public void setUnit(String newVal) {
        _unit = newVal;
    }

    public void setFormat(String newVal) {
        _format = newVal;
    }

    public void setTemperatureUnits(String newVal) {
        _temperatureUnits = newVal;
    }

    public void setWindSpeedUnits(String newVal) {
        _windSpeedUnits = newVal;
    }

    public void setPressureUnits(String newVal) {
        _pressureUnits = newVal;
    }

    public void setVisibilityUnits(String newVal) {
        _visibilityUnits = newVal;
    }

    public void setHeightUnits(String newVal) {
        _heightUnits = newVal;
    }
}
