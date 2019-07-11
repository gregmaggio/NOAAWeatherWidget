package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.dto.PredictionDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

/**
 * Created by Greg on 3/21/2018.
 */

public class GooglePlacesDAO {
    private static Logger _logger = LogFactory.getLogger(GooglePlacesDAO.class);
    private static String _apiKey = null;

    public static String getApiKey() {
        return _apiKey;
    }

    public static void setApiKey(String newVal) {
        _apiKey = newVal;
    }

    public List<PredictionDTO> loadAutoCompletePredictions(String searchText) throws IOException {
        URL url = new URL(MessageFormat.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input={0}&types=geocode&language=en&key={1}", searchText, _apiKey));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireStream(inputStream);
            _logger.info("responseText: " + responseText);
            JSONObject responseObj = new JSONObject(responseText);
            JSONArray predictions = (JSONArray)responseObj.get("predictions");
            List<PredictionDTO> list = new ArrayList<PredictionDTO>();
            for (int jj = 0; jj < predictions.length(); jj++) {
                PredictionDTO prediction = new PredictionDTO(predictions.getJSONObject(jj));
                if ((prediction.getDescription() != null) && (prediction.getDescription().length() > 0) && (prediction.getDescription().toUpperCase().contains("USA"))) {
                    list.add(prediction);
                }
            }
            return list;
        } catch (Throwable t) {
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("failed to connect")) {
                return null;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
        }
        return null;
    }

    public PlaceDTO loadPlace(String placeId) throws MalformedURLException {
        URL url = new URL(MessageFormat.format("https://maps.googleapis.com/maps/api/place/details/json?placeid={0}&key={1}", placeId, _apiKey));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireStream(inputStream);
            _logger.info("responseText: " + responseText);
            JSONObject responseObj = new JSONObject(responseText);
            return new PlaceDTO(responseObj.getJSONObject("result"));
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    _logger.warning("Exception: " + t.getMessage());
                }
            }
        }
        return null;
    }
}
