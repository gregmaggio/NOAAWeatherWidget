package ca.datamagic.noaa.util;

public class MathUtils {
    public static double frac(double X) {
        X = X - Math.floor(X);
        if (X < 0) {
            X = X + 1.0;
        }
        return X;
    }
}
