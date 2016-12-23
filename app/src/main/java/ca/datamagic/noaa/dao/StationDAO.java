package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 12/14/2016.
 */
public class StationDAO {
    public List<StationDTO> list() throws MalformedURLException, IOException, JSONException {
        URL url = new URL("http://datamagic.ca/WFO/api/station");
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
            buffer.append(currentLine);
        }
        String json = buffer.toString();
        JSONArray array = new JSONArray(json);
        ArrayList<StationDTO> items = new ArrayList<StationDTO>();
        for (int ii = 0; ii < array.length(); ii++) {
            items.add(new StationDTO(array.getJSONObject(ii)));
        }
        return items;
    }

    public StationDTO nearestWithRadiosonde(double latitude, double longitude) throws MalformedURLException, IOException, JSONException {
        URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/station/{0}/{1}/nearestWithRadiosonde", Double.toString(latitude), Double.toString(longitude)));
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
            buffer.append(currentLine);
        }
        String json = buffer.toString();
        JSONObject obj = new JSONObject(json);
        return new StationDTO(obj);
    }

    public StationDTO nearest(double latitude, double longitude) throws MalformedURLException, IOException, JSONException {
        URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/station/{0}/{1}/nearest", Double.toString(latitude), Double.toString(longitude)));
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
            buffer.append(currentLine);
        }
        String json = buffer.toString();
        JSONObject obj = new JSONObject(json);
        return new StationDTO(obj);
    }

    public StationDTO read(String id) throws MalformedURLException, IOException, JSONException {
        URL url = new URL(MessageFormat.format("http://datamagic.ca/WFO/api/station/{0}", id));
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
            buffer.append(currentLine);
        }
        String json = buffer.toString();
        JSONObject obj = new JSONObject(json);
        return new StationDTO(obj);
    }
}
