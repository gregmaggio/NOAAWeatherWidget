package ca.datamagic.noaa.dto;

import org.json.JSONObject;

/**
 * Created by Greg on 1/28/2017.
 */

public class ZipDTO {
    private String _zip = null;
    private String _city = null;
    private String _state = null;

    public ZipDTO() {

    }

    public ZipDTO(JSONObject obj) {
        _zip = obj.optString("zip", "");
        _city = obj.optString("city", "");
        _state = obj.optString("state", "");
    }

    public String getZip() {
        return _zip;
    }

    public void setZip(String newVal) {
        _zip = newVal;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String newVal) {
        _city = newVal;
    }

    public String getState() {
        return _state;
    }

    public void setState(String newVal) {
        _state = newVal;
    }
}
