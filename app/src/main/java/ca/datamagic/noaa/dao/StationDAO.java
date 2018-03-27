package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationDAO {
    private Logger _logger = LogFactory.getLogger(StationDAO.class);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;
    private static String _filesPath = null;

    public static String getFilesPath() {
        return _filesPath;
    }

    public static void setFilesPath(String newVal) {
        _filesPath = newVal;
    }

    public StationDTO nearest(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
        Throwable lastError = null;
        URL url = new URL(MessageFormat.format("http://datamagic.ca/Station/api/{0}/{1}/nearest", Double.toString(latitude), Double.toString(longitude)));
        _logger.info("url: " + url.toString());
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();
                String json = IOUtils.readEntireStream(connection.getInputStream());
                JSONObject obj = new JSONObject(json);
                return new StationDTO(obj);
            } catch (Throwable t) {
                lastError = t;
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
        if (lastError != null)
            throw lastError;
        return null;
    }

    public StationDTO nearestWithRadiosonde(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
        Throwable lastError = null;
        URL url = new URL(MessageFormat.format("http://datamagic.ca/Station/api/{0}/{1}/nearestWithRadiosonde", Double.toString(latitude), Double.toString(longitude)));
        _logger.info("url: " + url.toString());
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();
                String json = IOUtils.readEntireStream(connection.getInputStream());
                JSONObject obj = new JSONObject(json);
                return new StationDTO(obj);
            } catch (Throwable t) {
                lastError = t;
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
        if (lastError != null)
            throw lastError;
        return null;
    }
}
