package ca.datamagic.noaa.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.datamagic.noaa.logging.LogFactory;

public class TimeStampDTO {
    private static final Logger _logger = LogFactory.getLogger(TimeStampDTO.class);
    private static final Pattern _timeStampPattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})(\\x2B|\\x2D)(\\d+):(\\d+)");
    private TimeZone _timeZone = null;
    private String _timeStamp = null;
    private Calendar _calendar = null;
    private DateFormat _dayOfMonthFormat = null;
    private DateFormat _dayOfWeekFormat = null;
    private DateFormat _12HourOfDayFormat = null;
    private DateFormat _24HourOfDayFormat = null;

    public TimeStampDTO(String timeZoneId) {
        _timeZone = TimeZone.getTimeZone(timeZoneId);
        _dayOfMonthFormat = new SimpleDateFormat("MMM d");
        _dayOfMonthFormat.setTimeZone(_timeZone);
        _dayOfWeekFormat = new SimpleDateFormat("EEEE");
        _dayOfWeekFormat.setTimeZone(_timeZone);
        _12HourOfDayFormat = new SimpleDateFormat("h a");
        _12HourOfDayFormat.setTimeZone(_timeZone);
        _24HourOfDayFormat = new SimpleDateFormat("HH");
        _24HourOfDayFormat.setTimeZone(_timeZone);
    }

    public String getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(String newVal) {
        _timeStamp = newVal;
        if ((_timeStamp != null) && (_timeStamp.length() > 0)) {
            _logger.info("timeStamp: " + _timeStamp);
            Matcher timeStampMatcher = _timeStampPattern.matcher(_timeStamp);
            if (timeStampMatcher.matches()) {
                int year = Integer.parseInt(timeStampMatcher.group(1));
                int month = Integer.parseInt(timeStampMatcher.group(2));
                int day = Integer.parseInt(timeStampMatcher.group(3));
                int hour = Integer.parseInt(timeStampMatcher.group(4));
                int minute = Integer.parseInt(timeStampMatcher.group(5));
                int second = Integer.parseInt(timeStampMatcher.group(6));
                String addSubtract = timeStampMatcher.group(7);
                int hourAdjust = Integer.parseInt(timeStampMatcher.group(8));
                int minuteAdjust = Integer.parseInt(timeStampMatcher.group(9));
                _logger.info("year: " + Integer.toString(year));
                _logger.info("month: " + month);
                _logger.info("day: " + day);
                _logger.info("hour: " + hour);
                _logger.info("minute: " + minute);
                _logger.info("second: " + second);
                _logger.info("addSubtract: " + addSubtract);
                _logger.info("hourAdjust: " + hourAdjust);
                _logger.info("minuteAdjust: " + minuteAdjust);
                Calendar calendar = Calendar.getInstance(_timeZone);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                _calendar = calendar;
            }
        }
    }

    public Calendar getCalendar() {
        return _calendar;
    }

    public String getDayOfMonth() {
        if (_calendar != null) {
            return  _dayOfMonthFormat.format(_calendar.getTime());
        }
        return null;
    }

    public String getDayOfWeek() {
        if (_calendar != null) {
            return _dayOfWeekFormat.format(_calendar.getTime());
        }
        return null;
    }

    public String get12HourOfDay() {
        if (_calendar != null) {
            return _12HourOfDayFormat.format(_calendar.getTime());
        }
        return null;
    }

    public String get24HourOfDay() {
        if (_calendar != null) {
            return _24HourOfDayFormat.format(_calendar.getTime());
        }
        return null;
    }
}
