package ca.datamagic.noaa.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.MessageFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Greg on 4/10/2017.
 */
@RunWith(JUnit4.class)
public class LogFormatterTester {
    @Test
    public void test1() throws Throwable {
        LogRecord record = new LogRecord(Level.FINE, "Test");
        LogFormatter formatter  = new LogFormatter();
        System.out.println(formatter.format(record));
    }

    @Test
    public void test2() throws Throwable {
        LogRecord record = new LogRecord(Level.FINE, "Test");
        record.setLoggerName("TestLogger");
        record.setMessage("TestMessage");
        record.setSourceClassName("LogFormatterTester");
        record.setSourceMethodName("test2");

        Throwable cause = new Throwable("Cause");
        Throwable t = new Throwable("Exception", cause);
        record.setThrown(t);

        LogFormatter formatter  = new LogFormatter();
        System.out.println(formatter.format(record));
    }

    @Test
    public void logToFileTest() throws Throwable {
        Logger testLogger = LogFactory.getLogger(LogFormatterTester.class);
        testLogger.info("testInfo");
        testLogger.warning("testWarning");
        testLogger.fine("testFine");
        testLogger.finer("testFiner");
        testLogger.finest("testFinest");
        testLogger.log(Level.INFO, "Info");
        testLogger.log(Level.WARNING, "Warning", new Throwable("Test"));
        testLogger.log(Level.SEVERE, "Severe", new Throwable("Test"));
    }
}
