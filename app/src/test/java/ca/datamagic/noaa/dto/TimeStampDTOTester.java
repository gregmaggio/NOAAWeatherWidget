package ca.datamagic.noaa.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import ca.datamagic.noaa.ca.datamagic.testing.BaseTester;
import ca.datamagic.noaa.logging.LogFactory;

@RunWith(JUnit4.class)
public class TimeStampDTOTester extends BaseTester {
    private static final Logger _logger = LogFactory.getLogger(TimeStampDTOTester.class);

    @Test
    public void edtTest() throws Throwable {
        String timeZoneId = "America/New_York";
        _logger.info("timeZoneId: " + timeZoneId);

        String timeStamp = "2019-03-24T09:00:00-04:00";
        _logger.info("timeStamp: " + timeStamp);

        TimeStampDTO dto = new TimeStampDTO(timeZoneId);
        dto.setTimeStamp(timeStamp);
        _logger.info("DayOfWeek: " + dto.getDayOfWeek());
        _logger.info("12HourOfDay: " + dto.get12HourOfDay());
        _logger.info("24HourOfDay: " + dto.get24HourOfDay());
    }

    @Test
    public void pdtTest() throws Throwable {
        String timeZoneId = "America/Los_Angeles";
        _logger.info("timeZoneId: " + timeZoneId);

        String timeStamp = "2019-03-24T05:00:00-07:00";
        _logger.info("timeStamp: " + timeStamp);

        TimeStampDTO dto = new TimeStampDTO(timeZoneId);
        dto.setTimeStamp(timeStamp);
        _logger.info("DayOfWeek: " + dto.getDayOfWeek());
        _logger.info("12HourOfDay: " + dto.get12HourOfDay());
        _logger.info("24HourOfDay: " + dto.get24HourOfDay());
    }
}
