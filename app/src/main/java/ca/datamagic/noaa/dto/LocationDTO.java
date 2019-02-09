/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Location class represents the location element in the dwml xml stream.
 * 
 *   <location>
 *     <location-key>point1</location-key>
 *     <description>Berwyn Heights, MD</description>
 *     <point latitude="38.99" longitude="-76.92"/>
 *     <city state="MD">Berwyn Heights</city>
 *     <height datum="mean sea level">49</height>
 *   </location>
 *
 * @author greg
 *
 */
public class LocationDTO {
	public static final String NodeName = "location";
	public static final String LocationKeyNode = "location-key";
	public static final String DescriptionNode = "description";
	public static final String CityNode = "city";
	public static final String StateAttribute = "state";
	private String _locationKey = null;
	private PointDTO _point = null;
	private String _description = null;
	private String _city = null;
	private String _state = null;
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

	public String getDescription() {
		return _description;
	}

	public void setDescription(String newVal) {
		_description = newVal;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String newVal) {
		_city = newVal;
	}

	public String getState() {
		return _state;
	}

	public void setState(String newVal) {
		_state = newVal;
	}

	public HeightDTO getHeight() {
		return _height;
	}

	public void setHeight(HeightDTO newVal) {
		_height = newVal;
	}

	public boolean validate() {
		if (_point == null) {
			return false;
		}
		if ((_point.getLatitude() == null) || (_point.getLongitude() == null)) {
			return false;
		}
		if ((_description == null) || (_description.length() < 1)) {
			return false;
		}
		if (_height == null) {
			return false;
		}
		if ((_height.getValue() == null) || (_height.getHeightUnits() == null) || (_height.getHeightUnits().length() < 1)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Location: ");
		buffer.append("locationKey=" + ((_locationKey != null) ? _locationKey : "null"));
		buffer.append(",description=" + ((_description != null) ? _description : "null"));
		buffer.append(",city=" + ((_city != null) ? _city : "null"));
		buffer.append(",state=" + ((_state != null) ? _state : "null"));
		buffer.append(",point=" + ((_point != null) ? _point : "null"));
		buffer.append(",height=" + ((_height != null) ? _height : "null"));
		return buffer.toString();
	}
}
