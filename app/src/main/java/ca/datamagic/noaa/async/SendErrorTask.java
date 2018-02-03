package ca.datamagic.noaa.async;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.ErrorDAO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 4/10/2017.
 */

public class SendErrorTask extends AsyncTaskBase<Void, Void, Void> {
    private Logger _logger = LogFactory.getLogger(SendErrorTask.class);
    private String _logPath = null;
    private String _mailFrom = null;
    private String _mailSubject = null;
    private String _mailBody = null;

    public SendErrorTask(String logPath, String mailFrom, String mailSubject, String mailBody) {
        _logPath = logPath;
        _mailFrom = mailFrom;
        _mailSubject = mailSubject;
        _mailBody = mailBody;
    }

    public static List<String> tailFile(String fileName, int linesToRead) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");
        try {
            long lengthOfFile = randomAccessFile.length();
            long counterFromEnd = 1L;
            long newlineCounterGoal = linesToRead;
            int newlineCounter = 0;
            long tailPosition = 0L; // start of the end ;-)

            // If you want to get the last 10 lines,
            // and the last line ends with a newline, then you need to count back 11 newlines
            // if there is no trailing newline, then you only need to count back 10
            randomAccessFile.seek(lengthOfFile - 1L);
            char currentChar = (char) randomAccessFile.readByte();
            if (currentChar == '\n') {
                newlineCounterGoal++;
            }

            while (counterFromEnd <= lengthOfFile) {
                if ((lengthOfFile - counterFromEnd) < 0) {
                    break;
                }
                randomAccessFile.seek(lengthOfFile - counterFromEnd);
                if (randomAccessFile.readByte() == '\n') {
                    newlineCounter++;
                }
                tailPosition = randomAccessFile.getFilePointer();
                if (newlineCounter == newlineCounterGoal) {
                    break;
                }
                counterFromEnd++;
            }
            randomAccessFile.seek(tailPosition);

            String line;
            List<String> lines = new ArrayList<String>();
            int nLine = 0;
            while ((line = randomAccessFile.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } finally {
            randomAccessFile.close();
        }
    }

    @Override
    protected AsyncTaskResult<Void> doInBackground(Void... params) {
        _logger.info("Sending error...");
        try {
            StringBuffer mailBody = new StringBuffer(_mailBody);

            File newestLogFile = null;
            File logDirectory = new File(_logPath);
            if (logDirectory.exists()) {
                File[] logFiles = logDirectory.listFiles();
                if ((logFiles != null) && (logFiles.length > 0)) {
                    mailBody.append("\n\nLog Files:\n");
                    for (int ii = 0; ii < logFiles.length; ii++) {
                        String fileName = logFiles[ii].getName();
                        if (fileName.toLowerCase().contains(".txt") && !fileName.toLowerCase().contains(".lck")) {
                            mailBody.append(fileName + "\n");
                            if (newestLogFile != null) {
                                if (newestLogFile.lastModified() < logFiles[ii].lastModified()) {
                                    newestLogFile = logFiles[ii];
                                }
                            } else {
                                newestLogFile = logFiles[ii];
                            }
                        }
                    }
                }
            }
            if (newestLogFile != null) {
                List<String> lines = tailFile(newestLogFile.getAbsolutePath(), 100);
                mailBody.append("\n\n" + newestLogFile.getName() + " Tail:\n");
                for (String line : lines) {
                    mailBody.append("\n" + line + "\n");
                }
            }
            ErrorDAO dao = new ErrorDAO();
            dao.sendError(_mailFrom, _mailSubject, mailBody.toString());

            return new AsyncTaskResult<Void>();
        } catch (Throwable t) {
            return new AsyncTaskResult<Void>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Void> result) {
        _logger.info("...error sent.");
        fireCompleted(result);
    }
}
