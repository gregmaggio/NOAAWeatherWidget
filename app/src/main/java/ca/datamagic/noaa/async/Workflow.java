package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 2/12/2017.
 */

public class Workflow implements WorkflowStep.WorkflowStepListener {
    private static Logger _logger = LogManager.getLogger(Workflow.class);
    private List<WorkflowStep> _steps = new ArrayList<WorkflowStep>();
    private int _currentStep = 0;
    private List<WorkflowListener> _listeners = new ArrayList<WorkflowListener>();

    public void addListener(WorkflowListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(WorkflowListener listener) {
        _listeners.remove(listener);
    }

    public void fireCompleted(boolean success) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).completed(success);
            } catch (Throwable t) {
                _logger.warn("Exception", t);
            }
        }
    }

    public void addStep(WorkflowStep step) {
        step.addListener(this);
        _steps.add(step);
    }

    public void start() {
        _currentStep = 0;
        _steps.get(_currentStep).execute();
    }

    @Override
    public void pass(Object sender) {
        ++_currentStep;
        if (_currentStep < _steps.size()) {
            _steps.get(_currentStep).execute();
        } else {
            fireCompleted(true);
        }
    }

    @Override
    public void drop(Object sender) {
        fireCompleted(false);
    }

    public interface WorkflowListener {
        public void completed(boolean success);
    }
}
