/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The ConditionsIcon class represents the conditions-icon element in the dwml xml stream.
 * 
 * <conditions-icon type="forecast-NWS" time-layout="k-p24h-n7-1">
 *   <name>Conditions Icons</name>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/sct.jpg</icon-link>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/few.jpg</icon-link>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/tsra40.jpg</icon-link>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/few.jpg</icon-link>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/scttsra20.jpg</icon-link>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/sct.jpg</icon-link>
 *   <icon-link>http://www.nws.noaa.gov/weather/images/fcicons/scttsra30.jpg</icon-link>
 * </conditions-icon>
 * 
 * @author greg
 *
 */
public class ConditionsIconDTO {
	public static final String NodeName = "conditions-icon";
	public static final String TypeAttribute = "type";
	public static final String TimeLayoutAttribute = "time-layout";
	public static final String NameNode = "name";
	public static final String IconLinkNode = "icon-link";
	private String _type = null;
	private String _timeLayout = null;
	private String _name = null;
	private List<String> _iconLink = new ArrayList<String>();
	
	public String getType() {
		return _type;
	}
	
	public void setType(String newVal) {
		_type = newVal;
	}
	
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
	
	public List<String> getIconLink() {
		return _iconLink;
	}
	
	public void setIconLink(List<String> newVal) {
		_iconLink = newVal;
	}
}
