package ca.datamagic.noaa.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 1/2/2016.
 */
public class DiscussionDAO {
    private Logger _logger = LogFactory.getLogger(DiscussionDAO.class);
    private static int _maxTries = 5;
    private static String _discussionStart = "<pre class=\"glossaryProduct\">".toLowerCase();
    private static String _discussionEnd = "</pre>".toLowerCase();

    public String load(String issuedBy) throws Throwable {
        Throwable lastError = null;
        URL url = new URL(MessageFormat.format("http://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=0", issuedBy));
        _logger.info("url: " + url.toString());
        HttpURLConnection connection = null;
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
                boolean started = false;
                while ((currentLine = reader.readLine()) != null) {
                    currentLine = currentLine.trim();
                    if (!started) {
                        if (currentLine.toLowerCase().contains(_discussionStart)) {
                            started = true;
                        }
                    } else {
                        if (currentLine.toLowerCase().contains(_discussionEnd)) {
                            break;
                        }
                        buffer.append(currentLine);
                        buffer.append("\n");
                    }
                }
                String responseText = buffer.toString();
                _logger.info("responseLength: " + responseText.length());
                if ((responseText == null) || (responseText.length() < 1)) {
                    throw new Exception("Discussion was null");
                }
                return responseText;
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
