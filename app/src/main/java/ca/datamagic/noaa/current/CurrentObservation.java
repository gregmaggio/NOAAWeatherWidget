package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.ObservationDTO;

public class CurrentObservation {
    private static ObservationDTO _obervation = null;

    public static synchronized ObservationDTO getObervation() {
        return _obervation;
    }

    public static synchronized void setObervation(ObservationDTO newVal) {
        _obervation = newVal;
    }
}
