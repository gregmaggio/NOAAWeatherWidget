package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentStation;
import ca.datamagic.noaa.dao.DiscussionDAO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/3/2016.
 */
public class DiscussionTask extends AsyncTaskBase<String> {
    private static Logger _logger = LogFactory.getLogger(DiscussionTask.class);
    private static DiscussionDAO _dao = new DiscussionDAO();

    public DiscussionTask() {

    }

    @Override
    protected AsyncTaskResult<String> doInBackground() {
        try {
            StationDTO station = CurrentStation.getStation();
            String wfo = null;
            if (station != null) {
                wfo = station.getWFO();
            }
            if ((wfo == null) || (wfo.length() < 1)) {
                return new AsyncTaskResult<String>("");
            }
            _logger.info("Loading discussion...");
            return new AsyncTaskResult<String>(_dao.load(wfo));
        } catch (Throwable t) {
            return new AsyncTaskResult<String>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<String> result) {
        _logger.info("...Discussion loaded.");
        fireCompleted(result);
    }
}
