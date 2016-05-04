/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Point class represents the point element in the dwml xml stream.
 * 
 * <point latitude="39.00" longitude="-77.00"/>
 * 
 * @author greg
 *
 */
public class PointDTO {
	public static final String NodeName = "point";
	public static final String LatitudeAttribute = "latitude";
	public static final String LongitudeAttribute = "longitude";
	private Double _latitude = null;
	private Double _longitude = null;

	public PointDTO() {

	}

	public PointDTO(Double latitude, Double longitude) {
		_latitude = latitude;
		_longitude = longitude;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof PointDTO) {
			PointDTO point = (PointDTO)o;
			if ((_latitude != null) && (point.getLatitude() != null) &&
				(_longitude != null) && (point.getLongitude() != null)) {
				if ((_latitude.doubleValue() == point.getLatitude().doubleValue()) &&
					(_longitude.doubleValue() == point.getLongitude().doubleValue())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result1 = (_latitude != null) ? _latitude.hashCode() : 0;
		int result2 = (_longitude != null) ? _longitude.hashCode() : 0;
		return  (31 * result1) + result2;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if ((_latitude != null) && (_longitude != null)) {
			buffer.append(Double.toString(_latitude.doubleValue()));
			buffer.append(", ");
			buffer.append(Double.toString(_longitude.doubleValue()));
		}
		return buffer.toString();
	}
}
