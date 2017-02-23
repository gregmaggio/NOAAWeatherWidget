package ca.datamagic.noaa.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 1/2/2016.
 */
public class DiscussionDAO {
    private static Logger _logger = LogManager.getLogger(DiscussionDAO.class);
    private static int _maxTries = 5;

    public String load(String issuedBy) throws Throwable {
        Throwable lastError = null;
        URL url = new URL(MessageFormat.format("http://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=0", issuedBy));
        if (_logger.isDebugEnabled()) {
            _logger.debug("url: " + url.toString());
        }
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                boolean started = false;
                while ((currentLine = reader.readLine()) != null) {
                    if (!started) {
                        if (currentLine.trim().compareToIgnoreCase("000") == 0) {
                            started = true;
                        }
                    } else {
                        if (currentLine.trim().compareToIgnoreCase("$$") == 0) {
                            break;
                        }
                        buffer.append(currentLine);
                        buffer.append("\n");
                    }
                }
                String responseText = buffer.toString();
                if ((responseText == null) || (responseText.length() < 1)) {
                    throw new Exception("Discussion was null");
                }
                return responseText;
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
