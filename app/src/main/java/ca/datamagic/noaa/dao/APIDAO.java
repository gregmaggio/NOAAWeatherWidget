package ca.datamagic.noaa.dao;

import org.json.JSONObject;

import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.exception.MarineForecastNotSupportedException;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class APIDAO {
    private static Logger _logger = LogFactory.getLogger(APIDAO.class);

    private static FeatureDTO loadFeature(String urlSpec) {
        _logger.info("urlSpec: " + urlSpec);
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-User", "?1");
            connection.setRequestProperty("Sec-Fetch-Dest", "document");
            connection.connect();
            String responseText = null;
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
            if ((connection.getResponseCode() > 199) && (connection.getResponseCode() < 300)) {
                responseText = IOUtils.readEntireString(connection.getInputStream());
            } else if ((connection.getResponseCode() > 299) && (connection.getResponseCode() < 400)) {
                // check for location header
                String location = connection.getHeaderField("location");
                if ((location != null) && (location.length() > 0)) {
                    return loadFeature("https://api.weather.gov" + location);
                }
                return null;
            } else {
                return null;
            }
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            JSONObject obj = new JSONObject(responseText);
            return new FeatureDTO(obj);
        } catch (Throwable t) {
            String message = t.getMessage();
            //_logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("timeout")) {
                return null;
            }
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

    private static FeatureDTO loadForecast(String urlSpec) {
        _logger.info("urlSpec: " + urlSpec);
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-User", "?1");
            connection.setRequestProperty("Sec-Fetch-Dest", "document");
            connection.connect();
            String responseText = null;
            int responseCode = connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);
            if ((connection.getResponseCode() > 199) && (connection.getResponseCode() < 300)) {
                responseText = IOUtils.readEntireString(connection.getInputStream());
            } else {
                responseText = IOUtils.readEntireString(connection.getErrorStream());
            }
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            JSONObject obj = new JSONObject(responseText);
            return new FeatureDTO(obj);
        } catch (Throwable t) {
            String message = t.getMessage();
            //_logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("timeout")) {
                return null;
            }
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

    public FeatureDTO loadFeature(double latitude, double longitude) {
        return loadFeature(MessageFormat.format("https://api.weather.gov/points/{0},{1}", Double.toString(latitude), Double.toString(longitude)));
    }

    public FeatureDTO loadForecastHourly(FeatureDTO feature) throws MarineForecastNotSupportedException {
        FeatureDTO forecastHourly = loadForecast(feature.getProperties().getForecastHourly());
        if ((forecastHourly != null) && (forecastHourly.getStatus() != null) && (forecastHourly.getStatus() == 404)) {
            if ((forecastHourly.getDetail() != null) && (forecastHourly.getDetail().toLowerCase().contains("marine areas are not yet supported"))) {
                throw new MarineForecastNotSupportedException();
            }
        }
        if ((forecastHourly != null) && (forecastHourly.getStatus() != null) && (forecastHourly.getStatus() == 500)) {
            return null;
        }
        return forecastHourly;
    }

    public FeatureDTO loadForecast(FeatureDTO feature) throws MarineForecastNotSupportedException {
        FeatureDTO forecastDaily = loadForecast(feature.getProperties().getForecast());
        if ((forecastDaily != null) && (forecastDaily.getStatus() != null) && (forecastDaily.getStatus() == 404)) {
            if ((forecastDaily.getDetail() != null) && (forecastDaily.getDetail().toLowerCase().contains("marine areas are not yet supported"))) {
                throw new MarineForecastNotSupportedException();
            }
        }
        if ((forecastDaily != null) && (forecastDaily.getStatus() != null) && (forecastDaily.getStatus() == 500)) {
            return null;
        }
        return forecastDaily;
    }
}
