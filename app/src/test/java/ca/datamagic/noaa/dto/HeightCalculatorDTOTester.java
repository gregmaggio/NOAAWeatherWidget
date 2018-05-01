package ca.datamagic.noaa.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

@RunWith(JUnit4.class)
public class HeightCalculatorDTOTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(HeightCalculatorDTOTester.class);

    @Test
    public void test1() throws Throwable {
        Double initialFeet = 45.0;
        _logger.info("initialFeet: " + initialFeet);

        Double initialMeters = HeightCalculatorDTO.compute(initialFeet, HeightUnitsDTO.Feet, HeightUnitsDTO.Meters);
        _logger.info("initialMeters: " + initialMeters);

        Double computedFeet = HeightCalculatorDTO.compute(initialMeters, HeightUnitsDTO.Meters, HeightUnitsDTO.Feet);
        _logger.info("computedFeet: " + computedFeet);

        Double computedMeters = HeightCalculatorDTO.compute(computedFeet, HeightUnitsDTO.Feet, HeightUnitsDTO.Meters);
        _logger.info("computedMeters: " + computedMeters);

        Assert.assertTrue(Math.abs(computedFeet - initialFeet) < 0.01);
        Assert.assertTrue(Math.abs(computedMeters - initialMeters) < 0.01);
    }
}
