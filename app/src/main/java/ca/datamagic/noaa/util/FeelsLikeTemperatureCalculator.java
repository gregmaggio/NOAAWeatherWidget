package ca.datamagic.noaa.util;

/**
 * Created by Greg on 1/16/2016.
 */
public final class FeelsLikeTemperatureCalculator {
    public static final Double computeFeelsLikeTemperature(Double temperatureFahrenheit, Double relativeHumidity, Double windSpeed) {
        Double feelsLikeTemperature = null;

        if ((temperatureFahrenheit != null) && !temperatureFahrenheit.isNaN()) {
            if (feelsLikeTemperature == null) {
                feelsLikeTemperature = computeHeatIndexTemperature(temperatureFahrenheit, relativeHumidity);
            }

            if (feelsLikeTemperature == null) {
                feelsLikeTemperature = computeWindChill(temperatureFahrenheit, windSpeed);
            }

            if (feelsLikeTemperature == null) {
                feelsLikeTemperature = new Double(temperatureFahrenheit.doubleValue());
            }
        }

        return feelsLikeTemperature;
    }

    private static final Double computeHeatIndexTemperature(Double temperatureFahrenheit, Double relativeHumidity) {
        Double feelsLikeTemperature = null;
        if ((relativeHumidity != null) && !relativeHumidity.isNaN()) {
            if ((temperatureFahrenheit.doubleValue() >= 80) && (temperatureFahrenheit.doubleValue() < 85)) {
                if ((relativeHumidity.doubleValue() >= 40) && (relativeHumidity.doubleValue() < 50)) {
                    feelsLikeTemperature = new Double(79);
                } else if ((relativeHumidity.doubleValue() >= 50) && (relativeHumidity.doubleValue() < 60)) {
                    feelsLikeTemperature = new Double(80);
                } else if ((relativeHumidity.doubleValue() >= 60) && (relativeHumidity.doubleValue() < 70)) {
                    feelsLikeTemperature = new Double(81);
                } else if ((relativeHumidity.doubleValue() >= 70) && (relativeHumidity.doubleValue() < 80)) {
                    feelsLikeTemperature = new Double(82);
                } else if ((relativeHumidity.doubleValue() >= 80) && (relativeHumidity.doubleValue() < 90)) {
                    feelsLikeTemperature = new Double(84);
                } else if (relativeHumidity.doubleValue() >= 90) {
                    feelsLikeTemperature = new Double(85);
                }
            } else if ((temperatureFahrenheit.doubleValue() >= 85) && (temperatureFahrenheit.doubleValue() < 90)) {
                if ((relativeHumidity.doubleValue() >= 40) && (relativeHumidity.doubleValue() < 50)) {
                    feelsLikeTemperature = new Double(84);
                } else if ((relativeHumidity.doubleValue() >= 50) && (relativeHumidity.doubleValue() < 60)) {
                    feelsLikeTemperature = new Double(86);
                } else if ((relativeHumidity.doubleValue() >= 60) && (relativeHumidity.doubleValue() < 70)) {
                    feelsLikeTemperature = new Double(90);
                } else if ((relativeHumidity.doubleValue() >= 70) && (relativeHumidity.doubleValue() < 80)) {
                    feelsLikeTemperature = new Double(92);
                } else if ((relativeHumidity.doubleValue() >= 80) && (relativeHumidity.doubleValue() < 90)) {
                    feelsLikeTemperature = new Double(96);
                } else if (relativeHumidity.doubleValue() >= 90) {
                    feelsLikeTemperature = new Double(101);
                }
            } else if ((temperatureFahrenheit.doubleValue() >= 90) && (temperatureFahrenheit.doubleValue() < 95)) {
                if ((relativeHumidity.doubleValue() >= 40) && (relativeHumidity.doubleValue() < 50)) {
                    feelsLikeTemperature = new Double(90);
                } else if ((relativeHumidity.doubleValue() >= 50) && (relativeHumidity.doubleValue() < 60)) {
                    feelsLikeTemperature = new Double(94);
                } else if ((relativeHumidity.doubleValue() >= 60) && (relativeHumidity.doubleValue() < 70)) {
                    feelsLikeTemperature = new Double(99);
                } else if ((relativeHumidity.doubleValue() >= 70) && (relativeHumidity.doubleValue() < 80)) {
                    feelsLikeTemperature = new Double(105);
                } else if ((relativeHumidity.doubleValue() >= 80) && (relativeHumidity.doubleValue() < 90)) {
                    feelsLikeTemperature = new Double(113);
                } else if (relativeHumidity.doubleValue() >= 90) {
                    feelsLikeTemperature = new Double(121);
                }
            } else if ((temperatureFahrenheit.doubleValue() >= 95) && (temperatureFahrenheit.doubleValue() < 100)) {
                if ((relativeHumidity.doubleValue() >= 40) && (relativeHumidity.doubleValue() < 50)) {
                    feelsLikeTemperature = new Double(98);
                } else if ((relativeHumidity.doubleValue() >= 50) && (relativeHumidity.doubleValue() < 60)) {
                    feelsLikeTemperature = new Double(105);
                } else if ((relativeHumidity.doubleValue() >= 60) && (relativeHumidity.doubleValue() < 70)) {
                    feelsLikeTemperature = new Double(113);
                } else if ((relativeHumidity.doubleValue() >= 70) && (relativeHumidity.doubleValue() < 80)) {
                    feelsLikeTemperature = new Double(122);
                } else if (relativeHumidity.doubleValue() >= 80) {
                    feelsLikeTemperature = new Double(133);
                }
            } else if ((temperatureFahrenheit.doubleValue() >= 100) && (temperatureFahrenheit.doubleValue() < 105)) {
                if ((relativeHumidity.doubleValue() >= 40) && (relativeHumidity.doubleValue() < 50)) {
                    feelsLikeTemperature = new Double(109);
                } else if ((relativeHumidity.doubleValue() >= 50) && (relativeHumidity.doubleValue() < 60)) {
                    feelsLikeTemperature = new Double(118);
                } else if ((relativeHumidity.doubleValue() >= 60) && (relativeHumidity.doubleValue() < 70)) {
                    feelsLikeTemperature = new Double(129);
                } else if (relativeHumidity.doubleValue() >= 70) {
                    feelsLikeTemperature = new Double(142);
                }
            } else if ((temperatureFahrenheit.doubleValue() >= 105) && (temperatureFahrenheit.doubleValue() < 110)) {
                if ((relativeHumidity.doubleValue() >= 40) && (relativeHumidity.doubleValue() < 50)) {
                    feelsLikeTemperature = new Double(121);
                } else if ((relativeHumidity.doubleValue() >= 50) && (relativeHumidity.doubleValue() < 60)) {
                    feelsLikeTemperature = new Double(133);
                } else if (relativeHumidity.doubleValue() >= 60) {
                    feelsLikeTemperature = new Double(148);
                }
            } else if (temperatureFahrenheit.doubleValue() >= 110) {
                if (relativeHumidity.doubleValue() >= 40) {
                    feelsLikeTemperature = new Double(135);
                }
            }
        }
        return feelsLikeTemperature;
    }

    private static final Double computeWindChill(Double temperatureFahrenheit, Double windSpeed) {
        Double feelsLikeTemperature = null;

        if ((windSpeed != null) && !windSpeed.isNaN()) {
            if (temperatureFahrenheit.doubleValue() <= 40) {
                feelsLikeTemperature = new Double(35.74 + 0.6215 * temperatureFahrenheit.doubleValue() - 35.75 * Math.pow(windSpeed.doubleValue(), 0.16) + 0.4275 * temperatureFahrenheit.doubleValue() * Math.pow(windSpeed.doubleValue(), 0.16));
            }
        }

        return feelsLikeTemperature;
    }
}
