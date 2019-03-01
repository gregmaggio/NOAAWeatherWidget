package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dto.WFODTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class WFODAO {
    private static Logger _logger = LogFactory.getLogger(WFODAO.class);

    public List<WFODTO> read(double latitude, double longitude) throws Throwable {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(MessageFormat.format("http://env-5616586.jelastic.servint.net/WFO/api/{0}/{1}/coordinates", Double.toString(latitude), Double.toString(longitude)));
            _logger.info("url: " + url.toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String json = IOUtils.readEntireStream(connection.getInputStream());
            List<WFODTO> wfoList = parseJSON(json);
            return wfoList;
        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        } finally {
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

    private static List<WFODTO> parseJSON(String json) throws JSONException {
        JSONArray array = new JSONArray(json);
        ArrayList<WFODTO> items = new ArrayList<WFODTO>();
        for (int jj = 0; jj < array.length(); jj++) {
            items.add(new WFODTO(array.getJSONObject(jj)));
        }
        return  items;
    }
}
