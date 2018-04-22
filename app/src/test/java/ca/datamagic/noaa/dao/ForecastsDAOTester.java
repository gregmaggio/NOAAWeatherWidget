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
        StationDAO.setFilesPath(getFilesPath());
        DWMLDAO.setFilesPath(getFilesPath());
        StationDAO stationDAO = new StationDAO();
        DWMLDAO dwmlDAO = new DWMLDAO();
        //https://forecast.weather.gov/MapClick.php?lat=42.93998&lon=-78.73604&unit=0&lg=english&FcstType=dwml
        DWMLDTO dwml = dwmlDAO.load(42.93998, -78.73604, "e");
        ForecastsDTO forecasts = ForecastsDAO.getForecasts(dwml);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(forecasts);
        System.out.println(json);
        /*
        List<StationDTO> stations = stationDAO.list();
        for (int ii = 0; ii < stations.size(); ii++) {
            DWMLDTO dwml = dwmlDAO.load(stations.get(ii).getLatitude(), stations.get(ii).getLongitude(), "e");
            if (dwml != null) {
                List<ForecastDTO> forecasts = ForecastDAO.getForecast(dwml);
                String json = mapper.writeValueAsString(forecasts);
                System.out.println(json);
            } else {
                System.out.println("Station " + stations.get(ii).getStationId() + " [" + stations.get(ii).getStationName() + "] returned no DWML!");
            }
        }
        */
        //DWMLDTO dwml = dao.load(33.81167, -118.14639, "e");
    }
}
