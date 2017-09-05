package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.DWMLDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLTask extends AsyncTaskBase<Void, Void, DWMLDTO> {
    private Logger _logger = LogFactory.getLogger(DWMLTask.class);
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
        _logger.info("Loading DWML...");
        try {
            return new AsyncTaskResult<DWMLDTO>(_dao.load(_latitude, _longitude, _unit));
        } catch (Throwable t) {
            return new AsyncTaskResult<DWMLDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<DWMLDTO> result) {
        _logger.info("...DWML loaded.");
        fireCompleted(result);
    }
}
