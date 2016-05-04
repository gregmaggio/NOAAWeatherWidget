/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The Weather class represents the weather element in the dwml xml stream.
 * 
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
 * 
 * @author greg
 *
 */
public class WeatherDTO {
	public static final String NodeName = "weather";
	public static final String TimeLayoutAttribute = "time-layout";
	public static final String NameNode = "name";
	private String _timeLayout = null;
	private String _name = null;
	private List<WeatherConditionsDTO> _weatherConditions = new ArrayList<WeatherConditionsDTO>();
	
	public String getTimeLayout() {
		return _timeLayout;
	}
	
	public void setTimeLayout(String newVal) {
		_timeLayout = newVal;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String newVal) {
		_name = newVal;
	}
	
	public List<WeatherConditionsDTO> getWeatherConditions() {
		return _weatherConditions;
	}
	
	public void setWeatherConditions(List<WeatherConditionsDTO> newVal) {
		_weatherConditions = newVal;
	}
}
