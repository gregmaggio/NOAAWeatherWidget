package ca.datamagic.noaa.dto;

public class PressureCalculatorDTO {
    public static Double compute(Double input, String inputUnits, String outputUnits, Double elevation, String elevationUnits) {
        Double output = null;
        if ((input != null) && (inputUnits != null) && (outputUnits != null)) {
            if (inputUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                    output = new Double(33.8639 * (input.doubleValue() / 10.0));
                } else if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.StationPressure) == 0) {
                    double INpressure = input.doubleValue();
                    double Mheight = HeightCalculatorDTO.compute(elevation, elevationUnits, HeightUnitsDTO.Meters);
                    output = new Double(INpressure * Math.pow((288 - 0.0065 * Mheight)/288,5.2561));
                }
            } else if (inputUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                    output = new Double(input.doubleValue() * 0.295300);
                } else if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                    output = new Double(input.doubleValue());
                } else if (outputUnits.compareToIgnoreCase(PressureUnitsDTO.StationPressure) == 0) {
                    double INpressure = input.doubleValue() * 0.295300;
                    double Mheight = HeightCalculatorDTO.compute(elevation, elevationUnits, HeightUnitsDTO.Meters);
                    output = new Double(INpressure * Math.pow((288 - 0.0065 * Mheight)/288,5.2561));
                }
            }
        }
        return output;
    }
}
