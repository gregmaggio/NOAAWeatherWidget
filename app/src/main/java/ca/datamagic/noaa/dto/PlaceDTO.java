package ca.datamagic.noaa.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Greg on 3/21/2018.
 */

public class PlaceDTO {
    private String _placeId = null;
    private String _name = null;
    private Double _latitude = null;
    private Double _longtitude = null;

    public PlaceDTO() {

    }

    public PlaceDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("place_id") == 0) {
                _placeId = obj.getString(key);
            } else if (key.compareToIgnoreCase("name") == 0) {
                _name = obj.getString(key);
            } else if (key.compareToIgnoreCase("geometry") == 0) {
                JSONObject geometry = obj.getJSONObject(key);
                if (geometry.has("location")) {
                    JSONObject location = geometry.getJSONObject("location");
                    if (location.has("lat") && location.has("lng")) {
                        _latitude = location.getDouble("lat");
                        _longtitude = location.getDouble("lng");
                    }
                }
            }
        }
    }

    public String getPlaceId() {
        return _placeId;
    }

    public void setPlaceId(String newVal) {
        _placeId = newVal;
    }

    public String getName() {
        return _name;
    }

    public void setName(String newVal) {
        _name = newVal;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public Double getLongitude() {
        return _longtitude;
    }

    public void setLongitude(Double newVal) {
        _longtitude = newVal;
    }
}
