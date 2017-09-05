package ca.datamagic.noaa.dao;

import java.io.File;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.MultipartUtility;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 4/11/2017.
 */
public class ErrorDAO {
    private Logger _logger = LogFactory.getLogger(ErrorDAO.class);
    private static int _maxTries = 5;

    public void sendError(String mailFrom, String mailSubject, String mailBody, String zipFileName) throws Throwable {
        _logger.info("sendError");
        _logger.info("mailFrom: " + mailFrom);
        _logger.info("mailSubject: " + mailSubject);
        _logger.info("mailBody: " + mailBody);
        _logger.info("zipFileName: " + zipFileName);
        Throwable lastError = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                MultipartUtility multipartUtility = new MultipartUtility("http://datamagic.ca/api/error");
                multipartUtility.addFormField("mailFrom", mailFrom);
                multipartUtility.addFormField("mailSubject", mailSubject);
                multipartUtility.addFormField("mailBody", mailBody);
                multipartUtility.addFilePart("attachment", new File(zipFileName));
                multipartUtility.finish();
                break;
            } catch (Throwable t) {
                lastError = t;
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(500);
            }
        }
        if (lastError != null)
            throw lastError;
    }
}
