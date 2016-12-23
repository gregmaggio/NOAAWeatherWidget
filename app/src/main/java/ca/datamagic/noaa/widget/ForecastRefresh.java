package ca.datamagic.noaa.widget;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.ForecastTask;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 12/23/2016.
 */
public class ForecastRefresh implements AsyncTaskListener<DWMLDTO> {
    private static final int _maxTries = 5;
    private ForecastTask _task = null;
    private ForecastFragment _fragment = null;
    private int _tries = 0;

    public ForecastRefresh(double latitude, double longitude, int year, int month, int day, int numDays, String unit, String format, ForecastFragment fragment) {
        _fragment = fragment;
        _task = new ForecastTask(latitude, longitude, year, month, day, numDays, unit, format);
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
