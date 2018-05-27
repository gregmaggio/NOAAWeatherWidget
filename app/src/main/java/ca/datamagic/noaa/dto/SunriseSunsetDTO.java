package ca.datamagic.noaa.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class SunriseSunsetDTO implements Parcelable {
    private String _sunrise = null;
    private String _sunset = null;

    public SunriseSunsetDTO() {

    }

    public SunriseSunsetDTO(Parcel in) {
        _sunrise = in.readString();
        _sunset = in.readString();
    }

    public String getSunrise() {
        return _sunrise;
    }

    public void setSunrise(String newVal) {
        _sunrise = newVal;
    }

    public String getSunset() {
        return _sunset;
    }

    public void setSunset(String newVal) {
        _sunset = newVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(((_sunrise == null) ? "" : _sunrise));
        out.writeString(((_sunset == null) ? "" : _sunset));
    }

    public static final Parcelable.Creator<SunriseSunsetDTO> CREATOR  = new Parcelable.Creator<SunriseSunsetDTO>() {
        public SunriseSunsetDTO createFromParcel(Parcel in) {
            return new SunriseSunsetDTO(in);
        }

        public SunriseSunsetDTO[] newArray(int size) {
            return new SunriseSunsetDTO[size];
        }
    };
}
