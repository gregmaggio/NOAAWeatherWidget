package ca.datamagic.noaa.dao;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.MessageFormat;

import javax.xml.parsers.ParserConfigurationException;

import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dom.ObservationHandler;

/**
 * Created by Greg on 1/4/2016.
 */
public class ObservationDAO {
    /**
     * Get the current observation at a location
     * @param latitude
     * @param longitude
     * @return
     */
    public DWMLDTO GetCurrentObservation(double latitude, double longitude) throws Exception {
        URL url = new URL(MessageFormat.format("http://forecast.weather.gov/MapClick.php?lat={0}&lon={1}&unit=0&lg=english&FcstType=dwml", Double.toString(latitude), Double.toString(longitude)));
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.connect();

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        char[] buffer = new char[255];
        int bytesRead = 0;
        StringBuffer response = new StringBuffer();
        while ((bytesRead = reader.read(buffer, 0, buffer.length)) > 0) {
            response.append(new String(buffer, 0, bytesRead));
        }
        reader.close();
        ObservationHandler handler = new ObservationHandler();
        return handler.parse(response.toString());
    }
}
