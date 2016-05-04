package ca.datamagic.noaa.dto;

/**
 * Created by Greg on 1/2/2016.
 */
public class HeightDTO {
    public static final String NodeName = "height";
    public static final String DatumAttribute = "datum";
    public static final String HeightUnitsAttribute = "height-units";
    private String _datum = null;
    private String _heightUnits = null;
    private Double _value = null;

    public String getDatum() {
        return _datum;
    }

    public void setDatum(String newVal) {
        _datum = newVal;
    }

    public String getHeightUnits() {
        return _heightUnits;
    }

    public void setHeightUnits(String newVal) {
        _heightUnits = newVal;
    }

    public Double getValue() {
        return _value;
    }

    public void setValue(Double newVal) {
        _value = newVal;
    }
}
