package ca.datamagic.noaa.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class RadarSiteDTO {
    private String nexradSystem = null;
    private String icao = null;
    private String rdaLocation = null;
    private String responsibleWFO = null;
    private String wfo = null;
    private String equipment = null;
    private Double latitude = null;
    private Double longitude = null;
    private RadarSiteInfoDTO siteInfo = null;

    public RadarSiteDTO() {

    }

    public RadarSiteDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!obj.isNull(key)) {
                if (key.compareToIgnoreCase("nexradSystem") == 0) {
                    this.nexradSystem = obj.getString(key);
                } else if (key.compareToIgnoreCase("icao") == 0) {
                    this.icao = obj.getString(key);
                } else if (key.compareToIgnoreCase("rdaLocation") == 0) {
                    this.rdaLocation = obj.getString(key);
                } else if (key.compareToIgnoreCase("responsibleWFO") == 0) {
                    this.responsibleWFO = obj.getString(key);
                } else if (key.compareToIgnoreCase("wfo") == 0) {
                    this.wfo = obj.getString(key);
                } else if (key.compareToIgnoreCase("equipment") == 0) {
                    this.equipment = obj.getString(key);
                } else if (key.compareToIgnoreCase("latitude") == 0) {
                    this.latitude = obj.getDouble(key);
                } else if (key.compareToIgnoreCase("longitude") == 0) {
                    this.longitude = obj.getDouble(key);
                } else if (key.compareToIgnoreCase("siteInfo") == 0) {
                    this.siteInfo = new RadarSiteInfoDTO(obj.getJSONObject(key));
                }
            }
        }
    }

    public String getNexradSystem() {
        return this.nexradSystem;
    }

    public void setNexradSystem(String newVal) {
        this.nexradSystem = newVal;
    }

    public String getICAO() {
        return this.icao;
    }

    public void setICAO(String newVal) {
        this.icao = newVal;
    }

    public String getRDALocation() {
        return this.rdaLocation;
    }

    public void setRDALocation(String newVal) {
        this.rdaLocation = newVal;
    }

    public String getResponsibleWFO() {
        return this.responsibleWFO;
    }

    public void setResponsibleWFO(String newVal) {
        this.responsibleWFO = newVal;
    }

    public String getWFO() {
        return this.wfo;
    }

    public void setWFO(String newVal) {
        this.wfo = newVal;
    }

    public String getEquipment() {
        return this.equipment;
    }

    public void setEquipment(String newVal) {
        this.equipment = newVal;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double newVal) {
        this.latitude = newVal;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double newVal) {
        this.longitude = newVal;
    }

    public RadarSiteInfoDTO getSiteInfo() {
        return this.siteInfo;
    }

    public void setSiteInfo(RadarSiteInfoDTO newVal) {
        this.siteInfo = newVal;
    }
}
