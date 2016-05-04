package ca.datamagic.noaa.dto;

/**
 * Created by Greg on 1/1/2016.
 */
public class DWMLDTO {
    public static final String NodeName = "dwml";
    public static final String VersionAttribute = "version";
    private String _version = null;
    private HeadDTO _head = null;
    private DataDTO _data = null;

    public String getVersion() {
        return _version;
    }

    public void setVersion(String newVal) {
        _version = newVal;
    }

    public HeadDTO getHead() {
        return _head;
    }

    public void setHead(HeadDTO newVal) {
        _head = newVal;
    }

    public DataDTO getData() {
        return _data;
    }

    public void setData(DataDTO newVal) {
        _data = newVal;
    }
}
