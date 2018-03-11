package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 12/31/2015.
 */
public class ImageDAO {
    private Logger _logger = LogFactory.getLogger(ImageDAO.class);
    private static int _maxTries = 5;

    public Bitmap load(String imageUrl) throws Throwable {
        Throwable lastError = null;
        URL url = new URL(imageUrl);
        _logger.info("url: " + url.toString());
        //boolean isSecure = (url.getProtocol().compareToIgnoreCase("https") == 0) ? true : false;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    return BitmapFactory.decodeStream(inputStream);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
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
            } catch (Throwable t) {
                lastError = t;
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(500);
            }
        }
        if (lastError != null)
            throw lastError;
        return null;
    }
}
