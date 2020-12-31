package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.ImageDTO;
//import mil.nga.tiff.FileDirectory;
//import mil.nga.tiff.Rasters;
//import mil.nga.tiff.TIFFImage;
//import mil.nga.tiff.TiffReader;

public class RadarImageDAO {
    private static final int BUFFER_SIZE = 4096;

    public ImageDTO downloadUnzipConvert(String uri) throws IOException {
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        GZIPInputStream gzipInputStream = null;
        try {
            URL url = new URL(uri);
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
            inputStream = connection.getInputStream();
            gzipInputStream = new GZIPInputStream(inputStream);
            List<Byte> imageBytes = new ArrayList<Byte>();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            while ((bytesRead = gzipInputStream.read(buffer, 0, buffer.length)) > 0) {
                for (int ii = 0; ii < bytesRead; ii++) {
                    imageBytes.add(buffer[ii]);
                }
            }
            byte[] imageArray = new byte[imageBytes.size()];
            for (int ii = 0; ii < imageBytes.size(); ii++) {
                imageArray[ii] = imageBytes.get(ii).byteValue();
            }
            imageBytes.clear();
            /*
            TIFFImage tiffImage = TiffReader.readTiff(imageArray);
            FileDirectory fileDirectory = tiffImage.getFileDirectory();
            Rasters rasters = fileDirectory.readRasters();
            int width = rasters.getWidth();
            int height = rasters.getHeight();
            int sizePixel = rasters.sizePixel();
            int[] allPixels = new int[width * height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Number[] pixel = rasters.getPixel(x, y);
                    int red = pixel[0].intValue();
                    int green = pixel[1].intValue();
                    int blue = pixel[2].intValue();
                    int alpha = pixel[3].intValue();
                    if ((red == 0) && (green == 0) && (blue == 0) && (alpha == 0)) {
                        allPixels[x * width + y] = Color.alpha(Color.TRANSPARENT);
                    } else {
                        allPixels[x * width + y] = Color.argb(alpha, red, green, blue);
                    }
                }
            }
            ImageDTO dto = new ImageDTO();
            dto.setWidth(width);
            dto.setHeight(height);
            dto.setImageType(Bitmap.Config.ARGB_8888.ordinal());
            dto.setPixels(allPixels);
            return dto;
             */
            return null;
        } finally {
            if (gzipInputStream != null) {
                gzipInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
