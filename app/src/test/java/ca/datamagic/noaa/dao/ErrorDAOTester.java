package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by Greg on 4/11/2017.
 */
@RunWith(JUnit4.class)
public class ErrorDAOTester {
    @Test
    public void test1() throws Throwable {
        ErrorDAO dao = new ErrorDAO();
        dao.sendError("GregMaggio@HotMail.com", "Test", "Test", "C:/Temp/Logs/WFO/logs.zip");
    }
}
