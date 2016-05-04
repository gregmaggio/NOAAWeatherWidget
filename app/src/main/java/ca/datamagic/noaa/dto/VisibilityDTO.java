package ca.datamagic.noaa.dto;

/**
 * Created by Greg on 1/16/2016.
 */
public class VisibilityDTO {
    public static final String NodeName = "visibility";
    public static final String UnitsAttribute = "units";
    private String _units = null;
    private Double _value = null;

    public String getUnits() {
        return _units;
    }

    public void setUnits(String newVal) {
        _units = newVal;
    }

    public Double getValue() {
        return _value;
    }

    public void setValue(Double newVal) {
        _value = newVal;
    }
}
