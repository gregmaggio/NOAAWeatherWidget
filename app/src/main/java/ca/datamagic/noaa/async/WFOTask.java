package ca.datamagic.noaa.async;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.WFODAO;
import ca.datamagic.noaa.dto.WFODTO;
import ca.datamagic.noaa.logging.LogFactory;

public class WFOTask extends AsyncTaskBase<Void, Void, WFODTO> {
    private static Logger _logger = LogFactory.getLogger(WFOTask.class);
    private static WFODAO _dao = new WFODAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public WFOTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<WFODTO> doInBackground(Void... params) {
        try {
            _logger.info("Loading WFO...");
            List<WFODTO> list = _dao.read(_latitude, _longitude);
            if ((list == null) || (list.size() < 1)) {
                return new AsyncTaskResult<WFODTO>();
            }
            return new AsyncTaskResult<WFODTO>(list.get(0));
        } catch (Throwable t) {
            return new AsyncTaskResult<WFODTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<WFODTO> result) {
        _logger.info("...wfo loaded.");
        fireCompleted(result);
    }
}
