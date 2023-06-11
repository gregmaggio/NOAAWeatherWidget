package ca.datamagic.noaa.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;

/**
 * Created by Greg on 1/9/2016.
 */
public abstract class AsyncTaskBase<Result> implements Runnable {
    private static final Logger _logger = LogFactory.getLogger(AsyncTaskBase.class);
    private static final Executor _executor = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
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
                _logger.warning("Throwable: " + t.getMessage());
            }
        }
    }

    public void execute() {
        _executor.execute(this);
    }

    public void run() {
        try {
            AsyncTaskResult<Result> result = doInBackground();
            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        onPostExecute(result);
                    } catch (Throwable t) {
                        _logger.warning("Throwable: " + t.getMessage());
                    }
                }
            });
        } catch (Throwable t) {
            _logger.warning("Throwable: " + t.getMessage());
        }
    }

    protected abstract AsyncTaskResult<Result> doInBackground();

    protected abstract void onPostExecute(AsyncTaskResult<Result> result);
}
