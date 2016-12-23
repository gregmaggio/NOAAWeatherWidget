package ca.datamagic.noaa.logging;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by Greg on 12/21/2016.
 */
public class LogFormatter extends Formatter {
    private static Object _lockObj = new Object();
    private static SimpleDateFormat _dateFormat = null;
    private static SimpleDateFormat _timeFormat = null;
    private static TimeZone _utcTimeZone = null;
    private static String _header = null;

    static {
        synchronized (_lockObj) {
            _dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            _timeFormat = new SimpleDateFormat("HH:mm:ss");
            _utcTimeZone = TimeZone.getTimeZone("UTC");
            _dateFormat.setTimeZone(_utcTimeZone);
            _timeFormat.setTimeZone(_utcTimeZone);
            _header = "TimeStamp,Level,Logger,Thread,ClassName,MethodName,Message";
        }
    }

    @Override
    public String format(LogRecord r) {
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(r.getMillis());
        String timeStamp = MessageFormat.format("{0}T{1}Z", _dateFormat.format(calendar.getTime()), _timeFormat.format(calendar.getTime()));
        buffer.append(timeStamp);
        buffer.append(",");
        buffer.append(r.getLevel().getName());
        buffer.append(",");
        buffer.append(r.getLoggerName());
        buffer.append(",");
        buffer.append(Integer.toString(r.getThreadID()));
        buffer.append(",");
        buffer.append(r.getSourceClassName());
        buffer.append(",");
        buffer.append(r.getSourceMethodName());
        buffer.append(",");
        buffer.append("\"");
        buffer.append(r.getMessage());
        buffer.append("\"");
        return buffer.toString();
    }

    public static String getHeader() {
        return _header;
    }
}
