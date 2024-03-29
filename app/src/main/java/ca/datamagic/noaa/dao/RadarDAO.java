package ca.datamagic.noaa.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import org.beyka.tiffbitmapfactory.TiffBitmapFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.widget.MainActivity;

public class RadarDAO {
    private static final Logger _logger = LogFactory.getLogger(RadarDAO.class);
    private static final String RADAR_DATA_URL = "https://mrms.ncep.noaa.gov/data";

    public String[] loadUrls(String icao) throws IOException {
        List<String> urls = new ArrayList<>();
        String baseUrl = MessageFormat.format("{0}/RIDGEII/L3/{1}/SR_BREF/", RADAR_DATA_URL, icao);
        /*
        BDHC
        BDSA
        BOHA
        SR_BREF
        SR_BVEL
         */
        Document document = Jsoup.parse(new URL(baseUrl), 10000);
        Elements anchors = document.getElementsByTag("a");
        if (anchors != null) {
            for (int ii = 0; ii < anchors.size(); ii++) {
                Element anchor = anchors.get(ii);
                String href = anchor.attr("href");
                if ((href != null) && (href.length() > 0)) {
                    if (href.endsWith(".tif.gz")) {
                        urls.add(baseUrl + href);
                    }
                }
            }
        }
        String[] array = new String[urls.size()];
        urls.toArray(array);
        return array;
    }

    public Bitmap loadImage(String imageUrl) {
        _logger.info("imageUrl: " + imageUrl);
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        GZIPInputStream gzipInputStream = null;
        File tempFile = null;
        OutputStream tempStream = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            inputStream = connection.getInputStream();
            gzipInputStream = new GZIPInputStream(inputStream);
            byte[] bytes = IOUtils.readEntireByteArray(gzipInputStream);

            Context context = MainActivity.getThisInstance().getApplicationContext();
            tempFile = File.createTempFile("radar","tif", context.getCacheDir());
            tempStream = new FileOutputStream(tempFile);
            tempStream.write(bytes, 0, bytes.length);
            tempStream.flush();
            tempStream.close();
            tempStream = null;

            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.fromFile(tempFile), "r");
            return TiffBitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFd());
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            return null;
        } finally {
            IOUtils.closeQuietly(tempStream);
            if (tempFile != null) {
                try {
                    tempFile.delete();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
            IOUtils.closeQuietly(gzipInputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(connection);
        }
    }
}
