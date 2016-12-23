package ca.datamagic.noaa.widget;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.ObservationTask;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 12/23/2016.
 */
public class ObservationRefresh implements AsyncTaskListener<DWMLDTO> {
    private static final int _maxTries = 5;
    private ObservationTask _task = null;
    private ObservationFragment _fragment = null;
    private int _tries = 0;

    public ObservationRefresh(double latitude, double longitude, String unit, ObservationFragment observationFragment) {
        _fragment = observationFragment;
        _task = new ObservationTask(latitude, longitude, unit);
        _task.addListener(this);
        _task.execute((Void[]) null);
    }

    @Override
    public void Completed(AsyncTaskResult<DWMLDTO> result) {
        if (result.getThrowable() != null) {
            if (++_tries < _maxTries) {
                _task.execute((Void[]) null);
            }
        } else {
            _fragment.render(result.getResult());
        }
    }
}
