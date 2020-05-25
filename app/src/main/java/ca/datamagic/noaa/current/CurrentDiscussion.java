package ca.datamagic.noaa.current;

public class CurrentDiscussion {
    private static String _discussion = null;

    public static synchronized String getDiscussion() {
        return _discussion;
    }

    public static synchronized void setDiscussion(String newVal) {
        _discussion = newVal;
    }
}
