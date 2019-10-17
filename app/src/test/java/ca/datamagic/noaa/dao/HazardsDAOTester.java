package ca.datamagic.noaa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.dto.DWMLDTO;

@RunWith(JUnit4.class)
public class HazardsDAOTester extends BaseTester {
    @Test
    public void test1() throws Throwable {
        DWMLDAO dwmlDAO = new DWMLDAO();
        DWMLDTO dwml = dwmlDAO.load(41.32, -105.67, "e");
        HazardsDAO dao = new HazardsDAO();
        List<String> hazardsList = dao.getHazards(dwml);
        if (hazardsList != null) {
            for (int ii = 0; ii < hazardsList.size(); ii++) {
                System.out.println("Hazard: " + hazardsList.get(ii));
            }
        }
    }
}
