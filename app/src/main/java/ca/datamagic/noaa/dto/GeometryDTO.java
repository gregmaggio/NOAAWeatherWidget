package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeometryDTO {
    private String _type = null;
    private Double _latitude = null;
    private Double _longitude = null;

    public GeometryDTO() {

    }

    public GeometryDTO(JSONObject geometryObj) {
        _type = geometryObj.optString("type", "");
        JSONArray geometries = geometryObj.optJSONArray("geometries");
        if (geometries != null) {
            for (int ii = 0; ii < geometries.length(); ii++) {
                JSONObject obj = geometries.optJSONObject(ii);
                if (obj != null) {
                    String geometryType = obj.optString("type", "");
                    if (geometryType.compareToIgnoreCase("Point") == 0) {
                        JSONArray coordinates = obj.optJSONArray("coordinates");
                        if ((coordinates != null) && (coordinates.length() == 2)) {
                            _longitude = coordinates.optDouble(0, 0);
                            _latitude = coordinates.optDouble(1, 0);
                            break;
                        }
                    }
                }
            }
        }
    }

    public String getType() {
        return _type;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public void setType(String newVal) {
        _type = newVal;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }
}
