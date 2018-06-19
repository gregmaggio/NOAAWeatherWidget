package ca.datamagic.noaa.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 12/31/2015.
 */
public class ImageDAO {
    private static Logger _logger = LogFactory.getLogger(ImageDAO.class);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;
    private static int _bufferSize = 1024;
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

    public Bitmap load(String imageUrl) throws Throwable {
        Throwable lastError = null;
        _logger.info("imageUrl: " + imageUrl);
        URL url = new URL(imageUrl);

        String path = url.getPath();
        _logger.info("path: " + path);

        File file = new File(path);
        _logger.info("file: " + file.getName());

        byte[] imageBytes = null;
        if (_enableFileCache) {
            imageBytes = read(file.getName());
            if (imageBytes != null) {
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }
        }

        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    _logger.info("openConnection: " + url.toString());
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    List<Byte> bytesArray = new ArrayList<Byte>();
                    byte[] buffer = new byte[_bufferSize];
                    int bytesRead = 0;
                    while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
                        for (int jj = 0; jj < bytesRead; jj++) {
                            bytesArray.add(new Byte(buffer[jj]));
                        }
                    }
                    imageBytes = new byte[bytesArray.size()];
                    for (int jj = 0; jj < bytesArray.size(); jj++) {
                        imageBytes[jj] = bytesArray.get(jj).byteValue();
                    }
                    if (_enableFileCache) {
                        write(file.getName(), imageBytes);
                    }
                    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
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
                ThreadEx.sleep(_retryTimeoutMillis);
            }
        }
        if (lastError != null)
            throw lastError;
        return null;
    }

    public static void write(String imageName, byte[] bytes) throws IOException {
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
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static byte[] read(String imageName) throws IOException {
        InputStream input = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return null;
            }
            String fileName = MessageFormat.format("{0}/{1}", _filesPath, imageName);
            File file = new File(fileName);
            if (file.exists()) {
                input = new FileInputStream(file.getAbsolutePath());
                int inputBytes = (int)file.length();
                _logger.info("inputBytes: " + Integer.toString(inputBytes));
                byte[] inputBuffer = new byte[inputBytes];
                int bytesRead = input.read(inputBuffer, 0, inputBuffer.length);
                _logger.info("bytesRead: " + Integer.toString(bytesRead));
                if (bytesRead == inputBuffer.length) {
                    return inputBuffer;
                }
            }
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
