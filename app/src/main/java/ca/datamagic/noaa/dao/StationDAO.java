package ca.datamagic.noaa.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class StationDAO {
    private static Logger _logger = LogFactory.getLogger(StationDAO.class);

    public StationDTO read(double latitude, double longitude) throws Throwable {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(MessageFormat.format("https://datamagic.ca/Station/api/{0}/{1}/nearest", Double.toString(latitude), Double.toString(longitude)));
            _logger.info("url: " + url.toString());
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String json = IOUtils.readEntireStream(connection.getInputStream());
            StationDTO station = parseJSON(json);
            return station;
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("failed to connect")) {
                return null;
            }
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

    private static StationDTO parseJSON(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        return new StationDTO(obj);
    }
}
