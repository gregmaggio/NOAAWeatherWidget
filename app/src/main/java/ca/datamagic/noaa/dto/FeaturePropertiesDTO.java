package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeaturePropertiesDTO {
    private String _forecast = null;
    private String _forecastHourly = null;
    private String _timeZone = null;
    private String _radarStation = null;
    private String _city = null;
    private String _state = null;
    private PeriodDTO[] _periods = null;

    public FeaturePropertiesDTO() {

    }

    public FeaturePropertiesDTO(JSONObject obj) throws JSONException {
        _forecast = obj.optString("forecast", "");
        _forecastHourly = obj.optString("forecastHourly", "");
        _timeZone = obj.optString("timeZone", "");
        _radarStation = obj.optString("radarStation", "");
        JSONArray periodsArray = obj.optJSONArray("periods");
        if (periodsArray != null) {
            _periods = new PeriodDTO[periodsArray.length()];
            for (int ii = 0; ii < periodsArray.length(); ii++) {
                _periods[ii] = new PeriodDTO(periodsArray.getJSONObject(ii));
            }
        }
        JSONObject relativeLocationObj = obj.optJSONObject("relativeLocation");
        if (relativeLocationObj != null) {
            JSONObject propertiesObj = relativeLocationObj.optJSONObject("properties");
            if (propertiesObj != null) {
                _city = propertiesObj.optString("city", "");
                _state = propertiesObj.optString("state", "");
            }
        }
    }

    public String getForecast() {
        return _forecast;
    }

    public String getForecastHourly() {
        return _forecastHourly;
    }

    public String getTimeZone() {
        return _timeZone;
    }

    public String getRadarStation() {
        return _radarStation;
    }

    public String getCity() {
        return _city;
    }

    public String getState() {
        return _state;
    }

    public void setForecast(String newVal) {
        _forecast = newVal;
    }

    public void setForecastHourly(String newVal) {
        _forecastHourly = newVal;
    }

    public void setTimeZone(String newVal) {
        _timeZone = newVal;
    }

    public void setRadarStation(String newVal) {
        _radarStation = newVal;
    }

    public void setCity(String newVal) {
        _city = newVal;
    }

    public void setState(String newVal) {
        _state = newVal;
    }

    public PeriodDTO[] getPeriods() {
        return _periods;
    }

    public void setPeriods(PeriodDTO[] newVal) {
        _periods = newVal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("forecast=" + _forecast);
        builder.append("\nforecastHourly=" + _forecastHourly);
        if (_periods != null) {
            builder.append("\nperiods\n");
            for (int ii = 0; ii < _periods.length; ii++) {
                builder.append(_periods[ii]);
            }
        }
        return builder.toString();
    }
}
