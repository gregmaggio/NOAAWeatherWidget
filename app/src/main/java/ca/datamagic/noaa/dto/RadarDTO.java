package ca.datamagic.noaa.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class RadarDTO {
    private String _icao = null;
    private String _wfo = null;
    private String _responsibleWFO = null;
    private String _rdaLocation = null;
    private String _nexradSystem = null;
    private String _equipment = null;
    private Double _latitude = null;
    private Double _longitude = null;

    public RadarDTO() {

    }

    public RadarDTO(JSONObject obj) throws JSONException {
        _icao = obj.getString("icao");
        _wfo = obj.getString("wfo");
        _responsibleWFO = obj.optString("responsibleWFO");
        _rdaLocation = obj.optString("rdaLocation");
        _nexradSystem = obj.optString("nexradSystem");
        _equipment = obj.optString("equipment");
        _latitude = obj.getDouble("latitude");
        _longitude = obj.getDouble("longitude");
    }

    public String getICAO() {
        return _icao;
    }

    public void setICAO(String newVal) {
        _icao = newVal;
    }

    public String getWFO() {
        return _wfo;
    }

    public void setWFO(String newVal) {
        _wfo = newVal;
    }

    public String getResponsibleWFO() {
        return _responsibleWFO;
    }

    public void setResponsibleWFO(String newVal) {
        _responsibleWFO = newVal;
    }

    public String getRDALocation() {
        return _rdaLocation;
    }

    public void setRDALocation(String newVal) {
        _rdaLocation = newVal;
    }

    public String getNexradSystem() {
        return _nexradSystem;
    }

    public void setNexradSystem(String newVal) {
        _nexradSystem = newVal;
    }

    public String getEquipment() {
        return _equipment;
    }

    public void setEquipment(String newVal) {
        _equipment = newVal;
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
}
