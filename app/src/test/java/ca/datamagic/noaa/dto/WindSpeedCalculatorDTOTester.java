package ca.datamagic.noaa.dto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

@RunWith(JUnit4.class)
public class WindSpeedCalculatorDTOTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(WindSpeedCalculatorDTOTester.class);

    @Test
    public void testHighWind() throws Throwable {
        Double initialKnots = 76.8;
        _logger.info("initialKnots: " + initialKnots);

        Double initialMph = WindSpeedCalculatorDTO.compute(initialKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.MilesPerHour);
        _logger.info("initialMph: " + initialMph);

        Double initialKph = WindSpeedCalculatorDTO.compute(initialKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.KilometersPerHour);
        _logger.info("initialKph: " + initialKph);

        Double computedKnots = WindSpeedCalculatorDTO.compute(initialMph, WindSpeedUnitsDTO.MilesPerHour, WindSpeedUnitsDTO.Knots);
        _logger.info("computedKnots: " + computedKnots);

        Assert.assertTrue(Math.abs(computedKnots - initialKnots) < 0.01);

        computedKnots = WindSpeedCalculatorDTO.compute(initialKph, WindSpeedUnitsDTO.KilometersPerHour, WindSpeedUnitsDTO.Knots);
        _logger.info("computedKnots: " + computedKnots);

        Assert.assertTrue(Math.abs(computedKnots - initialKnots) < 0.01);

        Double computedMph = WindSpeedCalculatorDTO.compute(computedKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.MilesPerHour);
        _logger.info("computedMph: " + computedMph);

        Assert.assertTrue(Math.abs(computedMph - initialMph) < 0.01);

        computedMph = WindSpeedCalculatorDTO.compute(initialKph, WindSpeedUnitsDTO.KilometersPerHour, WindSpeedUnitsDTO.MilesPerHour);
        _logger.info("computedMph: " + computedMph);

        Assert.assertTrue(Math.abs(computedMph - initialMph) < 0.01);

        Double computedKph = WindSpeedCalculatorDTO.compute(computedKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.KilometersPerHour);
        _logger.info("computedKph: " + computedKph);

        Assert.assertTrue(Math.abs(computedKph - initialKph) < 0.01);

        computedKph = WindSpeedCalculatorDTO.compute(initialMph, WindSpeedUnitsDTO.MilesPerHour, WindSpeedUnitsDTO.KilometersPerHour);
        _logger.info("computedKph: " + computedKph);

        Assert.assertTrue(Math.abs(computedKph - initialKph) < 0.01);
    }

    @Test
    public void testLowWind() throws Throwable {
        Double initialKnots = 3.4;
        _logger.info("initialKnots: " + initialKnots);

        Double initialMph = WindSpeedCalculatorDTO.compute(initialKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.MilesPerHour);
        _logger.info("initialMph: " + initialMph);

        Double initialKph = WindSpeedCalculatorDTO.compute(initialKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.KilometersPerHour);
        _logger.info("initialKph: " + initialKph);

        Double computedKnots = WindSpeedCalculatorDTO.compute(initialMph, WindSpeedUnitsDTO.MilesPerHour, WindSpeedUnitsDTO.Knots);
        _logger.info("computedKnots: " + computedKnots);

        Assert.assertTrue(Math.abs(computedKnots - initialKnots) < 0.01);

        computedKnots = WindSpeedCalculatorDTO.compute(initialKph, WindSpeedUnitsDTO.KilometersPerHour, WindSpeedUnitsDTO.Knots);
        _logger.info("computedKnots: " + computedKnots);

        Assert.assertTrue(Math.abs(computedKnots - initialKnots) < 0.01);

        Double computedMph = WindSpeedCalculatorDTO.compute(computedKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.MilesPerHour);
        _logger.info("computedMph: " + computedMph);

        Assert.assertTrue(Math.abs(computedMph - initialMph) < 0.01);

        computedMph = WindSpeedCalculatorDTO.compute(initialKph, WindSpeedUnitsDTO.KilometersPerHour, WindSpeedUnitsDTO.MilesPerHour);
        _logger.info("computedMph: " + computedMph);

        Assert.assertTrue(Math.abs(computedMph - initialMph) < 0.01);

        Double computedKph = WindSpeedCalculatorDTO.compute(computedKnots, WindSpeedUnitsDTO.Knots, WindSpeedUnitsDTO.KilometersPerHour);
        _logger.info("computedKph: " + computedKph);

        Assert.assertTrue(Math.abs(computedKph - initialKph) < 0.01);

        computedKph = WindSpeedCalculatorDTO.compute(initialMph, WindSpeedUnitsDTO.MilesPerHour, WindSpeedUnitsDTO.KilometersPerHour);
        _logger.info("computedKph: " + computedKph);

        Assert.assertTrue(Math.abs(computedKph - initialKph) < 0.01);
    }
}
