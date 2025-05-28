package ca.datamagic.noaa.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class TimeZoneDTO {
    private Long _dstOffset = null;
    private Long _rawOffset = null;
    private String _status = null;
    private String _timeZoneId = null;
    private String _timeZoneName = null;
    private String _json = null;

    public TimeZoneDTO() {

    }

    public TimeZoneDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("dstOffset") == 0) {
                _dstOffset = obj.getLong(key);
            } else if (key.compareToIgnoreCase("rawOffset") == 0) {
                _rawOffset = obj.getLong(key);
            } else if (key.compareToIgnoreCase("status") == 0) {
                _status = obj.getString(key);
            } else if (key.compareToIgnoreCase("timeZoneId") == 0) {
                _timeZoneId = obj.getString(key);
            } else if (key.compareToIgnoreCase("timeZoneName") == 0) {
                _timeZoneName = obj.getString(key);
            }
        }
        _json = obj.toString();
    }

    public Long getDstOffset() {
        return _dstOffset;
    }

    public void setDstOffset(Long newVal) {
        _dstOffset = newVal;
    }

    public Long getRawOffset() {
        return _rawOffset;
    }

    public void setRawOffset(Long newVal) {
        _rawOffset = newVal;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String newVal) {
        _status = newVal;
    }

    public String getTimeZoneId() {
        return _timeZoneId;
    }

    public void setTimeZoneId(String newVal) {
        _timeZoneId = newVal;
    }

    public String getTimeZoneName() {
        return _timeZoneName;
    }

    public void setTimeZoneName(String newVal) {
        _timeZoneName = newVal;
    }

    @Override
    public String toString() {
        return  _json;
    }
}
