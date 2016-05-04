package ca.datamagic.noaa.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.MessageFormat;

import ca.datamagic.noaa.dom.DWMLByDayOutHandler;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 12/31/2015.
 */
public class ForecastDAO {
    //unit=0 - US
    //unit=1 - Metric
    //http://forecast.weather.gov/MapClick.php?lat=38.9233&lon=-77.0214&unit=0&lg=english&FcstType=dwml


    public String GetLatLonListForCityNames(int displayLevel) throws XmlPullParserException, IOException {
        SoapObject request = new SoapObject("http://graphical.weather.gov/xml/DWMLgen/wsdl/ndfdXML.wsdl", "LatLonListCityNames");
        request.addProperty("displayLevel", displayLevel);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE("http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php");
        httpTransport.call("http://graphical.weather.gov/xml/DWMLgen/wsdl/ndfdXML.wsdl#LatLonListCityNames", envelope);
        SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
        return  resultsRequestSOAP.toString();
    }

    public String GetLatLonListForZipCodes(String zipCodeList) throws XmlPullParserException, IOException {
        SoapObject request = new SoapObject("http://graphical.weather.gov/xml/DWMLgen/wsdl/ndfdXML.wsdl", "LatLonListZipCode");
        request.addProperty("zipCodeList", zipCodeList);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE("http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php");
        httpTransport.call("http://graphical.weather.gov/xml/DWMLgen/wsdl/ndfdXML.wsdl#LatLonListZipCode", envelope);
        SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
        return  resultsRequestSOAP.toString();
    }

    /**
     * Get a forecast for a given day
     * @param latitude
     * @param longitude
     * @param year
     * @param month
     * @param day
     * @param numDays
     * @param unit "e" for U.S. Standare/English units and "m" for Metric units
     * @param format "24 hourly" and "12 hourly"
     * @return
     * @throws Exception
     */
    public DWMLDTO GetForecastByDay(double latitude, double longitude, int year, int month, int day, int numDays, String unit, String format) throws Exception {

        String startDate = MessageFormat.format("{0}-{1}-{2}", String.format("%02d", year), String.format("%02d", month), String.format("%02d", day));
        SoapObject request = new SoapObject("http://graphical.weather.gov/xml/DWMLgen/wsdl/ndfdXML.wsdl", "NDFDgenByDay");
        request.addProperty("latitude", Double.toString(latitude));
        request.addProperty("longitude", Double.toString(longitude));
        request.addProperty("startDate", startDate);
        request.addProperty("numDays", Integer.toString(numDays));
        request.addProperty("Unit", unit);
        request.addProperty("format", format);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE("http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php");
        httpTransport.call("http://graphical.weather.gov/xml/DWMLgen/wsdl/ndfdXML.wsdl#NDFDgenByDay", envelope);
        SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
        String dwmlByDayOut = (String)resultsRequestSOAP.getProperty("dwmlByDayOut");
        DWMLByDayOutHandler handler = new DWMLByDayOutHandler();
        return handler.parse(dwmlByDayOut);
    }

}
