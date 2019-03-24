package ca.datamagic.noaa.dto;

import java.util.List;

/**
 * Created by Greg on 4/14/2018.
 */

public class ForecastsDTO {
    private Double _latitude = null;
    private Double _longitude = null;
    private Double _elevation = null;
    private String _elevationUnits = null;
    private List<ForecastDTO> _items = null;

    public ForecastsDTO() {

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

    public List<ForecastDTO> getItems() {
        return _items;
    }

    public void setItems(List<ForecastDTO> newVal) {
        _items = newVal;
    }
}
