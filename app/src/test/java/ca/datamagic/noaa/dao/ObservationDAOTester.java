package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 1/6/2016.
 */
@RunWith(JUnit4.class)
public class ObservationDAOTester {
    @Test
    public void test1() throws Exception {
        ObservationDAO dao = new ObservationDAO();
        DWMLDTO dwml = dao.GetCurrentObservation(39.32, -94.72, null);
    }
}
