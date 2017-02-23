/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Hazard class represents the hazard element in the dwml xml stream.
 * 
 * <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
 *   <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
 * </hazard>
 * 
 * @author greg
 *
 */
public class HazardDTO {
	public static final String NodeName = "hazard";
	public static final String HeadlineAttribute = "headline";
	public static final String HazardCodeAttribute = "hazardCode";
	public static final String PhenomenaAttribute = "phenomena";
	public static final String SignificanceAttribute = "significance";
	public static final String HazardTypeAttribute = "hazardType";
	public static final String HazardTextUrlNode = "hazardTextURL";
	private String _headline = null;
	private String _hazardCode = null;
	private String _phenomena = null;
	private String _significance = null;
	private String _hazardType = null;
	private String _hazardTextUrl = null;

	public String getHeadline() {
		return _headline;
	}

	public void setHeadline(String newVal) {
		_headline = newVal;
	}

	public String getHazardCode() {
		return _hazardCode;
	}
	
	public void setHazardCode(String newVal) {
		_hazardCode = newVal;
	}
	
	public String getPhenomena() {
		return _phenomena;
	}
	
	public void setPhenomena(String newVal) {
		_phenomena = newVal;
	}
	
	public String getSignificance() {
		return _significance;
	}
	
	public void setSignificance(String newVal) {
		_significance = newVal;
	}
	
	public String getHazardType() {
		return _hazardType;
	}
	
	public void setHazardType(String newVal) {
		_hazardType = newVal;
	}
	
	public String getHazardTextUrl() {
		return _hazardTextUrl;
	}
	
	public void setHazardTextUrl(String newVal) {
		_hazardTextUrl = newVal;
	}
}
