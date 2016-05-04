/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The HazardConditions class represents the hazard-conditions element in the dwml xml stream.
 * 
 * <hazard-conditions>
 *   <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
 *     <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
 *   </hazard>
 *   <hazard hazardCode="HT.Y" phenomena="Heat" significance="Advisory" hazardType="long duration">
 *     <hazardTextURL>http://forecast.weather.gov/wwamap/wwatxtget.php?cwa=lwx&amp;wwa=Heat%20Advisory</hazardTextURL>
 *   </hazard>
 * </hazard-conditions>
 * 
 * @author greg
 *
 */
public class HazardConditionsDTO {
	public static final String NodeName = "hazard-conditions";
	private List<HazardDTO> _hazards = new ArrayList<HazardDTO>();
	
	public List<HazardDTO> getHazards() {
		return _hazards;
	}
	
	public void setHazards(List<HazardDTO> newVal) {
		_hazards = newVal;
	}
}
