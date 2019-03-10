package ca.datamagic.noaa.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Greg on 4/14/2018.
 */

public class ObservationDTO implements Parcelable {
    private String _description = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private Double _elevation = null;
    private String _elevationUnits = null;
    private Double _temperature = null;
    private String _temperatureUnits = null;
    private Double _dewPoint = null;
    private String _dewPointUnits = null;
    private Double _relativeHumidity = null;
    private String _summary = null;
    private String _iconUrl = null;
    private Double _visibility = null;
    private String _visibilityUnits = null;
    private Double _windDirection = null;
    private String _windDirectionUnits = null;
    private Double _windSpeed = null;
    private String _windSpeedUnits = null;
    private Double _windGust = null;
    private String _windGustUnits = null;
    private Double _pressure = null;
    private String _pressureUnits = null;

    public ObservationDTO() {

    }

    public ObservationDTO(Parcel in) {
        _description = in.readString();
        _latitude = in.readDouble();
        _longitude = in.readDouble();
        _elevation = in.readDouble();
        _elevationUnits = in.readString();
        _temperature = in.readDouble();
        _temperatureUnits = in.readString();
        _dewPoint = in.readDouble();
        _dewPointUnits = in.readString();
        _relativeHumidity = in.readDouble();
        _summary = in.readString();
        _iconUrl = in.readString();
        _visibility = in.readDouble();
        _visibilityUnits = in.readString();
        _windDirection = in.readDouble();
        _windDirectionUnits = in.readString();
        _windSpeed = in.readDouble();
        _windSpeedUnits = in.readString();
        _windGust = in.readDouble();
        _windGustUnits = in.readString();
        _pressure = in.readDouble();
        _pressureUnits = in.readString();
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String newVal) {
        _description = newVal;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }

    public Double getElevation() {
        return _elevation;
    }

    public void setElevation(Double newVal) {
        _elevation = newVal;
    }

    public String getElevationUnits() {
        return _elevationUnits;
    }

    public void setElevationUnits(String newVal) {
        _elevationUnits = newVal;
    }

    public Double getTemperature() {
        return _temperature;
    }

    public void setTemperature(Double newVal) {
        _temperature = newVal;
    }

    public String getTemperatureUnits() {
        return _temperatureUnits;
    }

    public void setTemperatureUnits(String newVal) {
        _temperatureUnits = newVal;
    }

    public Double getDewPoint() {
        return _dewPoint;
    }

    public void setDewPoint(Double newVal) {
        _dewPoint = newVal;
    }

    public String getDewPointUnits() {
        return _dewPointUnits;
    }

    public void setDewPointUnits(String newVal) {
        _dewPointUnits = newVal;
    }

    public Double getRelativeHumidity() {
        return _relativeHumidity;
    }

    public void setRelativeHumidity(Double newVal) {
        _relativeHumidity = newVal;
    }

    public String getSummary() {
        return _summary;
    }

    public void setSummary(String newVal) {
        _summary = newVal;
    }

    public String getIconUrl() {
        return _iconUrl;
    }

    public void setIconUrl(String newVal) {
        _iconUrl = newVal;
    }

    public Double getVisibility() {
        return _visibility;
    }

    public void setVisibility(Double newVal) {
        _visibility = newVal;
    }

    public String getVisibilityUnits() {
        return _visibilityUnits;
    }

    public void setVisibilityUnits(String newVal) {
        _visibilityUnits = newVal;
    }

    public Double getWindDirection() {
        return _windDirection;
    }

    public void setWindDirection(Double newVal) {
        _windDirection = newVal;
    }

    public String getWindDirectionUnits() {
        return _windDirectionUnits;
    }

    public void setWindDirectionUnits(String newVal) {
        _windDirectionUnits = newVal;
    }

    public Double getWindSpeed() {
        return _windSpeed;
    }

    public void setWindSpeed(Double newVal) {
        _windSpeed = newVal;
    }

    public String getWindSpeedUnits() {
        return _windSpeedUnits;
    }

    public void setWindSpeedUnits(String newVal) {
        _windSpeedUnits = newVal;
    }

    public Double getWindGust() {
        return _windGust;
    }

    public void setWindGust(Double newVal) {
        _windGust = newVal;
    }

    public String getWindGustUnits() {
        return _windGustUnits;
    }

    public void setWindGustUnits(String newVal) {
        _windGustUnits = newVal;
    }

    public Double getPressure() {
        return _pressure;
    }

    public void setPressure(Double newVal) {
        _pressure = newVal;
    }

    public String getPressureUnits() {
        return _pressureUnits;
    }

    public void setPressureUnits(String newVal) {
        _pressureUnits = newVal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(((_description == null) ? "" : _description));
        out.writeDouble(((_latitude == null) ? Double.NaN : _latitude));
        out.writeDouble(((_longitude == null) ? Double.NaN : _longitude));
        out.writeDouble(((_elevation == null) ? Double.NaN : _elevation));
        out.writeString(((_elevationUnits == null) ? "" : _elevationUnits));
        out.writeDouble(((_temperature == null) ? Double.NaN : _temperature));
        out.writeString(((_temperatureUnits == null) ? "" : _temperatureUnits));
        out.writeDouble(((_dewPoint == null) ? Double.NaN : _dewPoint));
        out.writeString(((_dewPointUnits == null) ? "" : _dewPointUnits));
        out.writeDouble(((_relativeHumidity == null) ? Double.NaN : _relativeHumidity));
        out.writeString(((_summary == null) ? "" : _summary));
        out.writeString(((_iconUrl == null) ? "" : _iconUrl));
        out.writeDouble(((_visibility == null) ? Double.NaN : _visibility));
        out.writeString(((_visibilityUnits == null) ? "" : _visibilityUnits));
        out.writeDouble(((_windDirection == null) ? Double.NaN : _windDirection));
        out.writeString(((_windDirectionUnits == null) ? "" : _windDirectionUnits));
        out.writeDouble(((_windSpeed == null) ? Double.NaN : _windSpeed));
        out.writeString(((_windSpeedUnits == null) ? "" : _windSpeedUnits));
        out.writeDouble(((_windGust == null) ? Double.NaN : _windGust));
        out.writeString(((_windGustUnits == null) ? "" : _windGustUnits));
        out.writeDouble(((_pressure == null) ? Double.NaN : _pressure));
        out.writeString(((_pressureUnits == null) ? "" : _pressureUnits));
    }

    public static final Parcelable.Creator<ObservationDTO> CREATOR  = new Parcelable.Creator<ObservationDTO>() {
        public ObservationDTO createFromParcel(Parcel in) {
            return new ObservationDTO(in);
        }

        public ObservationDTO[] newArray(int size) {
            return new ObservationDTO[size];
        }
    };
}
