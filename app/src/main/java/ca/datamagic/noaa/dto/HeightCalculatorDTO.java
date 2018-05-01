package ca.datamagic.noaa.dto;

public class HeightCalculatorDTO {
    public static Double compute(Double input, String inputUnits, String outputUnits) {
        Double output = null;
        if ((input != null) && (inputUnits != null) && (outputUnits != null)) {
            if (inputUnits.compareToIgnoreCase(HeightUnitsDTO.Feet) == 0) {
                if (outputUnits.compareToIgnoreCase(HeightUnitsDTO.Feet) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(HeightUnitsDTO.Meters) == 0) {
                    output = new Double(input.doubleValue() * 0.3048);
                }
            } else if (inputUnits.compareToIgnoreCase(HeightUnitsDTO.Meters) == 0) {
                if (outputUnits.compareToIgnoreCase(HeightUnitsDTO.Feet) == 0) {
                    output = new Double(input.doubleValue() * 3.28084);
                } else if (outputUnits.compareToIgnoreCase(HeightUnitsDTO.Meters) == 0) {
                    output = new Double(input.doubleValue());
                }
            }
        }
        return output;
    }
}
