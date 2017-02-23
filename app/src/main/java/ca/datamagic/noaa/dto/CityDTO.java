package ca.datamagic.noaa.dto;

import org.json.JSONObject;

/**
 * Created by Greg on 1/28/2017.
 */

public class CityDTO {
    private String _city = null;
    private String _zip = null;

    public CityDTO() {

    }

    public CityDTO(JSONObject obj) {
        _city = obj.optString("city", "");
        _zip = obj.optString("zip", "");
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String newVal) {
        _city = newVal;
    }

    public String getZip() {
        return _zip;
    }

    public void setZip(String newVal) {
        _zip = newVal;
    }
}
