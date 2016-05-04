package ca.datamagic.noaa.util;

/**
 * Created by Greg on 1/16/2016.
 */
public final class WindDirectionConverter {
    private static final String[] DIRECTIONS = new String[] { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

    public static final String degreesToCompass(Double direction) {
        if ((direction != null) && !direction.isNaN()) {
            double value = Math.floor((direction.doubleValue() / 22.5) + 0.5);
            double index = value % 16.0;
            return  DIRECTIONS[(int)index];
        }
        return  null;
    }

}
