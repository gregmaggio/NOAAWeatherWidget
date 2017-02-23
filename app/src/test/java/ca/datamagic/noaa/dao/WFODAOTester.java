package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 1/3/2016.
 */
@RunWith(JUnit4.class)
public class WFODAOTester {
    @Test
    public void test1() throws Throwable {
        WFODAO dao = new WFODAO();
        List<WFODTO> items = dao.read(39.32, -94.72);
    }

    @Test
    public void test2() throws Throwable {
        WFODAO dao = new WFODAO();
        List<WFODTO> items = dao.list();
    }

    @Test
    public void test3() throws Throwable {
        WFODAO dao = new WFODAO();
        WFODTO wfo = dao.read("LWX");
    }
}
