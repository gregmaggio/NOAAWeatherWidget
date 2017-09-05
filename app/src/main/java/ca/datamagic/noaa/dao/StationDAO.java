package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.CityDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.dto.ZipDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationDAO {
    private Logger _logger = LogFactory.getLogger(StationDAO.class);
    private static int _maxTries = 5;

    public List<StationDTO> list(String city, String zip) throws Throwable {
        HttpURLConnection connection = null;
        Throwable lastError = null;
        StringBuffer queryString = new StringBuffer();
        if ((city != null) && (city.length() > 0)) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append("city=");
            queryString.append(URLEncoder.encode(city, "UTF-8"));
        }
        if ((zip != null) && (zip.length() > 0)) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append("zip=");
            queryString.append(URLEncoder.encode(zip, "UTF-8"));
        }
        StringBuffer urlSpec = new StringBuffer("http://datamagic.ca/Station/api/list");
        if (queryString.length() > 0) {
            urlSpec.append("?");
            urlSpec.append(queryString.toString());
        }
        URL url = new URL(urlSpec.toString());
        _logger.info("url: " + url.toString());
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
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
                ArrayList<StationDTO> items = new ArrayList<StationDTO>();
                for (int jj = 0; jj < array.length(); jj++) {
                    items.add(new StationDTO(array.getJSONObject(jj)));
                }
                Collections.sort(items);
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

    public List<CityDTO> cities() throws Throwable {
        HttpURLConnection connection = null;
        Throwable lastError = null;
        URL url = new URL("http://datamagic.ca/Station/api/cities");
        _logger.info("url: " + url.toString());
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
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
                ArrayList<CityDTO> items = new ArrayList<CityDTO>();
                for (int jj = 0; jj < array.length(); jj++) {
                    items.add(new CityDTO(array.getJSONObject(jj)));
                }
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

    public List<ZipDTO> zips() throws Throwable {
        HttpURLConnection connection = null;
        Throwable lastError = null;
        URL url = new URL("http://datamagic.ca/Station/api/zips");
        _logger.info("url: " + url.toString());
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
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
                ArrayList<ZipDTO> items = new ArrayList<ZipDTO>();
                for (int jj = 0; jj < array.length(); jj++) {
                    items.add(new ZipDTO(array.getJSONObject(jj)));
                }
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

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                while ((currentLine = reader.readLine()) != null) {
                    buffer.append(currentLine);
                }
                String json = buffer.toString();
                JSONObject obj = new JSONObject(json);
                return new StationDTO(obj);
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

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                while ((currentLine = reader.readLine()) != null) {
                    buffer.append(currentLine);
                }
                String json = buffer.toString();
                JSONObject obj = new JSONObject(json);
                return new StationDTO(obj);
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

    public StationDTO read(String id) throws Throwable {
        HttpURLConnection connection = null;
        Throwable lastError = null;
        URL url = new URL(MessageFormat.format("http://datamagic.ca/Station/api/{0}", id));
        _logger.info("url: " + url.toString());
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
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
                return new StationDTO(obj);
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
