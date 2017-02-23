package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 2/12/2017.
 */

public class WorkflowStep<Params, Progress, Result> {
    private static Logger _logger = LogManager.getLogger(WorkflowStep.class);
    private AsyncTaskBase<Params, Progress, Result> _task = null;
    private AsyncTaskListener<Result> _listener = null;
    public List<WorkflowStepListener> _listeners = null;

    public WorkflowStep(AsyncTaskBase<Params, Progress, Result> task, AsyncTaskListener<Result> listener) {
        _task = task;
        _listener = listener;
        _listeners = new ArrayList<WorkflowStepListener>();
    }

    public void addListener(WorkflowStepListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(WorkflowStepListener listener) {
        _listeners.remove(listener);
    }

    private void firePass() {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).pass(this);
            } catch (Throwable t) {
                _logger.warn("Exception", t);
            }
        }
    }

    private void fireDrop() {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).drop(this);
            } catch (Throwable t) {
                _logger.warn("Exception", t);
            }
        }
    }

    public void execute() {
        _task.addListener(new AsyncTaskListener<Result>() {
            @Override
            public void completed(AsyncTaskResult<Result> result) {
                _listener.completed(result);
                if (result.getThrowable() != null) {
                    fireDrop();
                } else {
                    firePass();
                }
            }
        });
        _task.execute((Params[])null);
    }

    public interface WorkflowStepListener {
        public void pass(Object sender);
        public void drop(Object sender);
    }
}
