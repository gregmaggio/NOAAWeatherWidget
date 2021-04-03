package ca.datamagic.noaa.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/11/2018.
 */

public class IOUtils {
    private static Logger _logger = LogFactory.getLogger(IOUtils.class);
    private static final int BUFFER_SIZE = 4096;

    public static String readEntireString(InputStream inputStream) throws IOException {
        StringBuffer textBuffer = new StringBuffer();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
            textBuffer.append(new String(buffer, 0, bytesRead));
        }
        return textBuffer.toString();
    }

    public static byte[] readEntireByteArray(InputStream inputStream) throws IOException {
        List<Byte> list = new ArrayList<Byte>();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
            for (int ii = 0; ii < bytesRead; ii++) {
                list.add(buffer[ii]);
            }
        }
        buffer = new byte[list.size()];
        for (int ii = 0; ii < list.size(); ii++) {
            buffer[ii] = list.get(ii).byteValue();
        }
        return buffer;
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

    public static void closeQuietly(HttpURLConnection connection) {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Throwable t) {
            _logger.warning(t.getMessage());
        }
    }

    public static void closeQuietly(HttpsURLConnection connection) {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Throwable t) {
            _logger.warning(t.getMessage());
        }
    }
}
