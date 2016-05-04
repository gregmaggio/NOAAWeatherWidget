/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Location class represents the location element in the dwml xml stream.
 * 
 * <location>
 *    <location-key>point1</location-key>
 *    <point latitude="39.00" longitude="-77.00"/>
 * </location>
 * 
 * @author greg
 *
 */
public class LocationDTO {
	public static final String NodeName = "location";
	public static final String LocationKeyNode = "location-key";
	public static final String AreaDescriptionNode = "area-description";
	private String _locationKey = null;
	private PointDTO _point = null;
	private String _areaDescription = null;
	private HeightDTO _height = null;

	public String getLocationKey() {
		return _locationKey;
	}
	
	public void setLocationKey(String newVal) {
		_locationKey = newVal;
	}
	
	public PointDTO getPoint() {
		return _point;
	}
	
	public void setPoint(PointDTO newVal) {
		_point = newVal;
	}

	public String getAreaDescription() {
		return _areaDescription;
	}

	public void setAreaDescription(String newVal) {
		_areaDescription = newVal;
	}

	public HeightDTO getHeight() {
		return _height;
	}

	public void setHeight(HeightDTO newVal) {
		_height = newVal;
	}
}
