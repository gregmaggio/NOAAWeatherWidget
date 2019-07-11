package ca.datamagic.noaa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * Created by Greg on 8/26/2017.
 */

public class MultipartUtility {
    private Logger _logger = Logger.getLogger(MultipartUtility.class.getName());
    private static final String _lineFeed = "\r\n";
    private String _boundary = null;
    private String _uri = null;
    private String _charset = null;
    private HttpURLConnection _connection = null;
    private OutputStream _outputStream = null;
    private PrintWriter _writer = null;

    public MultipartUtility(String uri) throws IOException {
        this(uri, "UTF-8");
    }

    public  MultipartUtility(String uri, String charset) throws IOException {
        _logger.info("uri: " + uri);
        _logger.info("charset: " + charset);
        _uri= uri;
        _charset = charset;

        // creates a unique boundary based on time stamp
        _boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(_uri);
        _connection = (HttpURLConnection)url.openConnection();
        _connection.setConnectTimeout(2000);
        _connection.setUseCaches(false);
        _connection.setDoOutput(true);    // indicates POST method
        _connection.setDoInput(true);
        _connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + _boundary);
        _outputStream = _connection.getOutputStream();
        _writer = new PrintWriter(new OutputStreamWriter(_outputStream, _charset), true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        _writer.append("--" + _boundary).append(_lineFeed);
        _writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(_lineFeed);
        _writer.append("Content-Type: text/plain; charset=" + _charset).append(_lineFeed);
        _writer.append(_lineFeed);
        _writer.append(value).append(_lineFeed);
        _writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        FileInputStream inputStream = null;
        try {
            String fileName = uploadFile.getName();
            _writer.append("--" + _boundary).append(_lineFeed);
            _writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(_lineFeed);
            _writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(_lineFeed);
            _writer.append("Content-Transfer-Encoding: binary").append(_lineFeed);
            _writer.append(_lineFeed);
            _writer.flush();

            inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                _outputStream.write(buffer, 0, bytesRead);
            }
            _outputStream.flush();
            inputStream.close();
            inputStream = null;

            _writer.append(_lineFeed);
            _writer.flush();
        } finally {
             if (inputStream != null) {
                 inputStream.close();
             }
        }
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        _writer.append(name + ": " + value).append(_lineFeed);
        _writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        BufferedReader reader = null;
        try {
            _writer.append(_lineFeed).flush();
            _writer.append("--" + _boundary + "--").append(_lineFeed);
            _writer.close();
            _writer = null;
            _outputStream.close();
            _outputStream = null;

            // checks server's status code first
            int responseCode = _connection.getResponseCode();
            _logger.info("responseCode: " + responseCode);

            reader = new BufferedReader(new InputStreamReader(_connection.getInputStream()));
            StringBuffer responseText = new StringBuffer();
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                if (responseText.length() > 0) {
                    responseText.append(_lineFeed);
                }
                responseText.append(currentLine);
            }
            _logger.info("responseText: " + responseText.toString());

            _connection.disconnect();
            _connection = null;

            return responseText.toString();
        } finally {
             if (reader != null) {
                 reader.close();
             }
        }
    }
}
