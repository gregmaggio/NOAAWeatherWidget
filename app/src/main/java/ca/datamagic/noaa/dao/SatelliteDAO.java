package ca.datamagic.noaa.dao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

public class SatelliteDAO {
    private static Logger _logger = LogFactory.getLogger(SatelliteDAO.class);
    private static Pattern _satelliteImagePattern = Pattern.compile("(\\d+)\\x5F(\\w+)\\x2D(\\w+)\\x2D(\\w+)\\x2D(\\w+)\\x2D(\\d+)x(\\d+)\\x2Ejpg", Pattern.CASE_INSENSITIVE);
    //(\d+)\x5F(\w+)\x2D(\w+)\x2D(\w+)\x2D(\w+)\x2D(\d+)x(\d+)\x2Ejpg
    //20203511220_GOES16-ABI-pr-GEOCOLOR-600x600.jpg
    //20203471506_GOES17-ABI-hi-GEOCOLOR-600x600.jpg
    //20203511516_GOES16-ABI-CONUS-GEOCOLOR-625x375.jpg
    // - => \\x2D
    // _ => \\x5F
    // . => \\x2E
    //PR: https://cdn.star.nesdis.noaa.gov/GOES16/ABI/SECTOR/pr/GEOCOLOR/
    //AK: https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/ak/GEOCOLOR/
    //US: https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/GEOCOLOR/
    //HI: https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/hi/GEOCOLOR/

    private static String getSatellitePath(String state) {
        if (state != null) {
            if (state.compareToIgnoreCase("PR") == 0) {
                return "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/SECTOR/pr/GEOCOLOR/";
            } else  if (state.compareToIgnoreCase("HI") == 0) {
                return "https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/hi/GEOCOLOR/";
            } else  if (state.compareToIgnoreCase("AK") == 0) {
                return "https://cdn.star.nesdis.noaa.gov/GOES17/ABI/SECTOR/ak/GEOCOLOR/";
            }
        }
        return "https://cdn.star.nesdis.noaa.gov/GOES16/ABI/CONUS/GEOCOLOR/";
    }

    public static List<String> loadSatelliteImages(String state) throws Throwable {
        String urlSpec = getSatellitePath(state);
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
            String responseText = IOUtils.readEntireString(connection.getInputStream());

            List<String> images = new ArrayList<String>();
            Document doc = Jsoup.parse(responseText);
            Elements links = doc.select("a");
            for (int jj = 0; jj < links.size(); jj++) {
                String fileName = links.get(jj).attr("href");
                _logger.info("fileName: " + fileName);
                Matcher satelliteImageMatcher = _satelliteImagePattern.matcher(fileName);
                if (satelliteImageMatcher.matches()) {
                    int width = Integer.parseInt(satelliteImageMatcher.group(6));
                    int height = Integer.parseInt(satelliteImageMatcher.group(7));
                    if ((width > 399) && (width < 1000) && (height > 299) && (height < 1000)) {
                        String imageUrl = MessageFormat.format("{0}{1}", urlSpec, fileName);
                        images.add(imageUrl);
                    }
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
