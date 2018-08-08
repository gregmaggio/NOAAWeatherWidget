package ca.datamagic.noaa.async;

import android.graphics.Bitmap;

import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.ImageDAO;
import ca.datamagic.noaa.logging.LogFactory;

public class SkewTTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static Logger _logger = LogFactory.getLogger(SkewTTask.class);
    private ImageDAO _dao = new ImageDAO(false);
    private String _skewTStation = null;

    public SkewTTask() {

    }

    public SkewTTask(String skewTStation) {
        _skewTStation = skewTStation;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        _logger.info("Retrieving skewT...");
        try {
            Bitmap bitmap = null;
            if (_skewTStation != null) {
                bitmap = _dao.load(MessageFormat.format("http://weather.unisys.com/sites/default/files/mnt/webdata/upper_air/skew/skew_{0}.gif", _skewTStation));
            }
            return new AsyncTaskResult<Bitmap>(bitmap);
        } catch (Throwable t) {
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        _logger.info("...skewT retrieved.");
        fireCompleted(result);
    }
}
