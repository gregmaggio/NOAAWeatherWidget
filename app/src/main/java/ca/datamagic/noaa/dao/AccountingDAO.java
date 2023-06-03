package ca.datamagic.noaa.dao;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class AccountingDAO {
    private static final Logger _logger = LogFactory.getLogger(AccountingDAO.class);

    public void post(Double deviceLatitude, Double deviceLongitude, String eventName, String eventMessage) {
        HttpsURLConnection connection = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL("https://datamagic.ca/Accounting/api");
            _logger.info("url: " + url.toString());
            JSONObject parameters = new JSONObject();
            if (deviceLatitude != null) {
                parameters.put("deviceLatitude", deviceLatitude.doubleValue());
            }
            if (deviceLongitude != null) {
                parameters.put("deviceLongitude", deviceLongitude.doubleValue());
            }
            if (eventName != null) {
                parameters.put("eventName", eventName);
            }
            if (eventMessage != null) {
                parameters.put("eventMessage", eventMessage);
            }
            String json = parameters.toString();
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(2000);
            connection.connect();
            outputStream = connection.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
            outputStream = null;
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(connection);
        }
    }
}
