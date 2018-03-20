package ca.datamagic.noaa.async;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.DiscussionDAO;
import ca.datamagic.noaa.dao.WFODAO;
import ca.datamagic.noaa.dto.WFODTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/3/2016.
 */
public class DiscussionTask extends AsyncTaskBase<Void, Void, String> {
    private Logger _logger = LogFactory.getLogger(DiscussionTask.class);
    private WFODAO _wfoDAO = null;
    private DiscussionDAO _discussionDAO = null;
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public DiscussionTask(double latitude, double longitude) {
        _wfoDAO = new WFODAO();
        _discussionDAO = new DiscussionDAO();
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(Void... params) {
        try {
            _logger.info("Loading WFO...");
            List<WFODTO> wfo = _wfoDAO.read(_latitude, _longitude);
            if ((wfo == null) || (wfo.size() < 1)) {
                return new AsyncTaskResult<String>("");
            }
            _logger.info("Loading discussion...");
            return new AsyncTaskResult<String>(_discussionDAO.load(wfo.get(0).getWFO()));
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
