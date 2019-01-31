package ca.datamagic.noaa.dao;

import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.MultipartUtility;
import ca.datamagic.noaa.util.ThreadEx;

/**
 * Created by Greg on 4/11/2017.
 */
public class ErrorDAO {
    private static Logger _logger = LogFactory.getLogger(ErrorDAO.class);
    private static int _maxTries = 5;
    private static int _retryTimeoutMillis = 500;

    public void sendError(String mailFrom, String mailSubject, String mailBody) throws Throwable {
        _logger.info("sendError");
        _logger.info("mailFrom: " + mailFrom);
        _logger.info("mailSubject: " + mailSubject);
        _logger.info("mailBody: " + mailBody);
        Throwable lastError = null;
        for (int ii = 0; ii < _maxTries; ii++) {
            try {
                MultipartUtility multipartUtility = new MultipartUtility("http://env-5616586.jelastic.servint.net/api/error");
                multipartUtility.addFormField("mailFrom", mailFrom);
                multipartUtility.addFormField("mailSubject", mailSubject);
                multipartUtility.addFormField("mailBody", mailBody);
                multipartUtility.finish();
                break;
            } catch (Throwable t) {
                lastError = t;
                _logger.warning("Exception: " + t.getMessage());
                ThreadEx.sleep(_retryTimeoutMillis);
            }
        }
        if (lastError != null)
            throw lastError;
    }
}
