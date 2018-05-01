package ca.datamagic.noaa.dto;

public class VisibilityCalculatorDTO {
    public static Double compute(Double input, String inputUnits, String outputUnits) {
        Double output = null;
        if ((input != null) && (inputUnits != null) && (outputUnits != null)) {
            if (inputUnits.compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Miles) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0) {
                    output = new Double(input.doubleValue() * 1.60934);
                }
            } else if (inputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Miles) == 0) {
                if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Miles) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0) {
                    output = new Double(input.doubleValue() * 1.60934);
                }
            } else if (inputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0) {
                if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    output = new Double(input.doubleValue() * 0.621371);
                } else if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Miles) == 0) {
                    output = new Double(input.doubleValue() * 0.621371);
                } else if (outputUnits.compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0) {
                    output = new Double(input.doubleValue());
                }
            }
        }
        return output;
    }
}
