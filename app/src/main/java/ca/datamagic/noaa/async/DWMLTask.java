package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.DWMLDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLTask extends AsyncTaskBase<Void, Void, DWMLDTO> {
    private static Logger _logger = LogFactory.getLogger(DWMLTask.class);
    private static DWMLDAO _dao = new DWMLDAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;
    private static String _unit = "e";

    public DWMLTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
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
