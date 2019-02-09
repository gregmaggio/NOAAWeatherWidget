package ca.datamagic.noaa.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;

/**
 * Created by Greg on 4/8/2018.
 */
@RunWith(JUnit4.class)
public class ForecastsDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DWMLDAO dwmlDAO = new DWMLDAO();
        DWMLDTO dwml = dwmlDAO.load(42.93998, -78.73604, "e");
        ForecastsDTO forecasts = ForecastsDAO.getForecasts(dwml);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(forecasts);
        System.out.println(json);
    }
}
