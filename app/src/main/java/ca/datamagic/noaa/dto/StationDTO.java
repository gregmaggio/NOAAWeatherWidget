package ca.datamagic.noaa.dto;

/**
 * The Station class represents the station element in the wx_station_index xml stream.
 *
 * <station>
 *	<station_id>TAPA</station_id>
 *	<state>AG</state>
 *	<station_name>Vc Bird Intl Airport Antigua</station_name>
 *	<latitude>17.117</latitude>
 *	<longitude>-61.783</longitude>
 *	<html_url>http://weather.noaa.gov/weather/current/TAPA.html</html_url>
 *	<rss_url>http://weather.gov/xml/current_obs/TAPA.rss</rss_url>
 * 	<xml_url>http://weather.gov/xml/current_obs/TAPA.xml</xml_url>
 * </station>
 *
 *
 * @author greg
 *
 */
public class StationDTO {
    public static final String NodeName = "station";
    public static final String StationIdNode = "station_id";
    public static final String StateNode = "state";
    public static final String StationNameNode = "station_name";
    public static final String LatitudeNode = "latitude";
    public static final String LongitudeNode = "longitude";
    public static final String HtmlUrlNode = "html_url";
    public static final String RssUrlNode = "rss_url";
    public static final String XmlUrlNode = "xml_url";
    private String _stationId = null;
    private String _state = null;
    private String _stationName = null;
    private Double _latitude = null;
    private Double _longitude = null;
    private String _htmlUrl = null;
    private String _rssUrl = null;
    private String _xmlUrl = null;

    public String getStationId() {
        return _stationId;
    }

    public void setStationId(String newVal) {
        _stationId = newVal;
    }

    public String getState() {
        return _state;
    }

    public void setState(String newVal) {
        _state = newVal;
    }

    public String getStationName() {
        return _stationName;
    }

    public void setStationName(String newVal) {
        _stationName = newVal;
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

    public String getHtmlUrl() {
        return _htmlUrl;
    }

    public void setHtmlUrl(String newVal) {
        _htmlUrl = newVal;
    }

    public String getRssUrl() {
        return _rssUrl;
    }

    public void setRssUrl(String newVal) {
        _rssUrl = newVal;
    }

    public String getXmlUrl() {
        return _xmlUrl;
    }

    public void setXmlUrl(String newVal) {
        _xmlUrl = newVal;
    }
}
