package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.WFODTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 1/3/2016.
 */
public class WFODAO {
    private Logger _logger = LogFactory.getLogger(WFODAO.class);
    private static int _maxTries = 5;

    public List<WFODTO> list() throws Throwable {
        Throwable lastError = null;
        HttpURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL("http://datamagic.ca/WFO/api/list");
                _logger.info("url: " + url.toString());
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                while ((currentLine = reader.readLine()) != null) {
                    buffer.append(currentLine);
                }
                String json = buffer.toString();
                _logger.info("json: " + json);
                JSONArray array = new JSONArray(json);
                ArrayList<WFODTO> items = new ArrayList<WFODTO>();
                for (int jj = 0; jj < array.length(); jj++) {
                    items.add(new WFODTO(array.getJSONObject(jj)));
                }
                connection.disconnect();
                connection = null;
                return items;
            } catch (Throwable t) {
                lastError = t;
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(500);
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

    public List<WFODTO> read(double latitude, double longitude) throws Throwable {
        Throwable lastError = null;
        HttpURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/{0}/{1}/coordinates", Double.toString(latitude), Double.toString(longitude)));
                _logger.info("url: " + url.toString());
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                while ((currentLine = reader.readLine()) != null) {
                    buffer.append(currentLine);
                }
                String json = buffer.toString();
                JSONArray array = new JSONArray(json);
                ArrayList<WFODTO> items = new ArrayList<WFODTO>();
                for (int jj = 0; jj < array.length(); jj++) {
                    items.add(new WFODTO(array.getJSONObject(jj)));
                }
                connection.disconnect();
                connection = null;
                return items;
            } catch (Throwable t) {
                lastError = t;
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(500);
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

    public WFODTO read(String id) throws Throwable {
        Throwable lastError = null;
        HttpURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/{0}", id));
                _logger.info("url: " + url.toString());
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                while ((currentLine = reader.readLine()) != null) {
                    buffer.append(currentLine);
                }
                String json = buffer.toString();
                JSONObject obj = new JSONObject(json);
                connection.disconnect();
                connection = null;
                return new WFODTO(obj);
            } catch (Throwable t) {
                lastError = t;
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(500);
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
