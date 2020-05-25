package ca.datamagic.noaa.current;

public class CurrentLocation {
    private static double _latitude = 38.9967;
    private static double _longitude = -76.9275;

    public static synchronized double getLatitude() {
        return _latitude;
    }

    public static synchronized double getLongitude() {
        return _longitude;
    }

    public static synchronized void setLatitude(double newVal) {
        _latitude = newVal;
    }

    public static synchronized void setLongitude(double newVal) {
        _longitude = newVal;
    }
}
