/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.Calendar;

/**
 * The CreationDate class represents the creation-date element in the dwml xml stream.
 * 
 * <creation-date refresh-frequency="PT1H">2011-05-30T03:18:45Z</creation-date>
 * 
 * @author greg
 *
 */
public class CreationDateDTO {
	public static final String NodeName = "creation-date";
	public static final String RefreshFrequencyAttribute = "refresh-frequency";
	private String _refreshFrequency = null;
	private Calendar _creationDate = null;

	public String getRefreshFrequency() {
		return _refreshFrequency;
	}
	
	public void setRefreshFrequency(String newVal) {
		_refreshFrequency = newVal;
	}
	
	public Calendar getCreationDate() {
		return _creationDate;
	}

	public void setCreationDate(Calendar newVal) {
		_creationDate = newVal;
	}
}
