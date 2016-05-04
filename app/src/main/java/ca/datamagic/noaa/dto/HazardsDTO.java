/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Hazards class represents the hazards element in the dwml xml stream.
 * 
 * <hazards time-layout="k-p32h-n2-3">
 *   <name>Watches, Warnings, and Advisories</name>
 *   <hazard-conditions>
 *     <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
 *       <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
 *     </hazard>
 *     <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
 *       <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
 *     </hazard>
 *   </hazard-conditions>
 * </hazards>
 * 
 * @author greg
 *
 */
public class HazardsDTO {
	public static final String NodeName = "hazards";
	public static final String TimeLayoutAttribute = "time-layout";
	public static final String NameNode = "name";
	private String _timeLayout = null;
	private String _name = null;
	private HazardConditionsDTO _hazardConditions = null;
	
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
	
	public HazardConditionsDTO getHazardConditions() {
		return _hazardConditions;
	}
	
	public void setHazardConditions(HazardConditionsDTO newVal) {
		_hazardConditions = newVal;
	}
}
