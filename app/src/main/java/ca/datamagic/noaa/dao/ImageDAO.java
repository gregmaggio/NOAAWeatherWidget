package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 12/31/2015.
 */
public class ImageDAO {
    private Logger _logger = LogManager.getLogger(ImageDAO.class);
    private static int _maxTries = 5;

    public Bitmap load(String imageUrl) throws Throwable {
        Throwable lastError = null;
        if (_logger.isDebugEnabled()) {
            _logger.debug("imageUrl: " + imageUrl);;
        }
        URL url = new URL(imageUrl);
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                InputStream inputStream = null;
                try {
                    URLConnection connection = url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    return BitmapFactory.decodeStream(inputStream);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
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
