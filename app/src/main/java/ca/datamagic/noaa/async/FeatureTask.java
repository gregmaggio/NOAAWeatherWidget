package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.APIDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class FeatureTask extends AsyncTaskBase<Void, Void, FeatureDTO> {
    private static Logger _logger = LogFactory.getLogger(FeatureTask.class);
    private static APIDAO _dao = new APIDAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public FeatureTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<FeatureDTO> doInBackground(Void... params) {
        _logger.info("Loading Feature...");
        try {
            FeatureDTO feature = _dao.loadFeature(_latitude, _longitude);
            return new AsyncTaskResult<FeatureDTO>(feature);
        } catch (Throwable t) {
            return new AsyncTaskResult<FeatureDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<FeatureDTO> result) {
        _logger.info("...Feature loaded.");
        fireCompleted(result);
    }
}
