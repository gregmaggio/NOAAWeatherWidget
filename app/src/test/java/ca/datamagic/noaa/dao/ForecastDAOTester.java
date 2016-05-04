package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 12/31/2015.
 */
@RunWith(JUnit4.class)
public class ForecastDAOTester {
    @Test
    public void test1() throws Exception {
        ForecastDAO dao = new ForecastDAO();
        //String text = dao.GetLatLonListForCityNames(4);
        //String text = dao.GetLatLonListForZipCodes("20740");
        DWMLDTO dto = dao.GetForecastByDay(39.32, -94.72, 2015, 12, 31, 7, "e", "24 hourly");
        //result.getResult().getHead().getSource().getProductionCenter().getDescription()
        //result.getResult().getHead().getSource().getCreditLink()
        //result.getResult().getData().getLocation().getLocationKey()

    }
}
