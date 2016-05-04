package ca.datamagic.noaa.async;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.util.Hashtable;

import ca.datamagic.noaa.dao.ImageDAO;

/**
 * Created by Greg on 1/1/2016.
 */
public class ImageTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static final String _tag = "ImageTask";
    private static final int _maxTries = 5;
    private static Hashtable<String, Bitmap> _cachedImages = new Hashtable<String, Bitmap>();
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

    private Bitmap load() {
        int tries = 0;
        while (tries < _maxTries) {
            try {
                return _dao.load(_imageUrl);
            } catch (Throwable t) {
                Log.w(_tag, "Exception", t);
            }
            ++tries;
        }
        return null;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        Log.d(_tag, "Running Task");
        try {
            Bitmap bitmap = null;
            if ((_imageUrl != null) && (_imageUrl.length() > 0)) {
                bitmap = getBitmap(_imageUrl);
                if (bitmap == null) {
                    bitmap = load();
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
        Log.d(_tag, "Completed Task");
        if (result.getResult() != null) {
            _imageView.setImageBitmap(result.getResult());
        } else if (result.getThrowable() != null) {
            Log.e(_tag, "Exception", result.getThrowable());
        }
        FireCompleted(result);
    }
}
