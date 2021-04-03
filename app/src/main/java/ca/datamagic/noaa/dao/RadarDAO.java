package ca.datamagic.noaa.dao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dto.RadarTimeDTO;
import ca.datamagic.noaa.util.IOUtils;

public class RadarDAO {
    private static final Pattern _timeStampPattern = Pattern.compile("CONUS\\x5FL2\\x5FBREF\\x5FQCD\\x5F(\\d{4})(\\d{2})(\\d{2})\\x5F(\\d{2})(\\d{2})(\\d{2})\\x2Etif\\x2Egz", Pattern.CASE_INSENSITIVE);
    private static final int _maxTimeStamps = 15;

    public int getRadarTile(double latitude, double longitude) {
        int tile = -1;
        if ((latitude >= 40.97989806962014) && (latitude <= 55.7765730186677) && (longitude >= -135.0) && (longitude <= -112.5)) {
            tile = 2;
        } else if ((latitude >= 40.97989806962014) && (latitude <= 55.7765730186677) && (longitude >= -112.50000000000001) && (longitude <= -90.00000000000001)) {
            tile = 3;
        } else if ((latitude >= 40.97989806962014) && (latitude <= 55.7765730186677) && (longitude >= -90.00000000000001) && (longitude <= -67.5)) {
            tile = 4;
        } else if ((latitude >= 40.97989806962014) && (latitude <= 55.7765730186677) && (longitude >= -67.5) && (longitude <= -44.99999999999999)) {
            tile = 5;
        } else if ((latitude >= 21.94304553343817) && (latitude <= 40.97989806962012) && (longitude >= -135.0) && (longitude <= -112.5)) {
            tile = 8;
        } else if ((latitude >= 21.94304553343817) && (latitude <= 40.97989806962012) && (longitude >= -112.50000000000001) && (longitude <= -90.00000000000001)) {
            tile = 9;
        } else if ((latitude >= 21.94304553343817) && (latitude <= 40.97989806962012) && (longitude >= -90.00000000000001) && (longitude <= -67.5)) {
            tile = 10;
        } else if ((latitude >= 21.94304553343817) && (latitude <= 40.97989806962012) && (longitude >= -67.5) && (longitude <= -44.99999999999999)) {
            tile = 11;
        }
        return tile;
    }

    public List<RadarTimeDTO> getTimeStamps() throws IOException {
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL("https://mrms.ncep.noaa.gov/data/RIDGEII/L2/CONUS/BREF_QCD/");
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-User", "?1");
            connection.setRequestProperty("Sec-Fetch-Dest", "document");
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireString(inputStream);
            Document document = Jsoup.parse(responseText);
            Elements anchors = document.getElementsByTag("a");
            TimeZone utc = TimeZone.getTimeZone("UTC");
            List<RadarTimeDTO> radarTimes = new ArrayList<RadarTimeDTO>();
            if (anchors != null) {
                for (int ii = anchors.size() - 1; ii > -1; ii--) {
                    Element anchor = anchors.get(ii);
                    if (anchor.hasAttr("href")) {
                        String href = anchor.attr("href");
                        Matcher timeStampMatcher = _timeStampPattern.matcher(href);
                        if (timeStampMatcher.matches()) {
                            String year = timeStampMatcher.group(1);
                            String month = timeStampMatcher.group(2);
                            String day = timeStampMatcher.group(3);
                            String hour = timeStampMatcher.group(4);
                            String minute = timeStampMatcher.group(5);
                            String second = timeStampMatcher.group(6);
                            String timeStamp = MessageFormat.format("{0}-{1}-{2}T{3}:{4}:{5}.000Z", year, month, day, hour, minute, second);
                            Calendar calendar = new GregorianCalendar(utc);
                            calendar.set(Calendar.YEAR, Integer.parseInt(year));
                            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                            calendar.set(Calendar.SECOND, Integer.parseInt(second));
                            radarTimes.add(new RadarTimeDTO(timeStamp, calendar.getTime()));
                        }
                    }
                }
            }
            Collections.sort(radarTimes, new RadarTimeDTO.RadarTimeComparator());
            if (radarTimes.size() > _maxTimeStamps) {
                int startIndex = radarTimes.size() - 1 - _maxTimeStamps;
                if (startIndex < 0) {
                    startIndex = 0;
                }
                radarTimes = radarTimes.subList(startIndex, radarTimes.size());
            }
            return radarTimes;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
