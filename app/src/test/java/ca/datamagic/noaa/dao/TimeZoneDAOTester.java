package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;

@RunWith(JUnit4.class)
public class TimeZoneDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        TimeZoneDAO.setFilesPath(getFilesPath());
        TimeZoneDAO dao = new TimeZoneDAO();
        String timeZone = dao.getTimeZone(39.32, -94.72);
        System.out.println("timeZone: " + timeZone);
        String json = TimeZoneDAO.read();
        System.out.println("json: " + json);
    }
}
