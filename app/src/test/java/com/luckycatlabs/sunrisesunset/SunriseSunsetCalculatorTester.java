package com.luckycatlabs.sunrisesunset;

import com.luckycatlabs.sunrisesunset.dto.Location;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

public class SunriseSunsetCalculatorTester extends BaseTester {
    private static Logger _logger = LogFactory.getLogger(SunriseSunsetCalculatorTester.class);

    @Test
    public void buffaloTest() throws  Exception {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar today = Calendar.getInstance();
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(new Location("42.93998", "-78.73604"), "America/New_York");
        String sunrise = calculator.getOfficialSunriseForDate(today);
        String sunset = calculator.getOfficialSunsetForDate(today);
        _logger.info("Sunrise: " + sunrise);
        _logger.info("Sunset: " + sunset);
        Date sunriseTime = formatter.parse(sunrise);
        Date sunsetTime = formatter.parse(sunset);
        long durationInMillis = sunsetTime.getTime() - sunriseTime.getTime();
        int hours = (int)(durationInMillis / (60 * 60 * 1000));
        int minutes = (int)((durationInMillis - hours * 60 * 60 * 1000)/ (60 * 1000));
        _logger.info("Duration: " + hours + ":" + minutes);
    }
}
