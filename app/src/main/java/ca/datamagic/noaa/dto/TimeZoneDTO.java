package ca.datamagic.noaa.dto;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class TimeZoneDTO implements Parcelable {
    private String _timeZoneId = null;
    private String _timeZoneName = null;
    private String _status = null;
    private Integer _dstOffset = null;
    private Integer _rawOffset = null;

    public TimeZoneDTO() {

    }

    public TimeZoneDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("timeZoneId") == 0) {
                _timeZoneId = obj.getString(key);
            } else if (key.compareToIgnoreCase("timeZoneName") == 0) {
                _timeZoneName = obj.getString(key);
            } else if (key.compareToIgnoreCase("status") == 0) {
                _status = obj.getString(key);
            } else if (key.compareToIgnoreCase("dstOffset") == 0) {
                _dstOffset = obj.getInt(key);
            } else if (key.compareToIgnoreCase("rawOffset") == 0) {
                _rawOffset = obj.getInt(key);
            }
        }
    }

    public TimeZoneDTO(Parcel in) {
        _timeZoneId = in.readString();
        _timeZoneName = in.readString();
        _status = in.readString();
        _dstOffset = in.readInt();
        _rawOffset = in.readInt();
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

    public String getStatus() {
        return _status;
    }

    public void setStatus(String newVal) {
        _status = newVal;
    }

    public Integer getDstOffset() {
        return _dstOffset;
    }

    public void setDstOffset(Integer newVal) {
        _dstOffset = newVal;
    }

    public Integer getRawOffset() {
        return _rawOffset;
    }

    public void setRawOffset(Integer newVal) {
        _rawOffset = newVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(((_timeZoneId == null) ? "" : _timeZoneId));
        out.writeString(((_timeZoneName == null) ? "" : _timeZoneName));
        out.writeString(((_status == null) ? "" : _status));
        out.writeInt(((_dstOffset == null) ? 0 : _dstOffset));
        out.writeInt(((_rawOffset == null) ? 0 : _rawOffset));
    }
}
