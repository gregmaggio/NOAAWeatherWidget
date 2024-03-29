package ca.datamagic.noaa.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

/**
 * Created by Greg on 1/2/2016.
 */
public class DiscussionDAO {
    private static final Logger _logger = LogFactory.getLogger(DiscussionDAO.class);
    private static final String _discussionStart = "<pre class=\"glossaryProduct\">".toLowerCase();
    private static final String _discussionEnd = "</pre>".toLowerCase();

    public String load(String issuedBy) throws Throwable {
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=0", issuedBy));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-User", "?1");
            connection.setRequestProperty("Sec-Fetch-Dest", "document");
            connection.connect();
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
            if ((responseCode > 299) && (responseCode < 400)) {
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                Set<String> keys = headerFields.keySet();
                for (String key : keys) {
                    _logger.info("key: " + key);
                    if ((key != null) && (key.compareToIgnoreCase("location") == 0)) {
                        List<String> values = headerFields.get(key);
                        if (values != null) {
                            if (values.size() > 0) {
                                URL newUrl = new URL(values.get(0));
                                connection.disconnect();
                                connection = (HttpsURLConnection) newUrl.openConnection();
                                connection.setDoInput(true);
                                connection.setDoOutput(false);
                                connection.setRequestMethod("GET");
                                connection.setConnectTimeout(5000);
                                connection.connect();
                            }
                        }
                    }
                }
            }
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("timeout")) {
                return null;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(connection);
        }
        return null;
    }
}
