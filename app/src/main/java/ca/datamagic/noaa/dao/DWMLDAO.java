package ca.datamagic.noaa.dao;

import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLDAO {
    private static Logger _logger = LogFactory.getLogger(DWMLDAO.class);
    private static Pattern _scriptPattern = Pattern.compile("<script", Pattern.CASE_INSENSITIVE);
    private static Pattern _closeTagPattern = Pattern.compile(">", Pattern.CASE_INSENSITIVE);
    private static Pattern _endScriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);

    /**
     * Get the current observation at a location
     * @param latitude
     * @param longitude
     * @return
     */
    public DWMLDTO load(double latitude, double longitude, String unit) throws Throwable {
        int intUnit = 0;
        if (unit.compareToIgnoreCase("m") == 0) {
            intUnit = 1;
        }
        Calendar now = Calendar.getInstance();
        Calendar sevenDays = Calendar.getInstance();
        sevenDays.add(Calendar.DAY_OF_MONTH, 7);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));   // This line converts the given date into UTC time zone
        String startDate = sdf.format(now.getTime()) + "Z";
        String endDate = sdf.format(sevenDays.getTime()) + "Z";
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/MapClick.php?lat={0}&lon={1}&unit={2}&lg=english&FcstType=dwml", Double.toString(latitude), Double.toString(longitude), Integer.toString(intUnit)));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            String responseText = IOUtils.readEntireStream(connection.getInputStream());
            _logger.info("responseLength: " + responseText.length());
            _logger.info("responseText: " + responseText);
            String locationRedirect = getLocationRedirect(responseText);
            _logger.info("locationRedirect: " + locationRedirect);
            if (locationRedirect != null) {
                return null;
            }
            DWMLHandler handler = new DWMLHandler();
            DWMLDTO dwml = handler.parse(responseText);
            return  dwml;
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

    private static String getLocationRedirect(String responseText) {
        if (responseText.toLowerCase().indexOf("window.location.href") > -1) {
            return responseText;
        }
        return null;
    }
}
