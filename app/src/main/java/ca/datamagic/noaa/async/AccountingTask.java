package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.AccountingDAO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;

public class AccountingTask extends AsyncTaskBase<Void> {
    private static Logger _logger = LogFactory.getLogger(AccountingTask.class);
    private AccountingDAO _dao = new AccountingDAO();
    private String _eventName = null;
    private String _eventMessage = null;

    public AccountingTask(String eventName, String eventMessage) {
        _eventName = eventName;
        _eventMessage = eventMessage;
    }

    @Override
    protected AsyncTaskResult<Void> doInBackground() {
        try {
            _logger.info("Posting accounting...");
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                _dao.post(mainActivity.getDeviceLatitude(), mainActivity.getDeviceLongitude(), _eventName, _eventMessage);
            }
            return new AsyncTaskResult<Void>();
        } catch (Throwable t) {
            return new AsyncTaskResult<Void>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Void> result) {
        _logger.info("...accounting posted.");
        fireCompleted(result);
    }
}
