/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The TimeLayout class represents the time-layout element in the dwml xml stream.
 * 
 * <time-layout time-coordinate="local" summarization="24hourly">
 * 
 * @author greg
 *
 */
public class TimeLayoutDTO {
	public static final String NodeName = "time-layout";
	public static final String TimeCoordinateAttribute = "time-coordinate";
	public static final String SummarizationAttribute = "summarization";
	public static final String LayoutKeyNode = "layout-key";
	public static final String StartValidTimeNode = "start-valid-time";
	private String _timeCoordinate = null;
	private String _summarization = null;
	private String _layoutKey = null;
	private List<ValidTimeDTO> _startTimes = new ArrayList<ValidTimeDTO>();
	
	public String getTimeCoordinate() {
		return _timeCoordinate;
	}
	
	public void setTimeCoordinate(String newVal) {
		_timeCoordinate = newVal;
	}
	
	public String getSummarization() {
		return _summarization;
	}
	
	public void setSummarization(String newVal) {
		_summarization = newVal;
	}
	
	public String getLayoutKey() {
		return _layoutKey;
	}
	
	public void setLayoutKey(String newVal) {
		_layoutKey = newVal;
	}

	public List<ValidTimeDTO> getStartTimes() {
		return _startTimes;
	}
	
	public void setStartTimes(List<ValidTimeDTO> newVal) {
		_startTimes = newVal;
	}
}
