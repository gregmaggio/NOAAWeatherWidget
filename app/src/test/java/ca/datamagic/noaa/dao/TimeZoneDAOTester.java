package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.TimeZone;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;

@RunWith(JUnit4.class)
public class TimeZoneDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        TimeZoneDAO dao = new TimeZoneDAO();
        String timeZoneId = dao.loadTimeZone(39.0103,-76.9124);
        System.out.println("timeZoneId: " + timeZoneId);
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        System.out.println("timeZone: " + timeZone);
    }
}
