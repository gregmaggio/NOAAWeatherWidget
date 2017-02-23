package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ca.datamagic.noaa.dao.DiscussionDAO;

/**
 * Created by Greg on 1/3/2016.
 */
public class DiscussionTask extends AsyncTaskBase<Void, Void, String> {
    private Logger _logger = LogManager.getLogger(DiscussionTask.class);
    private DiscussionDAO _dao = null;
    private String _wfo = null;

    public DiscussionTask(String wfo) {
        _dao = new DiscussionDAO();
        _wfo = wfo;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            return new AsyncTaskResult<String>(_dao.load(_wfo));
        } catch (Throwable t) {
            return new AsyncTaskResult<String>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        fireCompleted(result);
    }
}
