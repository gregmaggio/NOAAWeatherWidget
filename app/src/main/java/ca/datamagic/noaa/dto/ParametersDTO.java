package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The Parameters class represents the location element in the dwml xml stream.
 * 
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
 * 
 * @author greg
 *
 */
public class ParametersDTO {
	public static final String NodeName = "parameters";
	public static final String ApplicableLocationAttribute = "applicable-location";
	private String _applicableLocation = null;
	private List<TemperatureDTO> _temperatures = new ArrayList<TemperatureDTO>();
    private HumidityDTO _humidity = null;
	private ProbabilityOfPrecipitationDTO _probabilityOfPrecipitation = null;
	private WeatherDTO _weather = null;
	private ConditionsIconDTO _conditionsIcon = null;
	private WordedForecastDTO _wordedForecast = null;
	private List<HazardsDTO> _hazards = new ArrayList<HazardsDTO>();
	private DirectionDTO _direction = null;
    private List<WindSpeedDTO> _windSpeeds = new ArrayList<WindSpeedDTO>();
    private PressureDTO _pressure = null;

	public String getApplicableLocation() {
		return _applicableLocation;
	}
	
	public void setApplicableLocation(String newVal) {
		_applicableLocation = newVal;
	}
	
	public List<TemperatureDTO> getTemperatures() {
		return _temperatures;
	}
	
	public void setTemperatures(List<TemperatureDTO> newVal) {
		_temperatures = newVal;
	}

    public HumidityDTO getHumidity() {
        return _humidity;
    }

    public void setHumidity(HumidityDTO newVal) {
        _humidity = newVal;
    }

	public ProbabilityOfPrecipitationDTO getProbabilityOfPrecipitation() {
		return _probabilityOfPrecipitation;
	}
	
	public void setProbabilityOfPrecipitation(ProbabilityOfPrecipitationDTO newVal) {
		_probabilityOfPrecipitation = newVal;
	}
	
	public WeatherDTO getWeather() {
		return _weather;
	}
	
	public void setWeather(WeatherDTO newVal) {
		_weather = newVal;
	}
	
	public ConditionsIconDTO getConditionsIcon() {
		return _conditionsIcon;
	}
	
	public void setConditionsIcon(ConditionsIconDTO newVal) {
		_conditionsIcon = newVal;
	}

	public WordedForecastDTO getWordedForecast() {
		return _wordedForecast;
	}

	public void setWordedForecast(WordedForecastDTO newVal) {
		_wordedForecast = newVal;
	}

	public List<HazardsDTO> getHazards() {
		return _hazards;
	}
	
	public void addHazards(HazardsDTO newVal) {
		_hazards.add(newVal);
	}

    public DirectionDTO getDirection() {
        return _direction;
    }

    public void setDirection(DirectionDTO newVal) {
        _direction = newVal;
    }

    public List<WindSpeedDTO> getWindSpeeds() {
        return _windSpeeds;
    }

    public void setWindSpeeds(List<WindSpeedDTO> newVal) {
        _windSpeeds = newVal;
    }

    public PressureDTO getPressure() {
        return _pressure;
    }

    public void setPressure(PressureDTO newVal) {
        _pressure = newVal;
    }
}
