package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 1/3/2016.
 */
@RunWith(JUnit4.class)
public class WFODAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        WFODAO.setFilesPath(getFilesPath());
        WFODAO dao = new WFODAO();
        List<WFODTO> items = dao.read(39.32, -94.72);
        String json = WFODAO.read();
        System.out.println("json: " + json);
    }

}
