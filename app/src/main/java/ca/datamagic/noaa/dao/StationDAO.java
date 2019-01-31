package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationDAO {
    private Logger _logger = LogFactory.getLogger(StationDAO.class);

    public List<StationDTO> list() throws  Throwable {
        HttpURLConnection connection = null;
        URL url = new URL("http://env-5616586.jelastic.servint.net/Station/api/list");
        _logger.info("url: " + url.toString());
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String json = IOUtils.readEntireStream(connection.getInputStream());
            JSONArray array = new JSONArray(json);
            List<StationDTO> list = new ArrayList<StationDTO>();
            for (int jj = 0; jj < array.length(); jj++) {
                list.add(new StationDTO(array.getJSONObject(jj)));
            }
            return list;
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
        }
        return null;
    }

    public StationDTO nearest(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
        URL url = new URL(MessageFormat.format("http://env-5616586.jelastic.servint.net/Station/api/{0}/{1}/nearest", Double.toString(latitude), Double.toString(longitude)));
        _logger.info("url: " + url.toString());
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
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
        }
        return null;
    }

    public StationDTO nearestWithRadiosonde(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
        URL url = new URL(MessageFormat.format("http://env-5616586.jelastic.servint.net/Station/api/{0}/{1}/nearestWithRadiosonde", Double.toString(latitude), Double.toString(longitude)));
        _logger.info("url: " + url.toString());
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
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
        }
        return null;
    }
}
