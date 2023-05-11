package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class RadarSiteInfoDTO {
    private String crs = null;
    private double[] lowerCorner = null;
    private double[] upperCorner = null;
    private Integer width = null;
    private Integer height = null;

    public RadarSiteInfoDTO() {

    }

    public RadarSiteInfoDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!obj.isNull(key)) {
                if (key.compareToIgnoreCase("crs") == 0) {
                    this.crs = obj.getString(key);
                } else if (key.compareToIgnoreCase("lowerCorner") == 0) {
                    JSONArray jsonArray = obj.getJSONArray(key);
                    if ((jsonArray != null) && (jsonArray.length() == 2)) {
                        this.lowerCorner = new double[2];
                        this.lowerCorner[0] = jsonArray.getDouble(0);
                        this.lowerCorner[1] = jsonArray.getDouble(1);
                    }
                } else if (key.compareToIgnoreCase("upperCorner") == 0) {
                    JSONArray jsonArray = obj.getJSONArray(key);
                    if ((jsonArray != null) && (jsonArray.length() == 2)) {
                        this.upperCorner = new double[2];
                        this.upperCorner[0] = jsonArray.getDouble(0);
                        this.upperCorner[1] = jsonArray.getDouble(1);
                    }
                } else if (key.compareToIgnoreCase("width") == 0) {
                    this.width = obj.getInt(key);
                } else if (key.compareToIgnoreCase("height") == 0) {
                    this.height = obj.getInt(key);
                }
            }
        }
    }

    public String getCrs() {
        return this.crs;
    }

    public void setCrs(String newVal) {
        this.crs = newVal;
    }

    public double[] getLowerCorner() {
        return this.lowerCorner;
    }

    public void setLowerCorner(double[] newVal) {
        this.lowerCorner = newVal;
    }

    public double[] getUpperCorner() {
        return this.upperCorner;
    }

    public void setUpperCorner(double[] newVal) {
        this.upperCorner = newVal;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer newVal) {
        this.width = newVal;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer newVal) {
        this.height = newVal;
    }
}
