package ca.datamagic.noaa.logging;

import android.util.Log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by Greg on 4/1/2017.
 */

public class AndroidLogWriter extends AppenderSkeleton {
    private static final String _defaultTag = "NOAAWeatherWidget";

    @Override
    protected void append(LoggingEvent event) {
        String tag = event.getLoggerName();
        if ((tag == null) || (tag.length() < 1)) {
            tag = _defaultTag;
        }
        String message = "";
        if (event.getMessage() != null) {
            message = event.getMessage().toString();
        }
        Throwable throwable = null;
        if (event.getThrowableInformation() != null) {
            throwable = event.getThrowableInformation().getThrowable();
        }
        if (event.getLevel() == Level.DEBUG) {
            Log.d(tag, message, throwable);
        } else if (event.getLevel() == Level.INFO) {
            Log.i(tag, message, throwable);
        } else if (event.getLevel() == Level.WARN) {
            Log.w(tag, message, throwable);
        } else if (event.getLevel() == Level.ERROR) {
            Log.e(tag, message, throwable);
        } else if (event.getLevel() == Level.FATAL) {
            Log.wtf(tag, message, throwable);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
