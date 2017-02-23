package ca.datamagic.noaa.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Greg on 1/29/2017.
 */

public final class ThreadEx {
    private static Logger _logger = LogManager.getLogger(ThreadEx.class);

    public static final void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            _logger.warn("InterruptedException", ex);
        }
    }
}
