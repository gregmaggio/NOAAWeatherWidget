package ca.datamagic.noaa.current;

import android.content.Context;

public class CurrentContext {
    private static Context _context = null;

    public static synchronized Context getContext() {
        return _context;
    }

    public static synchronized void setContext(Context newVal) {
        _context = newVal;
    }
}
