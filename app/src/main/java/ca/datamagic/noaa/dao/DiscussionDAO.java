package ca.datamagic.noaa.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * Created by Greg on 1/2/2016.
 */
public class DiscussionDAO {
    public String load(String issuedBy) throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL(MessageFormat.format("http://forecast.weather.gov/product.php?site={0}&issuedby={0}&product=AFD&format=txt&version=1&glossary=0", issuedBy));
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
            if (!started) {
                if (currentLine.toLowerCase().contains("<pre")) {
                    started = true;
                }
            } else {
                if (currentLine.toLowerCase().contains("</pre>")) {
                    break;
                }
                buffer.append(currentLine);
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }
}
