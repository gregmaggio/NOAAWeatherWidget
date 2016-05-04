/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The Data class represents the data element in the dwml xml stream.
 * 
 * <data>
    <location>
      <location-key>point1</location-key>
      <point latitude="39.00" longitude="-77.00"/>
    </location>
    <moreWeatherInformation applicable-location="point1">http://forecast.weather.gov/MapClick.php?textField1=39.00&amp;textField2=-77.00</moreWeatherInformation>
    <time-layout time-coordinate="local" summarization="24hourly">
      <layout-key>k-p24h-n7-1</layout-key>
      <start-valid-time>2011-05-30T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-31T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-05-31T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-01T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-01T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-02T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-02T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-03T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-03T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-04T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-04T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-05T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-05T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-06T06:00:00-04:00</end-valid-time>
    </time-layout>
    <time-layout time-coordinate="local" summarization="12hourly">
      <layout-key>k-p12h-n14-2</layout-key>
      <start-valid-time>2011-05-29T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-30T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-05-30T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-30T18:00:00-04:00</end-valid-time>
      <start-valid-time>2011-05-30T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-31T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-05-31T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-31T18:00:00-04:00</end-valid-time>
      <start-valid-time>2011-05-31T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-01T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-01T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-01T18:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-01T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-02T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-02T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-02T18:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-02T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-03T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-03T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-03T18:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-03T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-04T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-04T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-04T18:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-04T18:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-05T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-06-05T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-06-05T18:00:00-04:00</end-valid-time>
    </time-layout>
    <time-layout time-coordinate="local" summarization="24hourly">
      <layout-key>k-p32h-n2-3</layout-key>
      <start-valid-time>2011-05-30T12:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-31T06:00:00-04:00</end-valid-time>
      <start-valid-time>2011-05-31T06:00:00-04:00</start-valid-time>
      <end-valid-time>2011-05-31T20:00:00-04:00</end-valid-time>
    </time-layout>
    <parameters applicable-location="point1">
      <temperature type="maximum" units="Fahrenheit" time-layout="k-p24h-n7-1">
        <name>Daily Maximum Temperature</name>
        <value>91</value>
        <value>95</value>
        <value>92</value>
        <value>87</value>
        <value>85</value>
        <value>82</value>
        <value>86</value>
      </temperature>
      <temperature type="minimum" units="Fahrenheit" time-layout="k-p24h-n7-1">
        <name>Daily Minimum Temperature</name>
        <value>72</value>
        <value>74</value>
        <value>73</value>
        <value>67</value>
        <value>64</value>
        <value>64</value>
        <value>65</value>
      </temperature>
      <probability-of-precipitation type="12 hour" units="percent" time-layout="k-p12h-n14-2">
        <name>12 Hourly Probability of Precipitation</name>
        <value>0</value>
        <value>12</value>
        <value>3</value>
        <value>4</value>
        <value>5</value>
        <value>36</value>
        <value>20</value>
        <value>13</value>
        <value>10</value>
        <value>19</value>
        <value>14</value>
        <value>11</value>
        <value>11</value>
        <value>26</value>
      </probability-of-precipitation>
      <weather time-layout="k-p24h-n7-1">
        <name>Weather Type, Coverage, and Intensity</name>
        <weather-conditions weather-summary="Partly Sunny"/>
        <weather-conditions weather-summary="Mostly Sunny"/>
        <weather-conditions weather-summary="Chance Thunderstorms">
          <value coverage="chance" intensity="none" weather-type="thunderstorms" qualifier="none"/>
          <value coverage="chance" intensity="light" additive="and" weather-type="rain showers" qualifier="none"/>
        </weather-conditions>
        <weather-conditions weather-summary="Mostly Sunny"/>
        <weather-conditions weather-summary="Slight Chance Thunderstorms">
          <value coverage="slight chance" intensity="none" weather-type="thunderstorms" qualifier="none"/>
          <value coverage="slight chance" intensity="light" additive="and" weather-type="rain showers" qualifier="none"/>
        </weather-conditions>
        <weather-conditions weather-summary="Partly Sunny"/>
        <weather-conditions weather-summary="Chance Thunderstorms">
          <value coverage="chance" intensity="none" weather-type="thunderstorms" qualifier="none"/>
          <value coverage="chance" intensity="light" additive="and" weather-type="rain showers" qualifier="none"/>
        </weather-conditions>
      </weather>
      <conditions-icon type="forecast-NWS" time-layout="k-p24h-n7-1">
        <name>Conditions Icons</name>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/sct.jpg</icon-link>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/few.jpg</icon-link>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/tsra40.jpg</icon-link>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/few.jpg</icon-link>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/scttsra20.jpg</icon-link>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/sct.jpg</icon-link>
        <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/scttsra30.jpg</icon-link>
      </conditions-icon>
      <hazards time-layout="k-p32h-n2-3">
        <name>Watches, Warnings, and Advisories</name>
        <hazard-conditions>
          <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
            <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
          </hazard>
          <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
            <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
          </hazard>
        </hazard-conditions>
      </hazards>
    </parameters>
  </data>
 *
 * @author greg
 *
 */
public class DataDTO {
	public static final String NodeName = "data";
    public static final String TypeAttribute = "type";
    private String _type = null;
	private LocationDTO _location = null;
	private MoreWeatherInformationDTO _moreWeatherInformation = null;
	private List<TimeLayoutDTO> _timeLayouts = new ArrayList<TimeLayoutDTO>();
	private ParametersDTO _parameters = null;

    public String getType() {
        return _type;
    }

    public void setType(String newVal) {
        _type = newVal;
    }

	public LocationDTO getLocation() {
		return _location;
	}
	
	public void setLocation(LocationDTO newVal) {
		_location = newVal;
	}
	
	public MoreWeatherInformationDTO getMoreWeatherInformation() {
		return _moreWeatherInformation;
	}
	
	public void setMoreWeatherInformation(MoreWeatherInformationDTO newVal) {
		_moreWeatherInformation = newVal;
	}
	
	public List<TimeLayoutDTO> getTimeLayouts() {
		return _timeLayouts;
	}
	
	public void setTimeLayouts(List<TimeLayoutDTO> newVal) {
		_timeLayouts = newVal;
	}
	
	public ParametersDTO getParameters() {
		return _parameters;
	}
	
	public void setParameters(ParametersDTO newVal) {
		_parameters = newVal;
	}
}
