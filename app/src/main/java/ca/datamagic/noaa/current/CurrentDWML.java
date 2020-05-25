package ca.datamagic.noaa.current;

import ca.datamagic.noaa.dto.DWMLDTO;

public class CurrentDWML {
    private static DWMLDTO _dwml = null;

    public static synchronized DWMLDTO getDWML() {
        return _dwml;
    }

    public static synchronized void setDWML(DWMLDTO newVal) {
        _dwml = newVal;
    }
}
