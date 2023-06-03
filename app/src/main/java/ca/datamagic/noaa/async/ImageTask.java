package ca.datamagic.noaa.async;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.ImageDAO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;

/**
 * Created by Greg on 1/1/2016.
 */
public class ImageTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private Logger _logger = LogFactory.getLogger(ImageTask.class);
    private ImageDAO _dao = null;
    private String _imageUrl = null;
    private ImageView _imageView = null;
    private boolean _enableFileCache = true;

    public ImageTask(String imageUrl, ImageView imageView) {
        _dao = new ImageDAO();
        _imageUrl = imageUrl;
        _imageView = imageView;
    }

    public ImageTask(String imageUrl, ImageView imageView, boolean enableFileCache) {
        _dao = new ImageDAO(enableFileCache);
        _imageUrl = imageUrl;
        _imageView = imageView;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        _logger.info("Loading image: " + _imageUrl);
        try {
            Bitmap bitmap = _dao.load(_imageUrl);
            if (bitmap == null) {
                _logger.warning("Error loading: " + _imageUrl);
                int index = _imageUrl.lastIndexOf(',');
                if (index > -1) {
                    _imageUrl = _imageUrl.substring(0, index);
                    bitmap = _dao.load(_imageUrl);
                    if (bitmap == null) {
                        throw new Exception("Error loading " + _imageUrl);
                    }
                    return new AsyncTaskResult<Bitmap>(bitmap);
                }
            }
            return new AsyncTaskResult<Bitmap>(bitmap);
        } catch (Throwable t) {
            _logger.warning("Error loading: " + _imageUrl);
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        _logger.info("...image loaded.");
        if (result.getResult() != null) {
            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        _logger.info("Setting image bitmap");
                        _imageView.setImageBitmap(result.getResult());
                    } catch (Throwable t) {
                        _logger.warning("Throwable: " + t.getMessage());
                    }
                }
            });
        }
        fireCompleted(result);
    }
}
