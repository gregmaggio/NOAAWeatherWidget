package ca.datamagic.noaa.dto;

public class PressureCalculatorDTO {
    public static Double compute(Double input, String inputUnits, String outputUnits) {
        Double output = null;
        if ((input != null) && (inputUnits != null) && (outputUnits != null)) {
            if (inputUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                    output = new Double(33.8639 * (input.doubleValue() / 10.0));
                }
            } else if (inputUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                    output = new Double(input.doubleValue() * 0.295300);
                } else if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                    output = new Double(input.doubleValue());
                }
            }
        }
        return output;
    }
}
