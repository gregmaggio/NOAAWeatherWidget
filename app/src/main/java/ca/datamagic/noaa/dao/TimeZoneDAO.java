package ca.datamagic.noaa.dao;

import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class TimeZoneDAO {
    private static Logger _logger = LogFactory.getLogger(TimeZoneDAO.class);

    public String getTimeZone(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
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
            return timeZone;
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

    private static String parseJSON(String json) throws JSONException {
        if (json.startsWith("\"")) {
            json = json.substring(1);
        }
        if (json.endsWith("\"")) {
            json = json.substring(0, json.length() - 1);
        }
        return  json;
    }
}
