package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class RadarDAO {
    private static final Logger _logger = LogFactory.getLogger(RadarDAO.class);
    private static final String _radarUrl = "https://radar-dot-api-project-378578942759.ue.r.appspot.com";

    public RadarDTO loadNearest(double latitude, double longitude) {
        HttpsURLConnection connection = null;
        try {
            String urlSpec = MessageFormat.format("{0}/api/{1}/{2}/nearest", _radarUrl, Double.toString(latitude), Double.toString(longitude));
            _logger.info("urlSpec: " + urlSpec);
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            String responseText = IOUtils.readEntireString(connection.getInputStream());
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            JSONObject obj = new JSONObject(responseText);
            return new RadarDTO(obj);
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            return null;
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

    public RadarDTO load(String icao) {
        HttpsURLConnection connection = null;
        try {
            String urlSpec = MessageFormat.format("{0}/api/{1}", _radarUrl, icao);
            _logger.info("urlSpec: " + urlSpec);
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            String responseText = IOUtils.readEntireString(connection.getInputStream());
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            JSONObject obj = new JSONObject(responseText);
            return new RadarDTO(obj);
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            return null;
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

    public String[] loadUrls(String icao) {
        HttpsURLConnection connection = null;
        try {
            String urlSpec = MessageFormat.format("{0}/api/url/{1}", _radarUrl, icao);
            _logger.info("urlSpec: " + urlSpec);
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            String responseText = IOUtils.readEntireString(connection.getInputStream());
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            JSONArray array = new JSONArray(responseText);
            String[] urls = new String[array.length()];
            for (int ii = 0; ii < array.length(); ii++) {
                urls[ii] = array.getString(ii);
            }
            return urls;
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            return null;
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

    public Bitmap loadImage(String imageUrl) {
        HttpsURLConnection connection = null;
        try {
            String urlSpec = MessageFormat.format("{0}/api/image", _radarUrl);
            _logger.info("urlSpec: " + urlSpec);
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(imageUrl.getBytes());
            outputStream.flush();
            outputStream.close();
            byte[] bytes = IOUtils.readEntireByteArray(connection.getInputStream());
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            return null;
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
