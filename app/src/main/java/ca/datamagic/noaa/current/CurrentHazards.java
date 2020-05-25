package ca.datamagic.noaa.current;

import java.util.List;

public class CurrentHazards {
    private static List<String> _hazards = null;

    public static synchronized List<String> getHazards() {
        return _hazards;
    }

    public static synchronized void setHazards(List<String> newVal) {
        _hazards = newVal;
    }
}
