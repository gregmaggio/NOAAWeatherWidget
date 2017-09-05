package ca.datamagic.noaa.util;

/**
 * Created by Greg on 1/29/2017.
 */
public final class ThreadEx {
    public static final void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            // TODO
        }
    }
}
