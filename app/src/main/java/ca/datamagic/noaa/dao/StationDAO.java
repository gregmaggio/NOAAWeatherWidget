package ca.datamagic.noaa.dao;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.util.DistanceCalculator;

public class StationDAO {
    private static final int _maxReturn = 5;
    private List<StationDTO> _stations = new ArrayList<StationDTO>();

    public StationDAO(InputStream inputStream) {
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
        distance = DistanceCalculator.distanceToMeters(distance, units);
        List<NearestStationResult> results = new ArrayList<NearestStationResult>();
        for (int ii = 0; ii < _stations.size(); ii++) {
            StationDTO station = _stations.get(ii);
            double distanceToStation = DistanceCalculator.computeDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distanceToStation <= distance) {
                results.add(new NearestStationResult(station, distanceToStation));
            }
        }
        if (results.size() > 0) {
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
        return null;
    }

    public StationDTO readNearest(double latitude, double longitude) {
        StationDTO nearest = null;
        double distanceToNearest = 0.0;
        for (int ii = 0; ii < _stations.size(); ii++) {
            StationDTO station = _stations.get(ii);
            double distanceToStation = DistanceCalculator.computeDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
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
