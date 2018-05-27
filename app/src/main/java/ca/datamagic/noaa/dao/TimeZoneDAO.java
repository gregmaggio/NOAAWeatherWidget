package ca.datamagic.noaa.dao;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.util.ThreadEx;

public class TimeZoneDAO {
    private static Logger _logger = LogFactory.getLogger(TimeZoneDAO.class);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;
    private static String _filesPath = null;

    public static String getFilesPath() {
        return _filesPath;
    }

    public static void setFilesPath(String newVal) {
        _filesPath = newVal;
    }

    public String getTimeZone(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL(MessageFormat.format("http://datamagic.ca/TimeZone/api/{0}/{1}/timeZone", Double.toString(latitude), Double.toString(longitude)));
                _logger.info("url: " + url.toString());
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();
                String json = IOUtils.readEntireStream(connection.getInputStream());
                String timeZone = parseJSON(json);
                write(json);
                return timeZone;
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
        String json = read();
        if ((json != null) && (json.length() > 0)) {
            return parseJSON(json);
        }
        return null;
    }

    private static String parseJSON(String json) throws JSONException {
        if (json.startsWith("\"")) {
            json = json.substring(1);
        }
        if (json.endsWith("\"")) {
            json = json.substring(0, json.length() - 1);
        }
        return  json;
    }

    public static void write(String json) throws IOException {
        OutputStream output = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return;
            }
            String fileName = MessageFormat.format("{0}/TimeZone.json", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            output = new FileOutputStream(file.getAbsolutePath());
            output.write(json.getBytes());
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
            String fileName = MessageFormat.format("{0}/TimeZone.json", _filesPath);
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
