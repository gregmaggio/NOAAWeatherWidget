package ca.datamagic.noaa.dto;

/**
 * Created by Greg on 4/8/2018.
 */

public class ForecastDTO {
    private String _timeStamp = null;
    private String _periodName = null;
    private String _summary = null;
    private Double _temperature = null;
    private String _temperatureUnits = null;
    private Double _pop = null;
    private String _imageUrl = null;
    private String _wordedForecast = null;

    public String getTimeStamp() {
        return _timeStamp;
    }

    public String getPeriodName() {
        return _periodName;
    }

    public String getSummary() {
        return _summary;
    }

    public Double getTemperature() {
        return _temperature;
    }

    public String getTemperatureUnits() {
        return _temperatureUnits;
    }

    public Double getPOP() {
        return _pop;
    }

    public String getImageUrl() {
        return _imageUrl;
    }

    public String getWordedForecast() {
        return _wordedForecast;
    }

    public void setTimeStamp(String newVal) {
        _timeStamp = newVal;
    }

    public void setPeriodName(String newVal) {
        _periodName = newVal;
    }

    public void setSummary(String newVal) {
        _summary = newVal;
    }

    public void setTemperature(Double newVal) {
        _temperature = newVal;
    }

    public void setTemperatureUnits(String newVal) {
        _temperatureUnits = newVal;
    }

    public void setPOP(Double newVal) {
        _pop = newVal;
    }

    public void setImageUrl(String newVal) {
        _imageUrl = newVal;
    }

    public void setWordedForecast(String newVal) {
        _wordedForecast = newVal;
    }

    @Override
    public String toString() {
        return "ForecastDTO{" +
                "_timeStamp='" + _timeStamp + '\'' +
                ", _summary='" + _summary + '\'' +
                ", _temperature=" + _temperature +
                ", _temperatureUnits='" + _temperatureUnits + '\'' +
                ", _pop=" + _pop +
                ", _imageUrl='" + _imageUrl + '\'' +
                ", _wordedForecast='" + _wordedForecast + '\'' +
                '}';
    }
}
