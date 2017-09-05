package ca.datamagic.noaa.async;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 2/12/2017.
 */

public class WorkflowStep<Params, Progress, Result> {
    private Logger _logger = LogFactory.getLogger(WorkflowStep.class);
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
                _logger.log(Level.WARNING, "Exception passing to a workflow step listener.", t);
            }
        }
    }

    private void fireDrop() {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).drop(this);
            } catch (Throwable t) {
                _logger.log(Level.WARNING, "Exception dropping to a workflow step listener.", t);
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
