package ca.datamagic.noaa.logging;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Greg on 4/10/2017.
 */

public class LogFormatter extends Formatter {
    private static final TimeZone _utc = TimeZone.getTimeZone("UTC");

    public LogFormatter() {
        super();
    }

    private static String formatTimeInMillis(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.setTimeZone(_utc);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String day = Integer.toString(calendar.get(Calendar.DATE));
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        String second = Integer.toString(calendar.get(Calendar.SECOND));
        if (month.length() < 2)
            month = "0" + month;
        if (day.length() < 2)
            day = "0" + day;
        if (hour.length() < 2)
            hour = "0" + hour;
        if (minute.length() < 2)
            minute = "0" + minute;
        if (second.length() < 2)
            second = "0" + second;
        return MessageFormat.format("{0}-{1}-{2}T{3}:{4}:{5}Z", year, month, day, hour, minute, second);
    }

    private static String formatLevel(Level level) {
        String formatted = "INFO";
        if (level != null) {
            formatted = level.toString().toUpperCase();
        }
        return formatted;
    }

    private static String formatThrowable(Throwable t, int maxStackTrace) {
        StringBuffer buffer = new StringBuffer();
        if ((t.getClass() != null) && (t.getMessage() != null)) {
            buffer.append(t.getClass().getCanonicalName());
            buffer.append(": ");
            buffer.append(t.getMessage());
        }
        if (t.getStackTrace() != null) {
            StackTraceElement[] stackTraceElements = t.getStackTrace();
            for (int ii = 0; ii < stackTraceElements.length; ii++) {
                buffer.append("\r\n");
                if (maxStackTrace > -1) {
                    if (ii > (maxStackTrace - 1)) {
                        buffer.append("\t... " + Integer.toString(stackTraceElements.length - ii) + " more");
                        break;
                    }
                }
                buffer.append("\tat ");
                buffer.append(stackTraceElements[ii].getClassName());
                if (stackTraceElements[ii].getFileName() != null) {
                    buffer.append("(");
                    buffer.append(stackTraceElements[ii].getFileName());
                    buffer.append(":");
                    buffer.append(Integer.toString(stackTraceElements[ii].getLineNumber()));
                    buffer.append(")");
                }
            }
        }
        return buffer.toString();
    }

    @Override
    public String format(LogRecord record) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(formatTimeInMillis(record.getMillis()));
        buffer.append(",");
        buffer.append(formatLevel(record.getLevel()));
        buffer.append(",");
        buffer.append(Integer.toString(record.getThreadID()));
        buffer.append(",");
        if (record.getLoggerName() != null) {
            buffer.append(record.getLoggerName());
        }
        buffer.append(",");
        if (record.getSourceClassName() != null) {
            buffer.append(record.getSourceClassName());
        }
        buffer.append(",");
        if (record.getSourceMethodName() != null) {
            buffer.append(record.getSourceMethodName());
        }
        buffer.append(",");
        if (record.getMessage() != null) {
            buffer.append(record.getMessage());
        }
        if (record.getThrown() != null) {
            buffer.append("\r\n");
            buffer.append(formatThrowable(record.getThrown(), -1));
            if (record.getThrown().getCause() != null)
            {
                buffer.append("\nCaused by: " + formatThrowable(record.getThrown(), 6));
            }
        }
        buffer.append("\r\n");
        return buffer.toString();
    }
}
