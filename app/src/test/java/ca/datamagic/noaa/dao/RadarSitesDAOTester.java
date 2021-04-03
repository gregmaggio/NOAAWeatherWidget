package ca.datamagic.noaa.dao;

import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarSitesDAOTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(RadarSitesDAOTester.class);

    @Test
    public void test1() throws Throwable {
        RadarSitesDAO dao = new RadarSitesDAO();
        List<RadarSiteDTO> sites = dao.load();
        _logger.info("sites: " + sites.size());
    }
}
