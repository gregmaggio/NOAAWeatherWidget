package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarUrlsTask extends AsyncTaskBase<Void, Void, String[]> {
    private static final Logger _logger = LogFactory.getLogger(RadarUrlsTask.class);
    private String _icao = null;

    public RadarUrlsTask(String icao) {
        _icao = icao;
    }

    @Override
    protected AsyncTaskResult<String[]> doInBackground(Void... params) {
        try {
            _logger.info("Loading radar urls...");
            RadarDAO dao = new RadarDAO();
            String[] urls = dao.loadUrls(_icao);
            return new AsyncTaskResult<String[]>(urls);
        } catch (Throwable t) {
            return new AsyncTaskResult<String[]>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String[]> result) {
        _logger.info("...radar urls loaded.");
        fireCompleted(result);
    }
}
