/**
 * 
 */
package ca.datamagic.noaa.dto;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static final Pattern _timeStampRegex = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})-(\\d{2}):(\\d{2})");
	private Calendar _timeStamp = null;

	public ValidTimeDTO(String timeStampText) {
		Matcher matcher = _timeStampRegex.matcher(timeStampText);
		if (matcher.matches()) {
			int year = Integer.parseInt(matcher.group(1));
			int month = Integer.parseInt(matcher.group(2));
			int day = Integer.parseInt(matcher.group(3));
			int hour = Integer.parseInt(matcher.group(4));
			int minute = Integer.parseInt(matcher.group(5));
			int second = Integer.parseInt(matcher.group(6));
			int hourAdjust = Integer.parseInt(matcher.group(7));
			int minuteAdjust = Integer.parseInt(matcher.group(8));
			Calendar utc = Calendar.getInstance();
			utc.set(Calendar.YEAR, year);
			utc.set(Calendar.MONTH, month - 1);
			utc.set(Calendar.DATE, day);
			utc.set(Calendar.HOUR_OF_DAY, hour);
			utc.set(Calendar.MINUTE, minute);
			utc.set(Calendar.SECOND, second);
			utc.add(Calendar.HOUR, -1 * hourAdjust);
			utc.add(Calendar.MINUTE, -1 * minuteAdjust);
			_timeStamp = utc;
		}
	}
	
	public Calendar getTimeStamp() {
		return _timeStamp;
	}
}
