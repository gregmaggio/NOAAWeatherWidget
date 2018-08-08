package ca.datamagic.noaa.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/2/2016.
 */
public class DiscussionDAO {
    private static Logger _logger = LogFactory.getLogger(DiscussionDAO.class);
    private static String _discussionStart = "<pre class=\"glossaryProduct\">".toLowerCase();
    private static String _discussionEnd = "</pre>".toLowerCase();

    public String load(String issuedBy) throws Throwable {
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=0", issuedBy));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
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
