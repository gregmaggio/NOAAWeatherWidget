/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The WeatherConditions class represents the weather-conditions element in the dwml xml stream.
 * 
 * <weather-conditions weather-summary="Chance Thunderstorms">
 *    <value coverage="chance" intensity="none" weather-type="thunderstorms" qualifier="none"/>
 *    <value coverage="chance" intensity="light" additive="and" weather-type="rain showers" qualifier="none"/>
 * </weather-conditions>
 * 
 * @author greg
 *
 */
public class WeatherConditionsDTO {
	public static final String NodeName = "weather-conditions";
	public static final String WeatherSummaryAttribute = "weather-summary";
	private String _weatherSummary = null;
	private List<ValueDTO> _values = new ArrayList<ValueDTO>();
	
	public String getWeatherSummary() {
		return _weatherSummary;
	}
	
	public void setWeatherSummary(String newVal) {
		_weatherSummary = newVal;
	}
	
	public List<ValueDTO> getValues() {
		return _values;
	}
	
	public void setValues(List<ValueDTO> newVal) {
		_values = newVal;
	}
}
