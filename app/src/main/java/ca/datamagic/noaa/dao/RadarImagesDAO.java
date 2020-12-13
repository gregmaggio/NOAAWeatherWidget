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
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-User", "?1");
            connection.setRequestProperty("Sec-Fetch-Dest", "document");
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
            String message = t.getMessage();
            _logger.warning("Exception: " + message);
            if ((message != null) && message.toLowerCase().contains("failed to connect")) {
                return null;
            }
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
