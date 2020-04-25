package ca.datamagic.noaa.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class FeatureDTO {
    private String _id = null;
    private String _type = null;
    private GeometryDTO _geometry = null;
    private FeaturePropertiesDTO _properties = null;

    public FeatureDTO() {

    }

    public FeatureDTO(JSONObject obj) throws JSONException {
        _id = obj.optString("id", "");
        _type = obj.optString("type", "");
        JSONObject geometryObj = obj.optJSONObject("geometry");
        if (geometryObj != null) {
            _geometry = new GeometryDTO(geometryObj);
        }
        JSONObject propertiesObj = obj.optJSONObject("properties");
        if (propertiesObj != null) {
            _properties = new FeaturePropertiesDTO(propertiesObj);
        }
    }

    public String getId() {
        return _id;
    }

    public String getType() {
        return _type;
    }

    public GeometryDTO getGeometry() {
        return _geometry;
    }

    public FeaturePropertiesDTO getProperties() {
        return _properties;
    }

    public void setId(String newVal) {
        _id = newVal;
    }

    public void setType(String newVal) {
        _type = newVal;
    }

    public void setGeometry(GeometryDTO newVal) {
        _geometry = newVal;
    }

    public void setProperties(FeaturePropertiesDTO newVal) {
        _properties = newVal;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=" + _id);
        builder.append("\ntype=" + _type);
        builder.append("\nproperties\n");
        builder.append(_properties);
        return builder.toString();
    }
}
