package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 2/18/2017.
 */
@RunWith(JUnit4.class)
public class DWMLDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DWMLDAO dao = new DWMLDAO();
        DWMLDTO dwml = dao.load(33.81167, -118.14639, "e");
    }
}
