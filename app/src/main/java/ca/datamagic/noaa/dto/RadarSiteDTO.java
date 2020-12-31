package ca.datamagic.noaa.dto;

public class RadarSiteDTO {
    public static final String NodeName = "nws:radar_sites";
    public static final String RadarIdNode = "nws:rda_id";
    public static final String WfoIdNode = "nws:wfo_id";
    public static final String NameNode = "nws:name";
    public static final String LatNode = "nws:lat";
    public static final String LonNode = "nws:lon";
    public static final String ElevationNode = "nws:eqp_elv";
    private String _radarId = null;
    private String _wfoId = null;
    private String _name = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private Double _elevation = null;
    private BoundingBoxDTO _boundingBox = null;

    public String getRadarId() {
        return _radarId;
    }

    public String getWfoId() {
        return _wfoId;
    }

    public String getName() {
        return _name;
    }

    public Double getLatitude() {
        return _latitude;
    }

    public Double getLongitude() {
        return _longitude;
    }

    public Double getElevation() {
        return _elevation;
    }

    public BoundingBoxDTO getBoundingBox() {
        return _boundingBox;
    }

    public void setRadarId(String newVal) {
        _radarId = newVal;
    }

    public void setWfoId(String newVal) {
        _wfoId = newVal;
    }

    public void setName(String newVal) {
        _name = newVal;
    }

    public void setLatitude(Double newVal) {
        _latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        _longitude = newVal;
    }

    public void setElevation(Double newVal) {
        _elevation = newVal;
    }

    public void setBoundingBox(BoundingBoxDTO newVal) {
        _boundingBox = newVal;
    }
}
