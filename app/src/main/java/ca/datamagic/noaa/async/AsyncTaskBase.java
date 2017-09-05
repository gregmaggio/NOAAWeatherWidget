package ca.datamagic.noaa.async;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 1/9/2016.
 */
public abstract class AsyncTaskBase<Params, Progress, Result> extends AsyncTask<Params, Progress, AsyncTaskResult<Result>> {
    private List<AsyncTaskListener<Result>> _listeners = new ArrayList<AsyncTaskListener<Result>>();

    public void addListener(AsyncTaskListener<Result> listener) {
        _listeners.add(listener);
    }

    public void removeListener(AsyncTaskListener<Result> listener) {
        _listeners.remove(listener);
    }

    protected void fireCompleted(AsyncTaskResult<Result> result) {
        for (AsyncTaskListener<Result> listener : _listeners) {
            try {
                listener.completed(result);
            } catch (Throwable t) {
                // TODO
            }
        }
    }
}
