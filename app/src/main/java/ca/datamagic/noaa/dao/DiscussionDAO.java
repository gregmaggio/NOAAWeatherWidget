package ca.datamagic.noaa.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.util.ThreadEx;
import ca.datamagic.noaa.widget.MainActivity;

/**
 * Created by Greg on 1/2/2016.
 */
public class DiscussionDAO {
    private static Logger _logger = LogFactory.getLogger(DiscussionDAO.class);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;
    private static String _discussionStart = "<pre class=\"glossaryProduct\">".toLowerCase();
    private static String _discussionEnd = "</pre>".toLowerCase();
    private static String _filesPath = null;

    public static String getFilesPath() {
        return _filesPath;
    }

    public static void setFilesPath(String newVal) {
        _filesPath = newVal;
    }

    public String load(String issuedBy) throws Throwable {
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=0", issuedBy));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                connection = (HttpsURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String currentLine = null;
                boolean started = false;
                while ((currentLine = reader.readLine()) != null) {
                    currentLine = currentLine.trim();
                    if (!started) {
                        if (currentLine.toLowerCase().contains(_discussionStart)) {
                            started = true;
                        }
                    } else {
                        if (currentLine.toLowerCase().contains(_discussionEnd)) {
                            break;
                        }
                        buffer.append(currentLine);
                        buffer.append("\n");
                    }
                }
                String responseText = buffer.toString();
                _logger.info("responseLength: " + responseText.length());
                if ((responseText == null) || (responseText.length() < 1)) {
                    throw new Exception("Discussion was null");
                }
                write(responseText);
                return responseText;
            } catch (Throwable t) {
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(_retryTimeoutMillis);
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
        return read();
    }

    public static void write(String discussionText) throws IOException {
        OutputStream output = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return;
            }
            String fileName = MessageFormat.format("{0}/Discussion.txt", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            output = new FileOutputStream(file.getAbsolutePath());
            output.write(discussionText.getBytes());
            output.flush();
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static String read() throws IOException {
        InputStream input = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return null;
            }
            String fileName = MessageFormat.format("{0}/Discussion.txt", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                input = new FileInputStream(file.getAbsolutePath());
                int inputBytes = (int)file.length();
                _logger.info("inputBytes: " + Integer.toString(inputBytes));
                byte[] inputBuffer = new byte[inputBytes];
                int bytesRead = input.read(inputBuffer, 0, inputBuffer.length);
                _logger.info("bytesRead: " + Integer.toString(bytesRead));
                if (bytesRead > 0) {
                    return new String(inputBuffer, 0, bytesRead);
                }
            }
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
