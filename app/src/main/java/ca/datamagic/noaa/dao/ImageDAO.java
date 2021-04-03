package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

/**
 * Created by Greg on 12/31/2015.
 */
public class ImageDAO {
    private static Logger _logger = LogFactory.getLogger(ImageDAO.class);
    private static String _filesPath = null;
    private boolean _enableFileCache = true;

    public ImageDAO() {

    }

    public ImageDAO(boolean enableFileCache) {
        _enableFileCache = enableFileCache;
    }

    public static String getFilesPath() {
        return _filesPath;
    }

    public static void setFilesPath(String newVal) {
        _filesPath = newVal;
    }

    public Bitmap load(String imageUrl) {
        _logger.info("imageUrl: " + imageUrl);
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imageUrl);

            String path = url.getPath();
            _logger.info("path: " + path);

            File file = new File(path);
            _logger.info("file: " + file.getName());

            byte[] imageBytes = null;
            if (_enableFileCache) {
                imageBytes = read(file.getName());
                if (imageBytes != null) {
                    _logger.info("Decoding cached image.");
                    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
            }

            _logger.info("openConnection: " + url.toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
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
            inputStream = connection.getInputStream();
            imageBytes = IOUtils.readEntireByteArray(inputStream);
            if (_enableFileCache) {
                write(file.getName(), imageBytes);
            }
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("failed to connect")) {
                return null;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(connection);
        }
        return null;
    }

    public static void write(String imageName, byte[] bytes) {
        OutputStream output = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return;
            }
            String fileName = MessageFormat.format("{0}/{1}", _filesPath, imageName);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            output = new FileOutputStream(file.getAbsolutePath());
            output.write(bytes);
            output.flush();
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static byte[] read(String imageName) {
        InputStream input = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return null;
            }
            String fileName = MessageFormat.format("{0}/{1}", _filesPath, imageName);
            File file = new File(fileName);
            if (file.exists()) {
                input = new FileInputStream(file.getAbsolutePath());
                int inputBytes = (int) file.length();
                _logger.info("inputBytes: " + Integer.toString(inputBytes));
                byte[] inputBuffer = new byte[inputBytes];
                int bytesRead = input.read(inputBuffer, 0, inputBuffer.length);
                _logger.info("bytesRead: " + Integer.toString(bytesRead));
                if (bytesRead == inputBuffer.length) {
                    return inputBuffer;
                }
            }
            return null;
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
        }
        return null;
    }
}
