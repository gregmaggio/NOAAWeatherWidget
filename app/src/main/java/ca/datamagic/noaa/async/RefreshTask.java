package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;

/**
 * Created by Greg on 2/19/2017.
 */

public class RefreshTask extends AsyncTaskBase<Void, Void, Void> {
    private Logger _logger = LogFactory.getLogger(RefreshTask.class);
    private MainActivity _mainActivity = null;

    public RefreshTask(MainActivity mainActivity) {
        _mainActivity = mainActivity;
    }

    @Override
    protected AsyncTaskResult<Void> doInBackground(Void... params) {
        _logger.info("Refreshing views...");
        try {
            _mainActivity.actionRefresh();
            return new AsyncTaskResult<Void>();
        } catch (Throwable t) {
            return new AsyncTaskResult<Void>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Void> result) {
        _logger.info("...views refreshed.");
        fireCompleted(result);
    }
}
