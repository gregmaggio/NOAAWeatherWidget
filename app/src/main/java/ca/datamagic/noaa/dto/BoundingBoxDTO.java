package ca.datamagic.noaa.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoundingBoxDTO {
    private static Pattern _coordinatesPattern = Pattern.compile("([-+]?[0-9]+\\x2E?[0-9]+),([-+]?[0-9]+\\x2E?[0-9]+) ([-+]?[0-9]+\\x2E?[0-9]+),([-+]?[0-9]+\\x2E?[0-9]+)", Pattern.CASE_INSENSITIVE);
    private Double _latitude1 = null;
    private Double _longitude1 = null;
    private Double _latitude2 = null;
    private Double _longitude2 = null;

    public BoundingBoxDTO() {

    }

    public BoundingBoxDTO(String coordinates) {
        Matcher coordinatesMatcher = _coordinatesPattern.matcher(coordinates);
        if (coordinatesMatcher.matches()) {
            _latitude1 = Double.parseDouble(coordinatesMatcher.group(1));
            _longitude1 = Double.parseDouble(coordinatesMatcher.group(2));
            _latitude2 = Double.parseDouble(coordinatesMatcher.group(3));
            _longitude2 = Double.parseDouble(coordinatesMatcher.group(4));
        }
    }

    public Double getLatitude1() {
        return _latitude1;
    }

    public Double getLongitude1() {
        return _longitude1;
    }

    public Double getLatitude2() {
        return _latitude2;
    }

    public Double getLongitude2() {
        return _longitude2;
    }

    public void setLatitude1(Double newVal) {
        _latitude1 = newVal;
    }

    public void setLongitude1(Double newVal) {
        _longitude1 = newVal;
    }

    public void setLatitude2(Double newVal) {
        _latitude2 = newVal;
    }

    public void setLongitude2(Double newVal) {
        _longitude2 = newVal;
    }
}
