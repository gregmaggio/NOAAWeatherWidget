package ca.datamagic.noaa.widget;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.DiscussionTask;

/**
 * Created by Greg on 12/23/2016.
 */
public class DicussionRefresh implements AsyncTaskListener<String> {
    private static final int _maxTries = 5;
    private DiscussionFragment _discussionFragment = null;
    private DiscussionTask _task = null;
    private int _tries = 0;

    public DicussionRefresh(String wfo, DiscussionFragment discussionFragment) {
        _discussionFragment = discussionFragment;
        _task = new DiscussionTask(wfo);
        _task.addListener(this);
        _task.execute((Void[]) null);
    }

    @Override
    public void Completed(AsyncTaskResult<String> result) {
        if (result.getThrowable() != null) {
            if (++_tries < _maxTries) {
                _task.execute((Void[]) null);
            }
        } else {
            _discussionFragment.render(result.getResult());
        }
    }
}
