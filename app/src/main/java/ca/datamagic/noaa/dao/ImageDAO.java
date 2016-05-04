package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Greg on 12/31/2015.
 */
public class ImageDAO {
    public Bitmap load(String imageUrl) throws MalformedURLException, IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(imageUrl);
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
    }
}
