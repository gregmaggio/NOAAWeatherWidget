package ca.datamagic.noaa.callable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class ImageDownloader implements Callable<Void> {
    private static final Logger _logger = LogFactory.getLogger(ImageDownloader.class);
    private boolean _running = false;
    private String _uri = null;
    private Bitmap _image = null;

    public ImageDownloader(String uri) {
        _uri = uri;
    }

    public Bitmap getImage() {
        return _image;
    }

    @Override
    public Void call() throws Exception {
        _running = true;
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            _logger.info("Downloading " + _uri + "...");
            URL url = new URL(_uri);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
            connection.setRequestProperty("accept-encoding", "gzip, deflate, br");
            connection.setRequestProperty("accept-language", "en-US,en;q=0.9");
            connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
            connection.setRequestProperty("sec-ch-ua-mobile", "?0");
            connection.setRequestProperty("sec-fetch-dest", "image");
            connection.setRequestProperty("sec-fetch-mode", "no-cors");
            connection.setRequestProperty("sec-fetch-site", "cross-site");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.connect();
            inputStream = connection.getInputStream();
            byte[] imageBytes = IOUtils.readEntireByteArray(inputStream);
            _image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            _logger.info("...downloaded");
        } catch (Throwable t) {
            _logger.warning(t.getMessage());
        }
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(connection);
        _running = false;
        return null;
    }
}
