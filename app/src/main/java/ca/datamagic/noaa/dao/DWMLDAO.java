package ca.datamagic.noaa.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.noaa.dom.DWMLHandler;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLDAO {
    private static Logger _logger = LogFactory.getLogger(DWMLDAO.class);
    private static Pattern _scriptPattern = Pattern.compile("<script", Pattern.CASE_INSENSITIVE);
    private static Pattern _closeTagPattern = Pattern.compile(">", Pattern.CASE_INSENSITIVE);
    private static Pattern _endScriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;
    private static String _filesPath = null;

    public static String getFilesPath() {
        return _filesPath;
    }

    public static void setFilesPath(String newVal) {
        _filesPath = newVal;
    }

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
        //https://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php?whichClient=NDFDgen&lat=67.1&lon=-157.85&listLatLon=&lat1=&lon1=&lat2=&lon2=&resolutionSub=&listLat1=&listLon1=&listLat2=&listLon2=&resolutionList=&endPoint1Lat=&endPoint1Lon=&endPoint2Lat=&endPoint2Lon=&listEndPoint1Lat=&listEndPoint1Lon=&listEndPoint2Lat=&listEndPoint2Lon=&zipCodeList=&listZipCodeList=&centerPointLat=&centerPointLon=&distanceLat=&distanceLon=&resolutionSquare=&listCenterPointLat=&listCenterPointLon=&listDistanceLat=&listDistanceLon=&listResolutionSquare=&citiesLevel=&listCitiesLevel=&sector=&gmlListLatLon=&featureType=&requestedTime=&startTime=&endTime=&compType=&propertyName=&product=time-series&begin=2018-04-08T00%3A00%3A00&end=2018-04-15T00%3A00%3A00&Unit=e&maxt=maxt&mint=mint&temp=temp&qpf=qpf&pop12=pop12&snow=snow&dew=dew&wspd=wspd&wdir=wdir&sky=sky&wx=wx&waveh=waveh&icons=icons&rh=rh&appt=appt&wgust=wgust&maxrh=maxrh&minrh=minrh&Submit=Submit
        URL url = new URL(MessageFormat.format("https://forecast.weather.gov/MapClick.php?lat={0}&lon={1}&unit={2}&lg=english&FcstType=dwml", Double.toString(latitude), Double.toString(longitude), Integer.toString(intUnit)));
        _logger.info("url: " + url.toString());
        HttpsURLConnection connection = null;
        for (int ii = 0; ii < _maxTries; ii++) {
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
                    break;
                }
                DWMLHandler handler = new DWMLHandler();
                DWMLDTO dwml = handler.parse(responseText);
                write(responseText);
                return  dwml;
            } catch (Throwable t) {
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(_retryTimeoutMillis);
            } finally {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Throwable t) {
                        _logger.warning("Exception: " + t.getMessage());
                    }
                }
            }
        }
        String lastResponseText = read();
        if ((lastResponseText != null) && (lastResponseText.length() > 0)) {
            DWMLHandler handler = new DWMLHandler();
            DWMLDTO dwml = handler.parse(lastResponseText);
            dwml.setCached(true);
            return dwml;
        }
        return null;
    }

    public static void write(String xml) throws IOException {
        OutputStream output = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return;
            }
            String fileName = MessageFormat.format("{0}/DWML.xml", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            output = new FileOutputStream(file.getAbsolutePath());
            output.write(xml.getBytes());
            output.flush();
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    public static String read() throws IOException {
        InputStream input = null;
        try {
            if ((_filesPath == null) || (_filesPath.length() < 1)) {
                return null;
            }
            String fileName = MessageFormat.format("{0}/DWML.xml", _filesPath);
            File file = new File(fileName);
            if (file.exists()) {
                input = new FileInputStream(file.getAbsolutePath());
                int inputBytes = (int)file.length();
                _logger.info("inputBytes: " + Integer.toString(inputBytes));
                byte[] inputBuffer = new byte[inputBytes];
                int bytesRead = input.read(inputBuffer, 0, inputBuffer.length);
                _logger.info("bytesRead: " + Integer.toString(bytesRead));
                if (bytesRead > 0) {
                    return new String(inputBuffer, 0, bytesRead);
                }
            }
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    private static String getLocationRedirect(String responseText) {
        if (responseText.toLowerCase().indexOf("window.location.href") > -1) {
            return responseText;
        }
        return null;
    }
}
