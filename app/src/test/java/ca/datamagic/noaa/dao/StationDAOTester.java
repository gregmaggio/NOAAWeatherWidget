package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.StationDTO;

@RunWith(JUnit4.class)
public class StationDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        StationDAO dao = new StationDAO();
        StationDTO station = dao.read(39.0103,-76.9124);
        System.out.println("station: " + station);
    }
}
