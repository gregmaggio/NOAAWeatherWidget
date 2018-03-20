package ca.datamagic.noaa.ca.datamagic.testing;

import org.junit.BeforeClass;

import java.io.File;
import java.util.logging.Level;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 11/24/2017.
 */

public class BaseTester {
    private static String _tempPath = null;
    private static String _logPath = null;
    private static String _filesPath = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        File tempFile = new File("C:/Temp");
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        File logFile = new File("C:/Temp/Logs");
        if (!logFile.exists()) {
            logFile.mkdir();
        }
        File logNOAAWeatherWidgetFile = new File("C:/Temp/Logs/NOAAWeatherWidget");
        if (!logNOAAWeatherWidgetFile.exists()) {
            logNOAAWeatherWidgetFile.mkdir();
        }
        File filesFile = new File("C:/Temp/NOAAWeatherWidget");
        if (!filesFile.exists()) {
            filesFile.mkdir();
        }
        _tempPath = tempFile.getAbsolutePath();
        _logPath = logNOAAWeatherWidgetFile.getAbsolutePath();
        _filesPath = filesFile.getAbsolutePath();
        LogFactory.initialize(Level.ALL, _logPath, false);
    }

    public static String getTempPath() {
        return _tempPath;
    }

    public static String getLogPath() {
        return _logPath;
    }

    public static String getFilesPath() {
        return _filesPath;
    }
}
