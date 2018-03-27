package ca.datamagic.noaa.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Greg on 3/21/2018.
 */

public class PredictionDTO {
    private String _placeId = null;
    private String _description = null;

    public PredictionDTO() {

    }

    public PredictionDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("place_id") == 0) {
                _placeId = obj.getString(key);
            } else if (key.compareToIgnoreCase("description") == 0) {
                _description = obj.getString(key);
            }
        }
    }

    public String getPlaceId() {
        return _placeId;
    }

    public void setPlaceId(String newVal) {
        _placeId = newVal;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String newVal) {
        _description = newVal;
    }
}
