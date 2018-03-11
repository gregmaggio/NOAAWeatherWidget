package ca.datamagic.noaa.dao;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLDAO {
    private Logger _logger = LogFactory.getLogger(DWMLDAO.class);
    private static int _maxTries = 5;

    /**
     * Get the current observation at a location
     * @param latitude
     * @param longitude
     * @return
     */
    public DWMLDTO load(double latitude, double longitude, String unit) throws Throwable {
        Throwable lastError = null;
        int intUnit = 0;
        if (unit.compareToIgnoreCase("m") == 0) {
            intUnit = 1;
        }
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/MapClick.php?lat={0}&lon={1}&unit={2}&lg=english&FcstType=dwml", Double.toString(latitude), Double.toString(longitude), Integer.toString(intUnit)));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                connection = (HttpsURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                char[] buffer = new char[255];
                int bytesRead = 0;
                StringBuffer response = new StringBuffer();
                while ((bytesRead = reader.read(buffer, 0, buffer.length)) > 0) {
                    response.append(new String(buffer, 0, bytesRead));
                }
                reader.close();
                _logger.info("responseLength: " + response.length());
                _logger.info("response: " + response.toString());
                DWMLHandler handler = new DWMLHandler();
                return handler.parse(response.toString());
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
