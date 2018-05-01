package ca.datamagic.noaa.dto;

public class TemperatureCalculatorDTO {
    public static Double compute(Double input, String inputUnits, String outputUnits) {
        Double output = null;
        if ((input != null) && (inputUnits != null) && (outputUnits != null)) {
            if (inputUnits.compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0) {
                if (outputUnits.compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0) {
                    output = new Double((input.doubleValue() - 32.0) * (5.0 / 9.0));
                }
            } else if (inputUnits.compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0) {
                if (outputUnits.compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0) {
                    output = new Double(input.doubleValue() * (9.0 / 5.0) + 32.0);
                } else if (outputUnits.compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0) {
                    output = new Double(input.doubleValue());
                }
            }
        }
        return output;
    }
}
