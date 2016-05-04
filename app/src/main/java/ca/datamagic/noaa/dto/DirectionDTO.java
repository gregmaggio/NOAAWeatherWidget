package ca.datamagic.noaa.dto;

/**
 * <direction type="wind" time-layout="k-p1h-n1-1" units="degrees true">
 *     <value>170</value>
 * </direction>
 * Created by Greg on 1/9/2016.
 */
public class DirectionDTO {
    public static final String NodeName = "direction";
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
