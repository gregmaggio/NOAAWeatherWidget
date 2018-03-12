package ca.datamagic.noaa.util;

import java.io.OutputStream;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/11/2018.
 */

public class IOUtils {
    private static Logger _logger = LogFactory.getLogger(IOUtils.class);

    public static void closeQuietly(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Throwable t) {
            _logger.warning(t.getMessage());
        }
    }
}
