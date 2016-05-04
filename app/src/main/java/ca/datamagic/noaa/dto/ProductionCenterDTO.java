/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The ProductionCenter class represents the production-center element in the dwml xml stream.
 * 
 * <production-center>Meteorological Development Laboratory<sub-center>Product Generation Branch</sub-center></production-center>
 * 
 * @author greg
 *
 */
public class ProductionCenterDTO {
	public static final String NodeName = "production-center";
	public static final String SubCenterNode = "sub-center";
	private String _description = null;
	private String _subCenter = null;
	
	public String getDescription() {
		return _description;
	}
	
	public void setDescription(String newVal) {
		_description = newVal;
	}
	
	public String getSubCenter() {
		return _subCenter;
	}
	
	public void setSubCenter(String newVal) {
		_subCenter = newVal;
	}
}
