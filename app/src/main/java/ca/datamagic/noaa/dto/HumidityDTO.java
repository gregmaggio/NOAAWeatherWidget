package ca.datamagic.noaa.dto;

/**
 * <humidity type="relative" time-layout="k-p1h-n1-1">
 *     <value>92</value>
 * </humidity>
 * Created by Greg on 1/4/2016.
 */
public class HumidityDTO {
    public static final String NodeName = "humidity";
    public static final String TypeAttribute = "type";
    public static final String TimeLayoutAttribute = "time-layout";
    public static final String ValueNode = "value";
    private String _type = null;
    private String _timeLayout = null;
    private Double _value = null;

    public String getType() {
        return _type;
    }

    public String getTimeLayout() {
        return _timeLayout;
    }

    public Double getValue() {
        return _value;
    }

    public void setType(String newVal) {
        _type = newVal;
    }

    public void setTimeLayout(String newVal) {
        _timeLayout = newVal;
    }

    public void setValue(Double newVal) {
        _value = newVal;
    }
}
