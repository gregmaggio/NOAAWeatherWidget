package ca.datamagic.noaa.widget;

import java.util.List;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.WFOTask;
import ca.datamagic.noaa.dto.WFODTO;

/**
 * Created by Greg on 12/23/2016.
 */
public class WFORefresh implements AsyncTaskListener<List<WFODTO>> {
    private static final int _maxTries = 5;
    private DiscussionFragment _discussionFragment = null;
    private WFOTask _task = null;
    private int _tries = 0;

    public WFORefresh(double latitude, double longitude, DiscussionFragment discussionFragment) {
        _discussionFragment = discussionFragment;
        _task = new WFOTask(latitude, longitude);
        _task.addListener(this);
        _task.execute((Void[]) null);
    }

    @Override
    public void Completed(AsyncTaskResult<List<WFODTO>> result) {
        if (result.getThrowable() != null) {
            if (++_tries < _maxTries) {
        _task.execute((Void[]) null);
            }
        } else {
            if (result.getResult().size() > 0) {
                new DicussionRefresh(result.getResult().get(0).getWFO(), _discussionFragment);
            }
        }
    }
}
