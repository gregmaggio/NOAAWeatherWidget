package ca.datamagic.noaa.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/11/2018.
 */

public class IOUtils {
    private static Logger _logger = LogFactory.getLogger(IOUtils.class);

    public static String readEntireStream(InputStream inputStream) throws IOException {
        StringBuffer textBuffer = new StringBuffer();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            textBuffer.append(new String(buffer, 0, bytesRead));
        }
        return textBuffer.toString();
    }

    public static void closeQuietly(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Throwable t) {
            _logger.warning(t.getMessage());
        }
    }

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
