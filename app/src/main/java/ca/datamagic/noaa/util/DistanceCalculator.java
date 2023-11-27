package ca.datamagic.noaa.util;

public class DistanceCalculator {
    private static final double _radiusOfEarthMeters = 6371e3;

    public static double distanceToMeters(double distance, String units) {
        if (units.compareToIgnoreCase("statute miles") == 0) {
            return distance * 1609.34;
        }
        return Double.NaN;
    }

    public static double computeDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        double sinDeltaLatitudeOverTwo = Math.sin(deltaLatitude / 2);
        double sinDeltaLongitudeOverTwo = Math.sin(deltaLongitude / 2);
        double a = sinDeltaLatitudeOverTwo * sinDeltaLatitudeOverTwo +
                Math.cos(latitude1) * Math.cos(latitude2) *
                        sinDeltaLongitudeOverTwo * sinDeltaLongitudeOverTwo;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = _radiusOfEarthMeters * c;
        return distance;
    }
}
