/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Value class represents the value element in the dwml xml stream.
 * 
 * <value coverage="chance" intensity="light" additive="and" weather-type="rain showers" qualifier="none"/>
 * 
 * @author greg
 *
 */
public class ValueDTO {
	public static final String NodeName = "value";
	public static final String CoverageAttribute = "coverage";
	public static final String IntensityAttribute = "intensity";
	public static final String AdditiveAttribute = "additive";
	public static final String WeatherTypeAttribute = "weather-type";
	public static final String QualifierAttribute = "qualifier";
	private String _coverage = null;
	private String _intensity = null;
	private String _additive = null;
	private String _weatherType = null;
	private String _qualifier = null;
	private VisibilityDTO _visibility = null;
	
	public String getCoverage() {
		return _coverage;
	}
	
	public void setCoverage(String newVal) {
		_coverage = newVal;
	}
	
	public String getIntensity() {
		return _intensity;
	}
	
	public void setIntensity(String newVal) {
		_intensity = newVal;
	}
	
	public String getAdditive() {
		return _additive;
	}
	
	public void setAdditive(String newVal) {
		_additive = newVal;
	}
	
	public String getWeatherType() {
		return _weatherType;
	}
	
	public void setWeatherType(String newVal) {
		_weatherType = newVal;
	}
	
	public String getQualifier() {
		return _qualifier;
	}
	
	public void setQualifier(String newVal) {
		_qualifier = newVal;
	}

    public VisibilityDTO getVisibility() {
        return _visibility;
    }

    public void setVisibility(VisibilityDTO newVal) {
        _visibility = newVal;
    }
}
