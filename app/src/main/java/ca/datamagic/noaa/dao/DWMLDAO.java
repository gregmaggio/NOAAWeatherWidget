package ca.datamagic.noaa.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLDAO {
    private static Logger _logger = LogFactory.getLogger(DWMLDAO.class);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;
    private static String _filesPath = null;

    public static String getFilesPath() {
        return _filesPath;
    }

    public static void setFilesPath(String newVal) {
        _filesPath = newVal;
    }

    /**
     * Get the current observation at a location
     * @param latitude
     * @param longitude
     * @return
     */
    public DWMLDTO load(double latitude, double longitude, String unit) throws Throwable {
        int intUnit = 0;
        if (unit.compareToIgnoreCase("m") == 0) {
            intUnit = 1;
        }
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/MapClick.php?lat={0}&lon={1}&unit={2}&lg=english&FcstType=dwml", Double.toString(latitude), Double.toString(longitude), Integer.toString(intUnit)));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                connection = (HttpsURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                char[] buffer = new char[255];
                int bytesRead = 0;
                StringBuffer response = new StringBuffer();
                while ((bytesRead = reader.read(buffer, 0, buffer.length)) > 0) {
                    response.append(new String(buffer, 0, bytesRead));
                }
                reader.close();
                String responseText = response.toString();
                _logger.info("responseLength: " + responseText.length());
                _logger.info("responseText: " + responseText);
                DWMLHandler handler = new DWMLHandler();
                DWMLDTO dwml = handler.parse(responseText);
                write(responseText);
                return  dwml;
            } catch (Throwable t) {
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(_retryTimeoutMillis);
            } finally {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Throwable t) {
                        _logger.warning("Exception: " + t.getMessage());
                    }
                }
            }
        }
        String lastResponseText = read();
        if ((lastResponseText != null) && (lastResponseText.length() > 0)) {
            DWMLHandler handler = new DWMLHandler();
            DWMLDTO dwml = handler.parse(lastResponseText);
            return dwml;
        }
        return null;
    }

    public static void write(String xml) throws IOException {
        OutputStream output = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return;
            }
            String fileName = MessageFormat.format("{0}/DWML.xml", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            output = new FileOutputStream(file.getAbsolutePath());
            output.write(xml.getBytes());
            output.flush();
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static String read() throws IOException {
        InputStream input = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return null;
            }
            String fileName = MessageFormat.format("{0}/DWML.xml", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                input = new FileInputStream(file.getAbsolutePath());
                int inputBytes = (int)file.length();
                _logger.info("inputBytes: " + Integer.toString(inputBytes));
                byte[] inputBuffer = new byte[inputBytes];
                int bytesRead = input.read(inputBuffer, 0, inputBuffer.length);
                _logger.info("bytesRead: " + Integer.toString(bytesRead));
                if (bytesRead > 0) {
                    return new String(inputBuffer, 0, bytesRead);
                }
            }
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
