package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ca.datamagic.noaa.dao.DWMLDAO;
import ca.datamagic.noaa.dto.DWMLDTO;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLTask extends AsyncTaskBase<Void, Void, DWMLDTO> {
    private Logger _logger = LogManager.getLogger(DWMLTask.class);
    private DWMLDAO _dao = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;
    private String _unit = "e";

    public DWMLTask(double latitude, double longitude, String unit) {
        _dao = new DWMLDAO();
        _latitude = latitude;
        _longitude = longitude;
        _unit = unit;
    }

    @Override
    protected AsyncTaskResult<DWMLDTO> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            return new AsyncTaskResult<DWMLDTO>(_dao.load(_latitude, _longitude, _unit));
        } catch (Throwable t) {
            return new AsyncTaskResult<DWMLDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<DWMLDTO> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Completed Task");
        }
        fireCompleted(result);
    }
}
