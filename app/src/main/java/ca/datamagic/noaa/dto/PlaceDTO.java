package ca.datamagic.noaa.dto;

import android.renderscript.ScriptGroup;

import org.json.JSONArray;
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
    private AddressComponentDTO[] _addressComponents = null;
    private String _json = null;

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
            } else if (key.compareToIgnoreCase("address_components") == 0) {
                JSONArray addressComponents = obj.getJSONArray(key);
                _addressComponents = new AddressComponentDTO[addressComponents.length()];
                for (int ii = 0; ii < addressComponents.length(); ii++) {
                    _addressComponents[ii] = new AddressComponentDTO(addressComponents.getJSONObject(ii));
                }
            }
        }
        _json = obj.toString();
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

    public AddressComponentDTO[] getAddressComponents() {
        return _addressComponents;
    }

    public void setAddressComponents(AddressComponentDTO[] newVal) {
        _addressComponents = newVal;
    }

    public String getCity() {
        if (_addressComponents != null) {
            for (int ii = 0; ii < _addressComponents.length; ii++) {
                if (_addressComponents[ii].isCity()) {
                    return _addressComponents[ii].getLongName();
                }
            }
        }
        return null;
    }

    public String getState() {
        if (_addressComponents != null) {
            for (int ii = 0; ii < _addressComponents.length; ii++) {
                if (_addressComponents[ii].isState()) {
                    return _addressComponents[ii].getShortName();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return  _json;
    }
}
