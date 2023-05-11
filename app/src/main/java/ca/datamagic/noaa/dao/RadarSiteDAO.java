package ca.datamagic.noaa.dao;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.datamagic.noaa.dto.RadarSiteDTO;
import ca.datamagic.noaa.util.IOUtils;

public class RadarSiteDAO {
    private static final double radiusOfEarthMeters = 6371e3;
    private static final int maxReturn = 5;
    private RadarSiteDTO[] sites = null;

    public RadarSiteDAO(InputStream inputStream) throws IOException, JSONException {
        String json = IOUtils.readEntireString(inputStream);
        JSONArray jsonArray = new JSONArray(json);
        this.sites = new RadarSiteDTO[jsonArray.length()];
        for (int ii = 0; ii < sites.length; ii++) {
            this.sites[ii] = new RadarSiteDTO(jsonArray.getJSONObject(ii));
        }
    }

    public int size() {
        return this.sites.length;
    }

    public RadarSiteDTO get(int index) {
        return this.sites[index];
    }

    public RadarSiteDTO[] readNearest(double latitude, double longitude, double distance, String units) {
        distance = distanceToMeters(distance, units);
        List<NearestRadarSiteResult> results = new ArrayList<NearestRadarSiteResult>();
        for (int ii = 0; ii < this.sites.length; ii++) {
            RadarSiteDTO site = this.sites[ii];
            double distanceToSite = computeDistance(latitude, longitude, site.getLatitude(), site.getLongitude());
            if (distanceToSite <= distance) {
                results.add(new NearestRadarSiteResult(site, distanceToSite));
            }
        }
        if (results.size() > 0) {
            Collections.sort(results, new NearestRadarSiteResultComparator());
            int numberReturned = results.size();
            if (numberReturned > maxReturn) {
                numberReturned = maxReturn;
            }
            RadarSiteDTO[] nearest = new RadarSiteDTO[numberReturned];
            for (int ii = 0; ii < numberReturned; ii++) {
                nearest[ii] = results.get(ii).getRadarSite();
            }
            return nearest;
        }
        return null;
    }

    public RadarSiteDTO readNearest(double latitude, double longitude) {
        RadarSiteDTO nearest = null;
        double distanceToNearest = 0.0;
        for (int ii = 0; ii < this.sites.length; ii++) {
            RadarSiteDTO site = this.sites[ii];
            if ((site.getLatitude() != null) && (site.getLongitude() != null)) {
                double distanceToSite = computeDistance(latitude, longitude, site.getLatitude(), site.getLongitude());
                if (nearest == null) {
                    nearest = site;
                    distanceToNearest = distanceToSite;
                } else if (distanceToNearest > distanceToSite) {
                    nearest = site;
                    distanceToNearest = distanceToSite;
                }
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
        double distance = radiusOfEarthMeters * c;
        return distance;
    }

    private class NearestRadarSiteResult {
        private RadarSiteDTO radarSite = null;
        private double distance = 0.0;

        public NearestRadarSiteResult(RadarSiteDTO radarSite, double distance) {
            this.radarSite = radarSite;
            this.distance = distance;
        }

        public RadarSiteDTO getRadarSite() {
            return this.radarSite;
        }

        public double getDistance() {
            return this.distance;
        }
    }

    private class NearestRadarSiteResultComparator implements Comparator<NearestRadarSiteResult> {
        @Override
        public int compare(NearestRadarSiteResult result1, NearestRadarSiteResult result2) {
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
