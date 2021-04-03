package ca.datamagic.noaa.async;

import android.graphics.Bitmap;

import java.util.logging.Logger;

import ca.datamagic.noaa.callable.ImageDownloader;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarBackgroundTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static Logger _logger = LogFactory.getLogger(RadarBackgroundTask.class);
    private int _tile = -1;

    public RadarBackgroundTask(int tile) {
        _tile = tile;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        _logger.info("Retrieving radar background bitmap...");
        try {
            String uri = null;
            switch (_tile) {
                case 1: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/5/1"; break;
                case 2: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/5/2"; break;
                case 3: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/5/3"; break;
                case 4: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/5/4"; break;
                case 5: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/5/5"; break;
                case 6: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/5/6"; break;
                case 7: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/6/1"; break;
                case 8: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/6/2"; break;
                case 9: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/6/3"; break;
                case 10: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/6/4"; break;
                case 11: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/6/5"; break;
                case 12: uri = "https://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/4/6/6"; break;
            }
            if (uri == null) {
                return new AsyncTaskResult<Bitmap>();
            }
            ImageDownloader downloader = new ImageDownloader(uri);
            downloader.call();
            return new AsyncTaskResult<Bitmap>(downloader.getImage());
        } catch (Throwable t) {
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        _logger.info("...radar background bitmap retrieved.");
        fireCompleted(result);
    }
}
