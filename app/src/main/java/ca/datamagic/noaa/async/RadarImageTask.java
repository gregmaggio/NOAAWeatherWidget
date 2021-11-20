package ca.datamagic.noaa.async;

import android.graphics.Bitmap;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarImageTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static final Logger _logger = LogFactory.getLogger(RadarImageTask.class);
    private String _imageUrl = null;

    public RadarImageTask(String imageUrl) {
        _imageUrl = imageUrl;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        try {
            _logger.info("Loading radar image ...");
            RadarDAO dao = new RadarDAO();
            Bitmap bitmap = dao.loadImage(_imageUrl);
            return new AsyncTaskResult<Bitmap>(bitmap);
        } catch (Throwable t) {
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        _logger.info("...radar image loaded.");
        fireCompleted(result);
    }
}
