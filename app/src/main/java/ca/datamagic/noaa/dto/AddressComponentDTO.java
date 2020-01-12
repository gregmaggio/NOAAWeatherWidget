package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class AddressComponentDTO {
    private String _longName = null;
    private String _shortName = null;
    private String[] _types = null;

    public AddressComponentDTO() {

    }

    public AddressComponentDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("long_name") == 0) {
                _longName = obj.getString(key);
            } else if (key.compareToIgnoreCase("short_name") == 0) {
                _shortName = obj.getString(key);
            } else if (key.compareToIgnoreCase("types") == 0) {
                JSONArray types = obj.getJSONArray(key);
                _types = new String[types.length()];
                for (int ii = 0; ii < types.length(); ii++) {
                    _types[ii] = types.get(ii).toString();
                }
            }
        }
    }

    public String getLongName() {
        return _longName;
    }

    public String getShortName() {
        return _shortName;
    }

    public String[] getTypes() {
        return _types;
    }

    public void setLongName(String newVal) {
        _longName = newVal;
    }

    public void setShortName(String newVal) {
        _shortName = newVal;
    }

    public void setTypes(String[] newVal) {
        _types = newVal;
    }

    public boolean isCity() {
        if (_types != null) {
            for (int ii = 0; ii < _types.length; ii++) {
                if (_types[ii].compareToIgnoreCase("locality") == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isState() {
        if (_types != null) {
            for (int ii = 0; ii < _types.length; ii++) {
                if (_types[ii].compareToIgnoreCase("administrative_area_level_1") == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
