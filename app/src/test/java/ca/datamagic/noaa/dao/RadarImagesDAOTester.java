package ca.datamagic.noaa.dao;

import org.junit.Test;

import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;

public class RadarImagesDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        RadarImagesDAO dao = new RadarImagesDAO();
        List<String> radarImages = dao.loadRadarImages("LWX");
        System.out.println(radarImages);
    }
}
