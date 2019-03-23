package ca.datamagic.noaa.dao;

import org.json.JSONObject;

import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class APIDAO {
    private static Logger _logger = LogFactory.getLogger(APIDAO.class);

    private static FeatureDTO loadFeature(String urlSpec) throws Throwable {
        URL url = new URL(urlSpec);
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String responseText = IOUtils.readEntireStream(connection.getInputStream());
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            JSONObject obj = new JSONObject(responseText);
            return new FeatureDTO(obj);
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

    public FeatureDTO loadFeature(double latitude, double longitude) throws Throwable {
        return loadFeature(MessageFormat.format("https://api.weather.gov/points/{0},{1}", Double.toString(latitude), Double.toString(longitude)));
    }

    public FeatureDTO loadForecastHourly(FeatureDTO feature) throws Throwable {
        return loadFeature(feature.getProperties().getForecastHourly());
    }

    public FeatureDTO loadForecast(FeatureDTO feature) throws Throwable {
        return loadFeature(feature.getProperties().getForecast());
    }
}
