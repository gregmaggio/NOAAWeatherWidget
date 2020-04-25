package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;
import ca.datamagic.quadtree.Quad;
import ca.datamagic.quadtree.Station;

public class StationTask extends AsyncTaskBase<Void, Void, Station> {
    private static final Logger _logger = LogFactory.getLogger(StationTask.class);
    private static final double distance = 75;
    private static final String units = "statute miles";
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public StationTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<Station> doInBackground(Void... params) {
        try {
            _logger.info("Loading Station...");
            Station station = null;
            Quad tree = MainActivity.getThisInstance().getTree();
            if (tree != null) {
                station = tree.readNearest(_latitude, _longitude, distance, units);
            }
            return new AsyncTaskResult<Station>(station);
        } catch (Throwable t) {
            return new AsyncTaskResult<Station>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Station> result) {
        _logger.info("...station loaded.");
        fireCompleted(result);
    }
}
