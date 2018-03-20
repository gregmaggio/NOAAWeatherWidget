package ca.datamagic.noaa.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/18/2018.
 */

public class URLConnectionReader {
    private static Logger _logger = LogFactory.getLogger(URLConnectionReader.class);
    private static int _bufferSize = 1024;

    public static byte[] readBytes(String urlSpec) throws IOException {
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        _logger.info("url: " + urlSpec);
        URL url = new URL(urlSpec);
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();
        List<Byte> bytesList = new ArrayList<Byte>();
        byte[] buffer = new byte[_bufferSize];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
            for (int ii = 0; ii < bytesRead; ii++) {
                bytesList.add(new Byte(buffer[ii]));
            }
        }
        byte[] bytes = new byte[bytesList.size()];
        for (int ii = 0; ii < bytesList.size(); ii++) {
            bytes[ii] = bytesList.get(ii);
        }
        return bytes;
    }

    public static String readString(String urlSpec) throws IOException {
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        _logger.info("url: " + urlSpec);
        URL url = new URL(urlSpec);
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();
        StringBuffer response = new StringBuffer();
        byte[] buffer = new byte[_bufferSize];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
            response.append(new String(buffer, 0, bytesRead));
        }
        return response.toString();
    }
}
