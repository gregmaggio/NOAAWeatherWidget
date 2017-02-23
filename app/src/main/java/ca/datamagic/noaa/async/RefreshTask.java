package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ca.datamagic.noaa.widget.MainActivity;

/**
 * Created by Greg on 2/19/2017.
 */

public class RefreshTask extends AsyncTaskBase<Void, Void, Void> {
    private Logger _logger = LogManager.getLogger(RefreshTask.class);
    private MainActivity _mainActivity = null;

    public RefreshTask(MainActivity mainActivity) {
        _mainActivity = mainActivity;
    }

    @Override
    protected AsyncTaskResult<Void> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            _mainActivity.actionRefresh();
            return new AsyncTaskResult<Void>();
        } catch (Throwable t) {
            return new AsyncTaskResult<Void>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Void> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        fireCompleted(result);
    }
}
