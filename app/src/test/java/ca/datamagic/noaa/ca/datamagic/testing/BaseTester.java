package ca.datamagic.noaa.ca.datamagic.testing;

import org.junit.BeforeClass;

import java.util.logging.Level;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 11/24/2017.
 */

public class BaseTester {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogFactory.initialize(Level.ALL, "C:/Temp/Logs/NOAAWeatherWidget", false);
    }
}
