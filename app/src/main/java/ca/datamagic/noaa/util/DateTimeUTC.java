package ca.datamagic.noaa.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Greg on 1/1/2016.
 */
public class DateTimeUTC {
    private static Pattern _datePattern = null;
    private static SimpleDateFormat _dateFormat = null;
    private static SimpleDateFormat _timeFormat = null;
    private Calendar _calendar = null;
    private Date _date = null;
    private int _year = 0;
    private int _month = 0;
    private int _day = 0;
    private int _hours = 0;
    private int _minutes = 0;
    private int _seconds = 0;
    private int _milliseconds = 0;

    public DateTimeUTC() {
        _calendar = Calendar.getInstance();
        _calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        _date = _calendar.getTime();
        _date = _calendar.getTime();
        _year = _calendar.get(Calendar.YEAR);
        _month = _calendar.get(Calendar.MONTH) + 1;
        _day = _calendar.get(Calendar.DAY_OF_MONTH);
        _hours = _calendar.get(Calendar.HOUR_OF_DAY);
        _minutes = _calendar.get(Calendar.MINUTE);
        _seconds = _calendar.get(Calendar.SECOND);
        _milliseconds = _calendar.get(Calendar.MILLISECOND);
    }

    public DateTimeUTC(Calendar calendar) {
        _calendar = calendar;
        _calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        _date = _calendar.getTime();
        _year = _calendar.get(Calendar.YEAR);
        _month = _calendar.get(Calendar.MONTH) + 1;
        _day = _calendar.get(Calendar.DAY_OF_MONTH);
        _hours = _calendar.get(Calendar.HOUR_OF_DAY);
        _minutes = _calendar.get(Calendar.MINUTE);
        _seconds = _calendar.get(Calendar.SECOND);
        _milliseconds = _calendar.get(Calendar.MILLISECOND);
    }

    public DateTimeUTC(Date date) {
        _calendar = Calendar.getInstance();
        _calendar.setTime(date);
        _calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        _date = _calendar.getTime();
        _year = _calendar.get(Calendar.YEAR);
        _month = _calendar.get(Calendar.MONTH) + 1;
        _day = _calendar.get(Calendar.DAY_OF_MONTH);
        _hours = _calendar.get(Calendar.HOUR_OF_DAY);
        _minutes = _calendar.get(Calendar.MINUTE);
        _seconds = _calendar.get(Calendar.SECOND);
        _milliseconds = _calendar.get(Calendar.MILLISECOND);
    }

    public DateTimeUTC(String dataTimeString) throws Exception {
        Matcher dateMatcher = getDatePattern().matcher(dataTimeString);
        if (!dateMatcher.matches()) {
            throw new Exception(MessageFormat.format("The string '{0}' does not match the pattern 'yyyy-MM-ddTHH:mm:ssZ'.", new Object[]{dataTimeString}));
        }
        _calendar = Calendar.getInstance();
        _calendar.set(Calendar.YEAR, Integer.parseInt(dateMatcher.group(1)));
        _calendar.set(Calendar.MONTH, Integer.parseInt(dateMatcher.group(2)) - 1);
        _calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateMatcher.group(3)));
        _calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateMatcher.group(4)));
        _calendar.set(Calendar.MINUTE, Integer.parseInt(dateMatcher.group(5)));
        _calendar.set(Calendar.SECOND, Integer.parseInt(dateMatcher.group(6)));
        _calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        _date = _calendar.getTime();
        _year = _calendar.get(Calendar.YEAR);
        _month = _calendar.get(Calendar.MONTH) + 1;
        _day = _calendar.get(Calendar.DAY_OF_MONTH);
        _hours = _calendar.get(Calendar.HOUR_OF_DAY);
        _minutes = _calendar.get(Calendar.MINUTE);
        _seconds = _calendar.get(Calendar.SECOND);
        _milliseconds = _calendar.get(Calendar.MILLISECOND);
    }

    private static synchronized Pattern getDatePattern() {
        if (_datePattern ==  null) {
            _datePattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})Z", Pattern.CASE_INSENSITIVE);
        }
        return _datePattern;
    }

    private static synchronized SimpleDateFormat getDateFormat() {
        if (_dateFormat == null) {
            _dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            _dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return _dateFormat;
    }

    private static synchronized SimpleDateFormat getTimeFormat() {
        if (_timeFormat == null) {
            _timeFormat = new SimpleDateFormat("HH:mm:ss");
            _timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return _timeFormat;
    }

    public static String now() {
        return (new DateTimeUTC()).toString();
    }

    public Calendar getCalendar() {
        return _calendar;
    }

    public Date getDate() {
        return _date;
    }

    public int getYear() {
        return _year;
    }

    public int getMonth() {
        return _month;
    }

    public int getDay() {
        return _day;
    }

    public int getHours() {
        return _hours;
    }

    public int getMinutes() {
        return _minutes;
    }

    public int getSeconds() {
        return _seconds;
    }

    public int getMilliseconds() {
        return _milliseconds;
    }

    @Override
    public String toString() {
        return getDateFormat().format(_date.getTime()) + "T" + getTimeFormat().format(_date.getTime()) + "Z";
    }
}
