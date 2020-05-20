package ca.datamagic.noaa.dto;

import android.graphics.Color;

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
    private Boolean _textOnly = Boolean.FALSE;
    private String _dateFormat = "yyyy-MM-dd";
    private String _timeFormat = "HH:mm";
    private Integer _widgetFontColor = Color.WHITE;
    private Boolean _showNewFeatures = Boolean.TRUE;

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

    public Boolean isTextOnly() { return _textOnly; }

    public String getDateFormat() {
        return _dateFormat;
    }

    public String getTimeFormat() {
        return _timeFormat;
    }

    public Integer getWidgetFontColor() {
        return _widgetFontColor;
    }

    public Boolean getShowNewFeatures() {
        return _showNewFeatures;
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

    public void setTextOnly(Boolean newVal) { _textOnly = newVal; }

    public void setDateFormat(String newVal) {
        _dateFormat = newVal;
    }

    public void setTimeFormat(String newVal) {
        _timeFormat = newVal;
    }

    public void setWidgetFontColor(Integer newVal) {
        _widgetFontColor = newVal;
    }

    public void setShowNewFeatures(Boolean newVal) {
        _showNewFeatures = newVal;
    }
}
