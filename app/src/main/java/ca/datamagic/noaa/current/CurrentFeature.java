package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.FeatureDTO;

public class CurrentFeature {
    private static FeatureDTO _feature = null;

    public static synchronized FeatureDTO getFeature() {
        return _feature;
    }

    public static synchronized void setFeature(FeatureDTO newVal) {
        _feature = newVal;
    }
}
