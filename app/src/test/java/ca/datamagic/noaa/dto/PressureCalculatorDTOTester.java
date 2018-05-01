package ca.datamagic.noaa.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

@RunWith(JUnit4.class)
public class PressureCalculatorDTOTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(PressureCalculatorDTOTester.class);

    @Test
    public void highPressureTest() throws Throwable {
        Double initialIn = 30.4;
        _logger.info("initialIn: " + initialIn);

        Double initialKpa = PressureCalculatorDTO.compute(initialIn, PressureUnitsDTO.InchesOfMercury, PressureUnitsDTO.KiloPascals);
        _logger.info("initialKpa: " + initialKpa);

        Double convertedIn = PressureCalculatorDTO.compute(initialKpa, PressureUnitsDTO.KiloPascals, PressureUnitsDTO.InchesOfMercury);
        _logger.info("convertedIn: " + convertedIn);

        Double convertedKpa = PressureCalculatorDTO.compute(convertedIn, PressureUnitsDTO.InchesOfMercury, PressureUnitsDTO.KiloPascals);
        _logger.info("convertedKpa: " + convertedKpa);

        Assert.assertTrue(Math.abs(convertedIn - initialIn) < 0.01);
        Assert.assertTrue(Math.abs(convertedKpa - initialKpa) < 0.01);
    }

    @Test
    public void lowPressureTest() throws Throwable {
        Double initialIn = 27.3;
        _logger.info("initialIn: " + initialIn);

        Double initialKpa = PressureCalculatorDTO.compute(initialIn, PressureUnitsDTO.InchesOfMercury, PressureUnitsDTO.KiloPascals);
        _logger.info("initialKpa: " + initialKpa);

        Double convertedIn = PressureCalculatorDTO.compute(initialKpa, PressureUnitsDTO.KiloPascals, PressureUnitsDTO.InchesOfMercury);
        _logger.info("convertedIn: " + convertedIn);

        Double convertedKpa = PressureCalculatorDTO.compute(convertedIn, PressureUnitsDTO.InchesOfMercury, PressureUnitsDTO.KiloPascals);
        _logger.info("convertedKpa: " + convertedKpa);

        Assert.assertTrue(Math.abs(convertedIn - initialIn) < 0.01);
        Assert.assertTrue(Math.abs(convertedKpa - initialKpa) < 0.01);
    }
}
