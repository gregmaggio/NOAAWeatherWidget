package ca.datamagic.noaa.dao;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;

public class StationDAO {
    private static final double _radiusOfEarthMeters = 6371e3;
    private List<StationDTO> _stations = new ArrayList<StationDTO>();

    public StationDAO(InputStream inputStream) throws IOException {
        CsvFormat format = new CsvFormat();
        format.setDelimiter(',');
        format.setLineSeparator("\n");
        format.setQuote('\"');
        CsvParserSettings settings = new CsvParserSettings();
        settings.setFormat(format);
        CsvParser csvParser = new CsvParser(settings);
        List<String[]> lines = csvParser.parseAll(inputStream);
        for (int ii = 1; ii < lines.size(); ii++) {
            String[] currentLineItems = lines.get(ii);
            String stationId = currentLineItems[0].toUpperCase();
            String stationName = currentLineItems[1];
            String state = currentLineItems[2];
            String wfo = currentLineItems[3];
            String radar = currentLineItems[4];
            String timeZoneId = currentLineItems[5];
            double latitude = Double.parseDouble(currentLineItems[6]);
            double longitude = Double.parseDouble(currentLineItems[7]);
            StationDTO station = new StationDTO();
            station.setStationId(stationId);
            station.setStationName(stationName);
            station.setState(state);
            station.setWFO(wfo);
            station.setRadar(radar);
            station.setTimeZoneId(timeZoneId);
            station.setLatitude(latitude);
            station.setLongitude(longitude);
            _stations.add(station);
        }
    }

    public int size() {
        return _stations.size();
    }

    public StationDTO get(int index) {
        return _stations.get(index);
    }

    public StationDTO readNearest(double latitude, double longitude, double distance, String units) {
        StationDTO nearest = null;
        double nearestDistance = Double.NaN;
        distance = distanceToMeters(distance, units);
        //System.out.println("distance: " + distance);
        for (int ii = 0; ii < _stations.size(); ii++) {
            StationDTO station = _stations.get(ii);
            double distanceToStation = computeDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distanceToStation <= distance) {
                //System.out.println("StationName: " + station.getStationName());
                //System.out.println("distanceToStation: " + distanceToStation);
                if (nearest == null) {
                    nearest = station;
                    nearestDistance = distanceToStation;
                } else if (nearestDistance > distanceToStation) {
                    nearest = station;
                    nearestDistance = distanceToStation;
                }
            }
        }
        if (nearest != null) {
            //System.out.println("nearest: " + nearest.getStationName());
            //System.out.println("nearestDistance: " + nearestDistance);
        }
        return nearest;
    }

    private static double distanceToMeters(double distance, String units) {
        if (units.compareToIgnoreCase("statute miles") == 0) {
            return distance * 1609.34;
        }
        return Double.NaN;
    }

    public static double computeDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double deltaLatitude = Math.toRadians(latitude2 - latitude1);
        double deltaLongitude = Math.toRadians(longitude2 - longitude1);
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);
        double sinDeltaLatitudeOverTwo = Math.sin(deltaLatitude / 2);
        double sinDeltaLongitudeOverTwo = Math.sin(deltaLongitude / 2);
        double a = sinDeltaLatitudeOverTwo * sinDeltaLatitudeOverTwo +
                Math.cos(latitude1) * Math.cos(latitude2) *
                        sinDeltaLongitudeOverTwo * sinDeltaLongitudeOverTwo;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = _radiusOfEarthMeters * c;
        return distance;
    }
}
