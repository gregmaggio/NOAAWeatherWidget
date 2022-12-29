package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.FeatureDTO;

@RunWith(JUnit4.class)
public class APIDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        APIDAO dao = new APIDAO();
        FeatureDTO feature = dao.loadFeature(32.65829086303711,-97.0950927734375);
        System.out.println("feature: " + feature);
        FeatureDTO forecast = dao.loadForecast(feature);
        System.out.println("forecast: " + forecast);
        FeatureDTO hourlyForecast = dao.loadForecastHourly(feature);
        System.out.println("hourlyForecast: " + hourlyForecast);
    }
}
