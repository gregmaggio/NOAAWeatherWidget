package ca.datamagic.noaa.dao;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;

public class StationDAO {
    private static final double _radiusOfEarthMeters = 6371e3;
    private static final int _maxReturn = 5;
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

    public StationDTO[] readNearest(double latitude, double longitude, double distance, String units) {
        distance = distanceToMeters(distance, units);
        List<NearestStationResult> results = new ArrayList<NearestStationResult>();
        for (int ii = 0; ii < _stations.size(); ii++) {
            StationDTO station = _stations.get(ii);
            double distanceToStation = computeDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distanceToStation <= distance) {
                results.add(new NearestStationResult(station, distanceToStation));
            }
        }
        Collections.sort(results, new NearestStationResultComparator());
        int numberReturned = results.size();
        if (numberReturned > _maxReturn) {
            numberReturned = _maxReturn;
        }
        StationDTO[] nearest = new StationDTO[numberReturned];
        for (int ii = 0; ii < numberReturned; ii++) {
            nearest[ii] = results.get(ii).getStation();
        }
        return nearest;
    }

    public StationDTO readNearest(double latitude, double longitude) {
        StationDTO nearest = null;
        double distanceToNearest = 0.0;
        for (int ii = 0; ii < _stations.size(); ii++) {
            StationDTO station = _stations.get(ii);
            double distanceToStation = computeDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (nearest == null) {
                nearest = station;
                distanceToNearest = distanceToStation;
            } else if (distanceToNearest > distanceToStation) {
                nearest = station;
                distanceToNearest = distanceToStation;
            }
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

    private class NearestStationResult {
        private StationDTO _station = null;
        private double _distance = 0.0;

        public NearestStationResult(StationDTO station, double distance) {
            _station = station;
            _distance = distance;
        }

        public StationDTO getStation() {
            return _station;
        }

        public double getDistance() {
            return _distance;
        }
    }

    private class NearestStationResultComparator implements Comparator<NearestStationResult> {
        @Override
        public int compare(NearestStationResult result1, NearestStationResult result2) {
            if (result1.getDistance() < result2.getDistance()) {
                return -1;
            }
            if (result1.getDistance() > result2.getDistance()) {
                return 1;
            }
            return 0;
        }
    }
}
