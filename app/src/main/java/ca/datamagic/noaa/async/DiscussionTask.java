package ca.datamagic.noaa.async;

import android.util.Log;

import ca.datamagic.noaa.dao.DiscussionDAO;

/**
 * Created by Greg on 1/3/2016.
 */
public class DiscussionTask extends AsyncTaskBase<Void, Void, String> {
    private static final String _tag = "DiscussionTask";
    private static final int _maxTries = 5;
    private DiscussionDAO _dao = null;
    private String _wfo = null;

    public DiscussionTask(String wfo) {
        _dao = new DiscussionDAO();
        _wfo = wfo;
    }

    private String load() {
        int tries = 0;
        while (tries < _maxTries) {
            try {
                return _dao.load(_wfo);
            } catch (Throwable t) {
                Log.w(_tag, "Exception", t);
            }
            ++tries;
        }
        return null;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        Log.d(_tag, "Running Task");
        try {
            return new AsyncTaskResult<String>(load());
        } catch (Throwable t) {
            return new AsyncTaskResult<String>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        Log.d(_tag, "Completed Task");
        if (result.getThrowable() != null) {
            Log.e(_tag, "Exception", result.getThrowable());
        }
        FireCompleted(result);
    }
}
