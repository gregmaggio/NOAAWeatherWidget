package ca.datamagic.noaa.dao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class RadarImagesDAO {
    private static Logger _logger = LogFactory.getLogger(RadarImagesDAO.class);

    public List<String> loadRadarImages(String wfo) throws Throwable {
        String urlSpec = MessageFormat.format("https://radar.weather.gov/ridge/RadarImg/N0R/{0}/", wfo);
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String responseText = IOUtils.readEntireStream(connection.getInputStream());

            List<String> images = new ArrayList<String>();
            Document doc = Jsoup.parse(responseText);
            Elements links = doc.select("a");
            for (int jj = 0; jj < links.size(); jj++) {
                String fileName = links.get(jj).attr("href");
                _logger.info("fileName: " + fileName);
                if (fileName.toLowerCase().endsWith(".gif")) {
                    String imageUrl = MessageFormat.format("https://radar.weather.gov/ridge/RadarImg/N0R/{0}/{1}", wfo, fileName);
                    images.add(imageUrl);
                }
            }
            return images;
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
}
