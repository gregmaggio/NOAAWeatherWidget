package ca.datamagic.noaa.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Greg on 12/21/2016.
 */
public class LogHandler extends FileHandler {
    private static Object _lockObj = new Object();
    private static SimpleDateFormat _dateFormat = null;
    private static TimeZone _utcTimeZone = null;
    private String _pattern = null;

    static {
        synchronized (_lockObj) {
            _dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            _utcTimeZone = TimeZone.getTimeZone("UTC");
            _dateFormat.setTimeZone(_utcTimeZone);
            Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers.length > 0) {
                if (handlers[0] instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handlers[0]);
                }
            }
        }
    }

    public LogHandler() throws IOException {
    }

    public LogHandler(String pattern) throws IOException {
        super(pattern);
        _pattern = pattern;
    }

    public LogHandler(String pattern, boolean append) throws IOException {
        super(pattern, append);
        _pattern = pattern;
    }

    public LogHandler(String pattern, int limit, int count) throws IOException {
        super(pattern, limit, count);
        _pattern = pattern;
    }

    public LogHandler(String pattern, int limit, int count, boolean append) throws IOException {
        super(pattern, limit, count, append);
        _pattern = pattern;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        File file = new File(_pattern);
        if (file.exists()) {
            synchronized (_lockObj) {
                Date lastModified = new Date(file.lastModified());
                String lastModifiedString = _dateFormat.format(lastModified);
                Date now = new Date();
                String nowString = _dateFormat.format(now);
                if (lastModifiedString.compareToIgnoreCase(nowString) != 0) {
                    try {
                        // We need to roll the file
                        close();
                        String absolutePath = file.getAbsolutePath();
                        String newFileName = MessageFormat.format("{0}.{1}", absolutePath, lastModifiedString);
                        file.renameTo(new File(newFileName));
                        OutputStream outputStream = new FileOutputStream(absolutePath);
                        outputStream.write(LogFormatter.getHeader().getBytes());
                        outputStream.write("\n".getBytes());
                        setOutputStream(outputStream);
                    } catch (IOException ex) {
                        // Do Nothing
                    }
                }
            }
        }
        super.publish(record);
    }
}
