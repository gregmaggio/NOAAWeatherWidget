package ca.datamagic.noaa.dao;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

public class AccountingDAO {
    private static Logger _logger = LogFactory.getLogger(AccountingDAO.class);

    public void post(Double deviceLatitude, Double deviceLongitude, String eventName, String eventMessage) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://datamagic.ca/Accounting/api");
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
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.connect();

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(json);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
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
    }
}
