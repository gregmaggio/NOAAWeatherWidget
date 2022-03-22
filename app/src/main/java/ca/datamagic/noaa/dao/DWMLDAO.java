package ca.datamagic.noaa.dao;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLDAO {
    private static Logger _logger = LogFactory.getLogger(DWMLDAO.class);

    /**
     * Get the current observation at a location
     * @param latitude
     * @param longitude
     * @return
     */
    public DWMLDTO load(double latitude, double longitude, String unit) throws Throwable {
        int intUnit = 0;
        if (unit.compareToIgnoreCase("m") == 0) {
            intUnit = 1;
        }
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/MapClick.php?lat={0}&lon={1}&unit={2}&lg=english&FcstType=dwml", Double.toString(latitude), Double.toString(longitude), Integer.toString(intUnit)));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        HttpsURLConnection redirectConnection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            //connection.setRequestProperty("Connection", "keep-alive");
            //connection.setRequestProperty("Cache-Control", "max-age=0");
            //connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("sec-ch-ua", "Not A;Brand");
            connection.setRequestProperty("sec-ch-ua-mobile", "?1");
            connection.setRequestProperty("sec-ch-ua-platform", "Android");
            connection.setRequestProperty("sec-fetch-dest", "document");
            connection.setRequestProperty("sec-fetch-mode", "navigate");
            connection.setRequestProperty("sec-fetch-site", "none");
            connection.setRequestProperty("sec-fetch-user", "?1");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.connect();
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
            if ((responseCode > 299) && (responseCode < 400)) {
                Iterator<?> it = connection.getHeaderFields().keySet().iterator();
                while (it.hasNext()) {
                    String name = (String) it.next();
                    _logger.info("header: " + name);
                    if (name != null) {
                        String value = connection.getHeaderField(name);
                        _logger.info("value: " + value);
                        if (name.compareToIgnoreCase("Location") == 0) {
                            URL redirectUrl = new URL(value.replace("http://", "https://"));
                            _logger.info("redirectUrl: " + redirectUrl);
                            redirectConnection = (HttpsURLConnection)redirectUrl.openConnection();
                            redirectConnection.setDoInput(true);
                            redirectConnection.setDoOutput(false);
                            redirectConnection.setRequestMethod("GET");
                            redirectConnection.setConnectTimeout(2000);
                            //redirectConnection.setRequestProperty("Connection", "keep-alive");
                            //redirectConnection.setRequestProperty("Cache-Control", "max-age=0");
                            //redirectConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                            redirectConnection.setRequestProperty("sec-ch-ua", "Not A;Brand");
                            redirectConnection.setRequestProperty("sec-ch-ua-mobile", "?1");
                            redirectConnection.setRequestProperty("sec-ch-ua-platform", "Android");
                            redirectConnection.setRequestProperty("sec-fetch-dest", "document");
                            redirectConnection.setRequestProperty("sec-fetch-mode", "navigate");
                            redirectConnection.setRequestProperty("sec-fetch-site", "none");
                            redirectConnection.setRequestProperty("sec-fetch-user", "?1");
                            redirectConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
                            redirectConnection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
                            redirectConnection.connect();
                            responseCode = redirectConnection.getResponseCode();
                            _logger.info("responseCode: " + responseCode);
                            inputStream = redirectConnection.getInputStream();
                        }
                    }
                }
            }
            if (inputStream == null) {
                inputStream = connection.getInputStream();
            }
            String responseText = IOUtils.readEntireString(inputStream);
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            DWMLHandler handler = new DWMLHandler();
            DWMLDTO dwml = handler.parse(responseText);
            return  dwml;
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("failed to connect")) {
                return null;
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
            if (redirectConnection != null) {
                try {
                    redirectConnection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
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
