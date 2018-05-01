package ca.datamagic.noaa.dto;

public class WindSpeedCalculatorDTO {
    public static Double compute(Double input, String inputUnits, String outputUnits) {
        Double output = null;
        if ((input != null) && (inputUnits != null) && (outputUnits != null)) {
            if (inputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    output = new Double(input.doubleValue() * 1.15078);
                } else if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    output = new Double(input.doubleValue() * 1.852);
                }
            } else if (inputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    output = new Double(input.doubleValue() * 0.868976);
                } else if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    output = new Double(input.doubleValue() * 1.60934);
                }
            } else if (inputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    output = new Double(input.doubleValue() * 0.539957);
                } else if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    output = new Double(input.doubleValue() * 0.621371);
                } else if (outputUnits.compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    output = new Double(input.doubleValue());
                }
            }
        }
        return output;
    }
}
