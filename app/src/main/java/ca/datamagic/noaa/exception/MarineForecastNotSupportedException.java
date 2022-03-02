package ca.datamagic.noaa.exception;

public class MarineForecastNotSupportedException extends Exception {
    public MarineForecastNotSupportedException() {
        super();
    }

    public MarineForecastNotSupportedException(String message) {
        super(message);
    }

    public MarineForecastNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarineForecastNotSupportedException(Throwable cause) {
        super(cause);
    }
}
