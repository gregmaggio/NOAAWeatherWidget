package ca.datamagic.noaa.async;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Hashtable;

import ca.datamagic.noaa.dao.ImageDAO;

/**
 * Created by Greg on 1/1/2016.
 */
public class ImageTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static Hashtable<String, Bitmap> _cachedImages = new Hashtable<String, Bitmap>();
    private Logger _logger = LogManager.getLogger(ImageTask.class);
    private ImageDAO _dao = null;
    private String _imageUrl = null;
    private ImageView _imageView = null;

    public ImageTask(String imageUrl, ImageView imageView) {
        _dao = new ImageDAO();
        _imageUrl = imageUrl;
        _imageView = imageView;
    }

    private static synchronized Bitmap getBitmap(String imageUrl) {
        String key = imageUrl.toLowerCase();
        if (_cachedImages.containsKey(key)) {
            return _cachedImages.get(key);
        }
        return null;
    }

    private static synchronized void setBitmap(String imageUrl, Bitmap newVal) {
        String key = imageUrl.toLowerCase();
        _cachedImages.put(key, newVal);
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            Bitmap bitmap = null;
            if ((_imageUrl != null) && (_imageUrl.length() > 0)) {
                bitmap = getBitmap(_imageUrl);
                if (bitmap == null) {
                    bitmap = _dao.load(_imageUrl);
                    if (bitmap != null) {
                        setBitmap(_imageUrl, bitmap);
                    }
                }
            }
            return new AsyncTaskResult<Bitmap>(bitmap);
        } catch (Throwable t) {
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Completed Task");
        }
        if (result.getResult() != null) {
            _imageView.setImageBitmap(result.getResult());
        }
        fireCompleted(result);
    }
}
