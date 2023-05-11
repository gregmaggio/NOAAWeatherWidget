package ca.datamagic.noaa.logging;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Greg on 8/27/2017.
 */

public class LogFactory {
    private static Level _level = Level.ALL;
    private static String _logPath = "C:/Temp/Logs";
    private static FileHandler _fileHandler = null;
    private static AndroidLogHandler _androidLogHandler = null;

    static {
        initialize(Level.ALL, _logPath, false);
    }

    public static void initialize(Level level, String logPath, boolean initializeAndroidLogger) {
        _level = level;
        _logPath = logPath;
        try {
            File file = new File(logPath);
            if (file.exists()) {
                _fileHandler = new FileHandler(MessageFormat.format("{0}/NOAAWeatherWidget.txt", logPath), 54939910, 7, true);
                _fileHandler.setFormatter(new LogFormatter());
            }
        } catch (Throwable t) {
            _fileHandler = null;
            System.err.println("Exception: " + t.getMessage());
        }
        try {
            if (initializeAndroidLogger) {
                _androidLogHandler = new AndroidLogHandler();
            } else {
                _androidLogHandler = null;
            }
        } catch (Throwable t) {
            _androidLogHandler = null;
            System.err.println("Exception: " + t.getMessage());
        }
    }

    public static String getLogPath() {
        return _logPath;
    }

    public static Logger getLogger(Class classObj) {
        return getLogger(classObj.getSimpleName());
    }

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setLevel(_level);
        if (_fileHandler != null) {
            logger.addHandler(_fileHandler);
        }
        if (_androidLogHandler != null) {
            logger.addHandler(_androidLogHandler);
        }
        return logger;
    }
}
