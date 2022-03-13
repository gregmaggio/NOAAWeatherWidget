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
        StationDAO stationDAO = new StationDAO(new FileInputStream("C:/Dev/Android/NOAAWeatherWidget/app/src/main/res/raw/stations.csv"));
        StationDTO[] nearest = stationDAO.readNearest(34.1981981981982,-118.92222039020855, distance, units);
        Assert.assertNotNull(nearest);
    }
}
