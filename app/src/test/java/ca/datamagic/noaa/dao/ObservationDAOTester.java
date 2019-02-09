package ca.datamagic.noaa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.ObservationDTO;

/**
 * Created by Greg on 4/14/2018.
 */
@RunWith(JUnit4.class)
public class ObservationDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DWMLDAO dwmlDAO = new DWMLDAO();
        DWMLDTO dwml = dwmlDAO.load(33.81167, -118.14639, "e");
        ObservationDTO observation = ObservationDAO.getObservation(dwml);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(observation);
        System.out.println(json);
    }
}
