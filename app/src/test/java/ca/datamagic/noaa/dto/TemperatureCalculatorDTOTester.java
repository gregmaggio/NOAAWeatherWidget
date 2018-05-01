package ca.datamagic.noaa.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

@RunWith(JUnit4.class)
public class TemperatureCalculatorDTOTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(TemperatureCalculatorDTOTester.class);

    @Test
    public void highTemperatureTest() throws Throwable {
        Double initialF = 115.6;
        _logger.info("initialF: " + initialF);

        Double initialC = TemperatureCalculatorDTO.compute(initialF, TemperatureUnitsDTO.Fahrenheit, TemperatureUnitsDTO.Celsius);
        _logger.info("initialC: " + initialC);

        Double convertedF = TemperatureCalculatorDTO.compute(initialC, TemperatureUnitsDTO.Celsius, TemperatureUnitsDTO.Fahrenheit);
        _logger.info("convertedF: " + convertedF);

        Double convertedC = TemperatureCalculatorDTO.compute(convertedF, TemperatureUnitsDTO.Fahrenheit, TemperatureUnitsDTO.Celsius);
        _logger.info("convertedC: " + convertedC);

        Assert.assertTrue(Math.abs(convertedF - initialF) < 0.01);
        Assert.assertTrue(Math.abs(convertedC - initialC) < 0.01);
    }

    @Test
    public void lowTemperatureTest() throws Throwable {
        Double initialF = 12.3;
        _logger.info("initialF: " + initialF);

        Double initialC = TemperatureCalculatorDTO.compute(initialF, TemperatureUnitsDTO.Fahrenheit, TemperatureUnitsDTO.Celsius);
        _logger.info("initialC: " + initialC);

        Double convertedF = TemperatureCalculatorDTO.compute(initialC, TemperatureUnitsDTO.Celsius, TemperatureUnitsDTO.Fahrenheit);
        _logger.info("convertedF: " + convertedF);

        Double convertedC = TemperatureCalculatorDTO.compute(convertedF, TemperatureUnitsDTO.Fahrenheit, TemperatureUnitsDTO.Celsius);
        _logger.info("convertedC: " + convertedC);

        Assert.assertTrue(Math.abs(convertedF - initialF) < 0.01);
        Assert.assertTrue(Math.abs(convertedC - initialC) < 0.01);
    }
}
