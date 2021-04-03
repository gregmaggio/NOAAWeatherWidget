package ca.datamagic.noaa.async;

import android.graphics.Bitmap;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.callable.ImageDownloader;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarImageTask extends AsyncTaskBase<Void, Void, Bitmap> {
    private static Logger _logger = LogFactory.getLogger(RadarImageTask.class);
    private int _tile = -1;
    private String _timeStamp = null;

    public RadarImageTask(String timeStamp, int tile) {
        _timeStamp = timeStamp;
        _tile = tile;
    }

    @Override
    protected AsyncTaskResult<Bitmap> doInBackground(Void... params) {
        _logger.info("Retrieving radar bitmap...");
        try {
            String urlTimeStamp = URLEncoder.encode(_timeStamp, "UTF-8");
            String uri = null;
            switch (_tile) {
                case 2: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-15028131.257091932%2C5009377.085697312%2C-12523442.714243276%2C7514065.628545968", urlTimeStamp); break;
                case 3: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-12523442.714243278%2C5009377.085697312%2C-10018754.171394622%2C7514065.628545968", urlTimeStamp); break;
                case 4: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-10018754.171394622%2C5009377.085697312%2C-7514065.628545966%2C7514065.628545968", urlTimeStamp); break;
                case 5: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-7514065.628545966%2C5009377.085697312%2C-5009377.08569731%2C7514065.628545968", urlTimeStamp); break;
                case 8: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-15028131.257091932%2C2504688.542848654%2C-12523442.714243276%2C5009377.08569731", urlTimeStamp); break;
                case 9: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-12523442.714243278%2C2504688.542848654%2C-10018754.171394622%2C5009377.08569731", urlTimeStamp); break;
                case 10: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-10018754.171394622%2C2504688.542848654%2C-7514065.628545966%2C5009377.08569731", urlTimeStamp); break;
                case 11: uri = MessageFormat.format("https://opengeo.ncep.noaa.gov/geoserver/conus/conus_bref_qcd/ows?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&TILED=true&LAYERS=conus_bref_qcd&TIME={0}&WIDTH=256&HEIGHT=256&SRS=EPSG%3A3857&BBOX=-7514065.628545966%2C2504688.542848654%2C-5009377.08569731%2C5009377.08569731", urlTimeStamp); break;
            }
            if (uri == null) {
                return new AsyncTaskResult<Bitmap>();
            }
            _logger.info("uri: " + uri);
            ImageDownloader downloader = new ImageDownloader(uri);
            downloader.call();
            return new AsyncTaskResult<Bitmap>(downloader.getImage());
        } catch (Throwable t) {
            return new AsyncTaskResult<Bitmap>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Bitmap> result) {
        _logger.info("...radar bitmap retrieved.");
        fireCompleted(result);
    }
}
