package ca.datamagic.noaa.async;

import android.graphics.Bitmap;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.ImageDAO;
import ca.datamagic.noaa.dao.SatelliteDAO;
import ca.datamagic.noaa.logging.LogFactory;

public class SatelliteTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static Logger _logger = LogFactory.getLogger(RadarBitmapsTask.class);
    private ImageDAO _dao = null;
    private String _state = null;

    public SatelliteTask(String state) {
        _dao = new ImageDAO(false);
        _state = state;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        _logger.info("Retrieving satellite bitmap...");
        try {
            String imageUrl = SatelliteDAO.getSatelliteImage(_state);
            Bitmap bitmap = _dao.load(imageUrl);
            return new AsyncTaskResult<Bitmap>(bitmap);
        } catch (Throwable t) {
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        _logger.info("...satellite bitmap retrieved.");
        fireCompleted(result);
    }
}
