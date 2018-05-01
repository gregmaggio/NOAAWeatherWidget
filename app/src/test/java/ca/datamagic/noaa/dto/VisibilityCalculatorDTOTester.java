package ca.datamagic.noaa.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

@RunWith(JUnit4.class)
public class VisibilityCalculatorDTOTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(VisibilityCalculatorDTOTester.class);

    @Test
    public void visibilityTest() throws  Throwable {
        Double initialSM = 5.0;
        _logger.info("initialSM: " + initialSM);

        Double initialM = VisibilityCalculatorDTO.compute(initialSM, VisibilityUnitsDTO.StatuteMiles, VisibilityUnitsDTO.Miles);
        _logger.info("initialM: " + initialM);

        Double initialKm = VisibilityCalculatorDTO.compute(initialSM, VisibilityUnitsDTO.StatuteMiles, VisibilityUnitsDTO.Kilometers);
        _logger.info("initialKm: " + initialKm);

        Double computedSM = VisibilityCalculatorDTO.compute(initialM, VisibilityUnitsDTO.Miles, VisibilityUnitsDTO.StatuteMiles);
        _logger.info("computedSM: " + computedSM);

        Assert.assertTrue(Math.abs(computedSM - initialSM) < 0.01);

        computedSM = VisibilityCalculatorDTO.compute(initialKm, VisibilityUnitsDTO.Kilometers, VisibilityUnitsDTO.StatuteMiles);
        _logger.info("computedSM: " + computedSM);

        Assert.assertTrue(Math.abs(computedSM - initialSM) < 0.01);

        Double computedM = VisibilityCalculatorDTO.compute(initialKm, VisibilityUnitsDTO.Kilometers, VisibilityUnitsDTO.Miles);
        _logger.info("computedM: " + computedM);

        Assert.assertTrue(Math.abs(computedM - initialM) < 0.01);
    }
}
