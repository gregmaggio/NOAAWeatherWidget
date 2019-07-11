package ca.datamagic.noaa.dao;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class TimeZoneDAO {
    private static Logger _logger = LogFactory.getLogger(TimeZoneDAO.class);

    public String loadTimeZone(double latitude, double longitude) throws MalformedURLException {
        URL url = new URL(MessageFormat.format("https://datamagic.ca/TimeZone/api/{0}/{1}/timeZone", latitude, longitude));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireStream(inputStream);
            _logger.info("responseText: " + responseText);
            String timeZoneId = responseText.trim();
            if (timeZoneId.startsWith("\"")) {
                timeZoneId = timeZoneId.substring(1);
            }
            if (timeZoneId.endsWith("\"")) {
                timeZoneId = timeZoneId.substring(0, timeZoneId.length() - 1);
            }
            return timeZoneId;
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("failed to connect")) {
                return null;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
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
