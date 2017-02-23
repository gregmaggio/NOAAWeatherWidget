package ca.datamagic.noaa.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ca.datamagic.noaa.dto.WFODTO;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 1/3/2016.
 */
public class WFODAO {
    private static Logger _logger = LogManager.getLogger(WFODAO.class);
    private static int _maxTries = 5;

    public List<WFODTO> list() throws Throwable {
        Throwable lastError = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL("http://datamagic.ca/WFO/api/wfo/list");
                if (_logger.isDebugEnabled()) {
                    _logger.debug("url: " + url.toString());
                }

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
                return items;
            } catch (Throwable t) {
                lastError = t;
                _logger.warn("Exception", t);
                ThreadEx.sleep(500);
            }
        }
        if (lastError != null)
            throw lastError;
        return null;
    }

    public List<WFODTO> read(double latitude, double longitude) throws Throwable {
        Throwable lastError = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/wfo/{0}/{1}/coordinates", Double.toString(latitude), Double.toString(longitude)));
                if (_logger.isDebugEnabled()) {
                    _logger.debug("url: " + url.toString());
                }

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
                return items;
            } catch (Throwable t) {
                lastError = t;
                _logger.warn("Exception", t);
                ThreadEx.sleep(500);
            }
        }
        if (lastError != null)
            throw lastError;
        return null;
    }

    public WFODTO read(String id) throws Throwable {
        Throwable lastError = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/wfo/{0}", id));
                if (_logger.isDebugEnabled()) {
                    _logger.debug("url: " + url.toString());
                }

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
                return new WFODTO(obj);
            } catch (Throwable t) {
                lastError = t;
                _logger.warn("Exception", t);
                ThreadEx.sleep(500);
            }
        }
        if (lastError != null)
            throw lastError;
        return null;
    }
}
