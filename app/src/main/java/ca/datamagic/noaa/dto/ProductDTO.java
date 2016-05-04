/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The Product class represents the product element in the dwml xml stream.
 * 
 * <product srsName="WGS 1984" concise-name="dwmlByDay" operational-mode="official">
 *    <title>NOAA's National Weather Service Forecast by 24 Hour Period</title>
 *    <field>meteorological</field>
 *    <category>forecast</category>
 *    <creation-date refresh-frequency="PT1H">2011-05-30T03:18:45Z</creation-date>
 * </product>
 *   
 * @author greg
 *
 */
public class ProductDTO {
	public static final String NodeName = "product";
	public static final String SrsNameAttribute = "srsName";
	public static final String ConciseNameAttribute = "concise-name";
	public static final String OperationalModeAttribute = "operational-mode";
	public static final String TitleNode = "title";
	public static final String FieldNode = "field";
	public static final String CategoryNode = "category";
	private String _srsName = null;
	private String _conciseName = null;
	private String _operationalMode = null;
	private String _title = null;
	private String _field = null;
	private String _category = null;
	private CreationDateDTO _creationDate = null;
	
	public String getSrsName() {
		return _srsName;
	}
	
	public void setSrsName(String newVal) {
		_srsName = newVal;
	}

	public String getConciseName() {
		return _conciseName;
	}
	
	public void setConciseName(String newVal) {
		_conciseName = newVal;
	}
	
	public String getOperationalMode() {
		return _operationalMode;
	}
	
	public void setOperationalMode(String newVal) {
		_operationalMode = newVal;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public void setTitle(String newVal) {
		_title = newVal;
	}
	
	public String getField() {
		return _field;
	}
	
	public void setField(String newVal) {
		_field = newVal;
	}
	
	public String getCategory() {
		return _category;
	}
	
	public void setCategory(String newVal) {
		_category = newVal;
	}
	
	public CreationDateDTO getCreationDate() {
		return _creationDate;
	}
	
	public void setCreationDate(CreationDateDTO newVal) {
		_creationDate = newVal;
	}
}
