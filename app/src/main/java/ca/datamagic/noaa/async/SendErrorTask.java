package ca.datamagic.noaa.async;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    private byte[] getFileContents(File file) throws IOException {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);

            List<String> lines = new ArrayList<String>();
            String currentLine = null;
            while ((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
            }

            List<Byte> bytes = new ArrayList<Byte>();
            int startIndex = 0;
            if (lines.size() > 200) {
                startIndex = lines.size() - 200 - 1;
            }
            byte[] crlf = "\r\n".getBytes();
            for (int ii = startIndex; ii < lines.size(); ii++) {
                if (ii > startIndex) {
                    for (int jj = 0; jj < crlf.length; jj++) {
                        bytes.add(new Byte((crlf[jj])));
                    }
                }
                byte[] lineBytes = lines.get(ii).getBytes();
                for (int jj = 0; jj < lineBytes.length; jj++) {
                    bytes.add(new Byte(lineBytes[jj]));
                }
            }

            byte[] buffer = new byte[bytes.size()];
            for (int ii = 0; ii < bytes.size(); ii++) {
                buffer[ii] = bytes.get(ii).byteValue();
            }
            return buffer;
        } catch (Throwable t) {
            _logger.log(Level.WARNING, "Error retrieving file contents: " + file.getName(), t);
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    @Override
    protected AsyncTaskResult<Void> doInBackground(Void... params) {
        _logger.info("Sending error...");
        String zipFileName = null;
        ZipOutputStream zipOutputStream = null;
        FileInputStream fileInputStream = null;
        try {
            zipFileName = MessageFormat.format("{0}/logs.zip", _logPath);
            File zipFile = new File(zipFileName);
            if (zipFile.exists()) {
                zipFile.delete();
            }

            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_MONTH, -1);

            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            File logDirectory = new File(_logPath);
            if (logDirectory.exists()) {
                File[] logFiles = logDirectory.listFiles();
                if (logFiles != null) {
                    for (int ii = 0; ii < logFiles.length; ii++) {
                        String fileName = logFiles[ii].getName();
                        if (fileName.toLowerCase().contains(".txt") && !fileName.toLowerCase().contains(".lck")) {
                            if (logFiles[ii].lastModified() > yesterday.getTimeInMillis()) {
                                byte[] fileContents = getFileContents(logFiles[ii]);
                                if (fileContents != null) {
                                    ZipEntry zipEntry = new ZipEntry(logFiles[ii].getName());
                                    zipOutputStream.putNextEntry(zipEntry);
                                    zipOutputStream.write(fileContents, 0, fileContents.length);
                                    zipOutputStream.closeEntry();
                                }
                            }
                        }
                    }
                }
            }

            zipOutputStream.close();
            zipOutputStream = null;

            ErrorDAO dao = new ErrorDAO();
            dao.sendError(_mailFrom, _mailSubject, _mailBody, zipFileName);

            return new AsyncTaskResult<Void>();
        } catch (Throwable t) {
            return new AsyncTaskResult<Void>(t);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Throwable t) {
                    _logger.log(Level.WARNING, "Error closing file input stream." , t);
                }
            }
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (Throwable t) {
                    _logger.log(Level.WARNING, "Error closing zip output stream." , t);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Void> result) {
        _logger.info("...error sent.");
        fireCompleted(result);
    }
}
