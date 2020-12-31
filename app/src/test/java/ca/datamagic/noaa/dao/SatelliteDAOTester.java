package ca.datamagic.noaa.dao;

import org.junit.Test;

import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;

public class SatelliteDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        List<String> satelliteImages = SatelliteDAO.loadSatelliteImages("MD");
        System.out.println(satelliteImages);
    }
}
