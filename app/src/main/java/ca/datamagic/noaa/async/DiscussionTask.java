package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.DiscussionDAO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/3/2016.
 */
public class DiscussionTask extends AsyncTaskBase<Void, Void, String> {
    private Logger _logger = LogFactory.getLogger(DiscussionTask.class);
    private DiscussionDAO _dao = null;
    private String _wfo = null;

    public DiscussionTask(String wfo) {
        _dao = new DiscussionDAO();
        _wfo = wfo;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        _logger.info("Loading discussion...");
        try {
            return new AsyncTaskResult<String>(_dao.load(_wfo));
        } catch (Throwable t) {
            return new AsyncTaskResult<String>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        _logger.info("...Discussion loaded.");
        fireCompleted(result);
    }
}
