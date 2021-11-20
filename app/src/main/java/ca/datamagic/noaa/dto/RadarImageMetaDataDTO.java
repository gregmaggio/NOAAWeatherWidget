package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RadarImageMetaDataDTO {
    private String _crs = null;
    private double[] _lowerCorner = null;
    private double[] _upperCorner = null;
    private Double _centerLatitude = null;
    private Double _centerLongitude = null;
    private Integer _width = null;
    private Integer _height = null;

    public RadarImageMetaDataDTO() {

    }

    public RadarImageMetaDataDTO(JSONObject obj) throws JSONException {
        _crs = obj.getString("crs");
        JSONArray lowerCorner = obj.getJSONArray("lowerCorner");
        _lowerCorner = new double[lowerCorner.length()];
        for (int ii = 0; ii < lowerCorner.length(); ii++) {
            _lowerCorner[ii] = lowerCorner.getDouble(ii);
        }
        JSONArray upperCorner = obj.getJSONArray("upperCorner");
        _upperCorner = new double[upperCorner.length()];
        for (int ii = 0; ii < upperCorner.length(); ii++) {
            _upperCorner[ii] = upperCorner.getDouble(ii);
        }
        _centerLatitude = (_lowerCorner[1] + _upperCorner[1]) / 2.0;
        _centerLongitude = (_lowerCorner[0] + _upperCorner[0]) / 2.0;
        _width = obj.getInt("width");
        _height = obj.getInt("height");
    }

    public String getCRS() {
        return _crs;
    }

    public double[] getLowerCorner() {
        return _lowerCorner;
    }

    public double[] getUpperCorner() {
        return _upperCorner;
    }

    public Double getCenterLatitude() {
        return _centerLatitude;
    }

    public Double getCenterLongitude() {
        return _centerLongitude;
    }

    public Integer getWidth() {
        return _width;
    }

    public Integer getHeight() {
        return _height;
    }

    public void setCRS(String newVal) {
        _crs = newVal;
    }

    public void setLowerCorner(double[] newVal) {
        _lowerCorner = newVal;
    }

    public void setUpperCorner(double[] newVal) {
        _upperCorner = newVal;
    }

    public void setCenterLatitude(Double newVal) {
        _centerLatitude = newVal;
    }

    public void setCenterLongitude(Double newVal) {
        _centerLongitude = newVal;
    }

    public void setWidth(Integer newVal) {
        _width = newVal;
    }

    public void setHeight(Integer newVal) {
        _height = newVal;
    }
}
