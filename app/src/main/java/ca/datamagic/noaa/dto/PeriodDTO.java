package ca.datamagic.noaa.dto;

import org.json.JSONObject;

public class PeriodDTO {
    private Integer _number = null;
    private String _name = null;
    private String _startTime = null;
    private String _endTime = null;
    private Boolean _isDaytime = null;
    private Double _temperature = null;
    private String _temperatureUnit = null;
    private String _temperatureTrend = null;
    private String _windSpeed = null;
    private String _windDirection = null;
    private String _icon = null;
    private String _shortForecast = null;
    private String _detailedForecast = null;

    public PeriodDTO() {

    }

    public PeriodDTO(JSONObject obj) {
        _number = obj.optInt("number", 0);
        _name = obj.optString("name", "");
        _startTime = obj.optString("startTime", "");
        _endTime = obj.optString("endTime", "");
        _isDaytime = obj.optBoolean("isDaytime", false);
        _temperature = obj.optDouble("temperature", 0);
        _temperatureUnit = obj.optString("temperatureUnit", "");
        _temperatureTrend = obj.optString("temperatureTrend", "");
        _windSpeed = obj.optString("windSpeed", "");
        _windDirection = obj.optString("windDirection", "");
        _icon = obj.optString("icon", "");
        _shortForecast = obj.optString("shortForecast", "");
        _detailedForecast = obj.optString("detailedForecast", "");
    }

    public Integer getNumber() {
        return _number;
    }

    public String getName() {
        return _name;
    }

    public String getStartTime() {
        return _startTime;
    }

    public String getEndTime() {
        return _endTime;
    }

    public Boolean isDaytime() {
        return _isDaytime;
    }

    public Double getTemperature() {
        return _temperature;
    }

    public String getTemperatureUnit() {
        return _temperatureUnit;
    }

    public String getTemperatureTrend() {
        return _temperatureTrend;
    }

    public String getWindSpeed() {
        return _windSpeed;
    }

    public String getWindDirection() {
        return _windDirection;
    }

    public String getIcon() {
        return _icon;
    }

    public String getShortForecast() {
        return _shortForecast;
    }

    public String getDetailedForecast() {
        return _detailedForecast;
    }

    public void setNumber(Integer newVal) {
        _number = newVal;
    }

    public void setName(String newVal) {
        _name = newVal;
    }

    public void setStartTime(String newVal) {
        _startTime = newVal;
    }

    public void setEndTime(String newVal) {
        _endTime = newVal;
    }

    public void setDaytime(Boolean newVal) {
        _isDaytime = newVal;
    }

    public void setTemperature(Double newVal) {
        _temperature = newVal;
    }

    public void setTemperatureUnit(String newVal) {
        _temperatureUnit = newVal;
    }

    public void setTemperatureTrend(String newVal) {
        _temperatureTrend = newVal;
    }

    public void setWindSpeed(String newVal) {
        _windSpeed = newVal;
    }

    public void setWindDirection(String newVal) {
        _windDirection = newVal;
    }

    public void setIcon(String newVal) {
        _icon = newVal;
    }

    public void setShortForecast(String newVal) {
        _shortForecast = newVal;
    }

    public void setDetailedForecast(String newVal) {
        _detailedForecast = newVal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("number=" + _number);
        builder.append("\nname=" + _name);
        builder.append("\nstartTime=" + _startTime);
        builder.append("\nendTime=" + _endTime);
        builder.append("\nisDaytime=" + _isDaytime);
        builder.append("\ntemperature=" + _temperature);
        builder.append("\ntemperatureUnit=" + _temperatureUnit);
        builder.append("\ntemperatureTrend=" + _temperatureTrend);
        builder.append("\nwindSpeed=" + _windSpeed);
        builder.append("\nwindDirection=" + _windDirection);
        builder.append("\nicon=" + _icon);
        builder.append("\nshortForecast=" + _shortForecast);
        builder.append("\ndetailedForecast=" + _detailedForecast);
        return builder.toString();
    }
}
