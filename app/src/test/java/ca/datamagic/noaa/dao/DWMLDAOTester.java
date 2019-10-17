package ca.datamagic.noaa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 2/18/2017.
 */
@RunWith(JUnit4.class)
public class DWMLDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DWMLDAO dao = new DWMLDAO();
        DWMLDTO dwml = dao.load(38.98, -76.92, "e");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dwml);
        System.out.println("json: " + json);
    }

    @Test
    public void test2() throws Throwable {
        DWMLDAO dao = new DWMLDAO();
        DWMLDTO dwml = dao.load(67.1, -157.85, "e");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dwml);
        System.out.println("json: " + json);
    }

    @Test
    public void test3() throws Throwable {
        //https://www.weather.gov/Fairbanks
        URL url = new URL("https://www.weather.gov/Fairbanks");
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.connect();
        int responseCode = connection.getResponseCode();
        System.out.println("responseCode: " + responseCode);
        String responseMessage = connection.getResponseMessage();
        System.out.println("responseMessage: " + responseMessage);
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        Set<String> keys = headerFields.keySet();
        for (String key : keys) {
            System.out.println("key: " + key);
            List<String> values = headerFields.get(key);
            if (values.size() > 0) {
                System.out.println("value: " + values.get(0));
            }
        }
    }

    @Test
    public void test4() throws Throwable {
        DWMLDAO dao = new DWMLDAO();
        DWMLDTO dwml = dao.load(41.31136689999999, -105.5911007, "e");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dwml);
        System.out.println("json: " + json);
    }
}
