/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The MoreWeatherInformation class represents the moreWeatherInformation element in the dwml xml stream.
 * 
 * <moreWeatherInformation applicable-location="point1">http://forecast.weather.gov/MapClick.php?textField1=39.00&amp;textField2=-77.00</moreWeatherInformation>
 * 
 * @author greg
 *
 */
public class MoreWeatherInformationDTO {
	public static final String NodeName = "moreWeatherInformation";
	public static final String ApplicableLocationAttribute = "applicable-location";
	private String _link = null;
	private String _applicableLocation = null;
	
	public String getLink() {
		return _link;
	}
	
	public void setLink(String newVal) {
		_link = newVal;
	}
	
	public String getApplicableLocation() {
		return _applicableLocation;
	}
	
	public void setApplicableLocation(String newVal) {
		_applicableLocation = newVal;
	}
}
