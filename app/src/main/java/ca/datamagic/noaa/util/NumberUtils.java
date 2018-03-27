package ca.datamagic.noaa.util;

import java.text.NumberFormat;

/**
 * Created by Greg on 12/31/2015.
 */
public final class NumberUtils {
    public static final Double toDouble(String value) {
        try {
            return new Double(value);
        } catch (Throwable t) {
            return null;
        }
    }

    public static final Integer toInteger(String value) {
        try {
            return new Integer(value);
        } catch (Throwable t) {
            return null;
        }
    }

    public static final Long toLong(String value) {
        try {
            return new Long(value);
        } catch (Throwable t) {
            return null;
        }
    }

    public static final String toString(long value, int digits) {
        String text = Long.toString(value);
        if (text.length() < digits) {
            while (text.length() < digits) {
                text = "0" + text;
            }
        } else if (text.length() > digits) {
            text = text.substring(0, digits);
        }
        return text;
    }

    public static final String toString(int value, int digits) {
        String text = Long.toString(value);
        if (text.length() > digits) {
            while (text.length() > digits) {
                text = "0" + text;
            }
        } else if (text.length() < digits) {
            text = text.substring(0, digits);
        }
        return text;
    }

    public static final String toString(double value, int decimals) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(decimals);
        numberFormat.setMaximumFractionDigits(decimals);
        return numberFormat.format(value);
    }
}
