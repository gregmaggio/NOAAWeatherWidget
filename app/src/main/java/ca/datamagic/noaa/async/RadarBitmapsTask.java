package ca.datamagic.noaa.async;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.ImageDAO;
import ca.datamagic.noaa.dto.BitmapsDTO;
import ca.datamagic.noaa.dto.StringListDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarBitmapsTask extends AsyncTaskBase<Void, Void, BitmapsDTO> {
    private static Logger _logger = LogFactory.getLogger(RadarBitmapsTask.class);
    private ImageDAO _dao = null;
    private StringListDTO _radarImages = null;

    public RadarBitmapsTask() {
        _dao = new ImageDAO();
    }

    public RadarBitmapsTask(boolean enableFileCache) {
        _dao = new ImageDAO(enableFileCache);
    }

    public RadarBitmapsTask(StringListDTO radarImages, boolean enableFileCache) {
        _dao = new ImageDAO(enableFileCache);
        _radarImages = radarImages;
    }

    public void setRadarImages(StringListDTO newVal) {
        _radarImages = newVal;
    }

    @Override
    protected AsyncTaskResult<BitmapsDTO> doInBackground(Void... params) {
        _logger.info("Retrieving radar bitmaps...");
        try {
            List<Bitmap> radarBitmaps = new ArrayList<Bitmap>();
            if ((_radarImages != null) && (_radarImages.size() > 0)) {
                for (int ii = 0; ii < _radarImages.size(); ii++) {
                    Bitmap radarBitmap = _dao.load(_radarImages.get(ii));
                    if (radarBitmap != null) {
                        radarBitmaps.add(radarBitmap);
                    }
                }
            }
            return new AsyncTaskResult<BitmapsDTO>(new BitmapsDTO(radarBitmaps));
        } catch (Throwable t) {
            return new AsyncTaskResult<BitmapsDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<BitmapsDTO> result) {
        _logger.info("...radar bitmaps retrieved.");
        fireCompleted(result);
    }
}
