package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.DiscussionDAO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/3/2016.
 */
public class DiscussionTask extends AsyncTaskBase<Void, Void, String> {
    private static Logger _logger = LogFactory.getLogger(DiscussionTask.class);
    private static DiscussionDAO _dao = new DiscussionDAO();
    private String _wfo = null;

    public DiscussionTask() {

    }

    public void setWFO(String newVal) {
        _wfo = newVal;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        try {
            if ((_wfo == null) || (_wfo.length() < 1)) {
                return new AsyncTaskResult<String>("");
            }
            _logger.info("Loading discussion...");
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
