package ca.datamagic.noaa.dto;

/**
 * <pressure type="barometer" time-layout="k-p1h-n1-1" units="inches of mercury">
 *     <value>30.14</value>
 * </pressure>
 * Created by Greg on 1/9/2016.
 */
public class PressureDTO {
    public static final String NodeName = "pressure";
    public static final String TypeAttribute = "type";
    public static final String TimeLayoutAttribute = "time-layout";
    public static final String UnitsAttribute = "units";
    public static final String ValueNode = "value";
    private String _type = null;
    private String _timeLayout = null;
    private String _units = null;
    private Double _value = null;

    public String getType() {
        return _type;
    }

    public void setType(String newVal) {
        _type = newVal;
    }

    public String getTimeLayout() {
        return _timeLayout;
    }

    public void setTimeLayout(String newVal) {
        _timeLayout = newVal;
    }

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
