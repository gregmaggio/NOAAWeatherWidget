package ca.datamagic.noaa.widget;

import java.text.MessageFormat;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.StationTask;
import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 12/23/2016.
 */
public class StationRefresh implements AsyncTaskListener<StationDTO> {
    private static final int _maxTries = 5;
    private SkewTFragment _skewTFragment = null;
    private StationTask _task = null;
    private int _tries = 0;

    public StationRefresh(double latitude, double longitude, SkewTFragment skewTFragment) {
        _skewTFragment = skewTFragment;
        _task = new StationTask(latitude, longitude);
        _task.addListener(this);
        _task.execute((Void[]) null);
    }
    @Override
    public void Completed(AsyncTaskResult<StationDTO> result) {
        if (result.getThrowable() != null) {
            if (++_tries < _maxTries) {
                _task.execute((Void[]) null);
            }
        } else {
            String skewTUrl = MessageFormat.format("http://weather.unisys.com/upper_air/skew/skew_{0}.gif", result.getResult().getStationId());
            _skewTFragment.render(skewTUrl);
        }
    }
}
