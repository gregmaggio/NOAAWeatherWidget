package ca.datamagic.noaa.logging;

import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by Greg on 4/10/2017.
 */

public class AndroidLogHandler extends Handler {
    private static final String _tag = "NOAAWeatherWidget";

    @Override
    public void close() {

    }

    @Override
    public void flush() {

    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel() != null) {
            if (getLevel().intValue() >= record.getLevel().intValue()) {
                if (record.getLevel() == Level.FINE) {
                    if (record.getThrown() != null) {
                        Log.d(_tag, record.getMessage(), record.getThrown());
                    } else {
                        Log.d(_tag, record.getMessage());
                    }
                } else if (record.getLevel() == Level.FINER) {
                    if (record.getThrown() != null) {
                        Log.d(_tag, record.getMessage(), record.getThrown());
                    } else {
                        Log.d(_tag, record.getMessage());
                    }
                } else if (record.getLevel() == Level.FINEST) {
                    if (record.getThrown() != null) {
                        Log.d(_tag, record.getMessage(), record.getThrown());
                    } else {
                        Log.d(_tag, record.getMessage());
                    }
                } else if (record.getLevel() == Level.WARNING) {
                    if (record.getThrown() != null) {
                        Log.w(_tag, record.getMessage(), record.getThrown());
                    } else {
                        Log.w(_tag, record.getMessage());
                    }
                } else if (record.getLevel() == Level.SEVERE) {
                    if (record.getThrown() != null) {
                        Log.e(_tag, record.getMessage(), record.getThrown());
                    } else {
                        Log.e(_tag, record.getMessage());
                    }
                } else {
                    if (record.getThrown() != null) {
                        Log.d(_tag, record.getMessage(), record.getThrown());
                    } else {
                        Log.d(_tag, record.getMessage());
                    }
                }
            }
        }
    }
}
