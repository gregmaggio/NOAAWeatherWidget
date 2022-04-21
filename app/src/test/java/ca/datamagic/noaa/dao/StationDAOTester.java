package ca.datamagic.noaa.dao;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.StationDTO;

public class StationDAOTester extends BaseTester {
    private static final double distance = 75;
    private static final String units = "statute miles";

    @Test
    public void test1() throws Throwable {
        double latitude = 46.09893035888672;
        double longitude = -64.78649139404297;
        StationDAO stationDAO = new StationDAO(new FileInputStream("C:/Dev/Android/NOAAWeatherWidget/app/src/main/res/raw/stations.csv"));
        StationDTO[] nearest = stationDAO.readNearest(latitude,longitude, distance, units);
        Assert.assertNotNull(nearest);
        System.out.println("nearest: " + nearest.length);
        StationDTO nearestStation = stationDAO.readNearest(latitude,longitude);
        Assert.assertNotNull(nearestStation);
        System.out.println("StationId: " + nearestStation.getStationId());
    }
}
