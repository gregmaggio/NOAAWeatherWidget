package ca.datamagic.noaa.dao;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.TimeZoneDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class GoogleTimeZoneDAO {
    private static Logger _logger = LogFactory.getLogger(GoogleTimeZoneDAO.class);
    private static String _apiKey = null;

    public static String getApiKey() {
        return _apiKey;
    }

    public static void setApiKey(String newVal) {
        _apiKey = newVal;
    }

    public TimeZoneDTO loadTimeZone(Double latitude, Double longitude, String sessionToken) throws MalformedURLException {
        long timestamp = Instant.now().getEpochSecond();
        URL url = new URL(MessageFormat.format("https://maps.googleapis.com/maps/api/timezone/json?location={0}%2C{1}&timestamp={2}&sessiontoken={3}&key={4}", latitude, longitude, Long.toString(timestamp), sessionToken, _apiKey));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireString(inputStream);
            _logger.info("responseText: " + responseText);
            JSONObject responseObj = new JSONObject(responseText);
            return new TimeZoneDTO(responseObj);
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(connection);
        }
        return null;
    }
}
