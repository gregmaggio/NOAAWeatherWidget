/**
 * 
 */
package ca.datamagic.noaa.dto;

/**
 * The ValidTime class represents the start-valid-time and end-valid-time elements in the dwml xml stream.
 * 
 * <start-valid-time>2011-05-30T06:00:00-04:00</start-valid-time>
 * <end-valid-time>2011-05-31T06:00:00-04:00</end-valid-time>
 * 
 * @author greg
 *
 */
public class ValidTimeDTO {
	public static final String PeriodNameAttribute = "period-name";
	private String _timeStamp = null;
	private String _periodName = null;

	public ValidTimeDTO() {

	}

	public ValidTimeDTO(String timeStamp, String periodName) {
		_timeStamp = timeStamp;
		_periodName = periodName;
	}
	
	public String getTimeStamp() {
		return _timeStamp;
	}

	public void setTimeStamp(String newVal) { _timeStamp = newVal; }

	public String getPeriodName() {
		return _periodName;
	}

	public void setPeriodName(String newVal) { _periodName = newVal; }
}
