package ca.datamagic.noaa.async;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 1/28/2017.
 */

public class StationSearchTask extends AsyncTaskBase<Void, Void, List<StationDTO>> {
    private Logger _logger = LogManager.getLogger(StationSearchTask.class);
    private StationDAO _dao = null;
    private String _city = null;
    private String _zip = null;

    public StationSearchTask(String city, String zip) {
        _dao = new StationDAO();
        _city = city;
        _zip = zip;
    }

    @Override
    protected AsyncTaskResult<List<StationDTO>> doInBackground(Void... params) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Running Task");
        }
        try {
            return new AsyncTaskResult<List<StationDTO>>(_dao.list(_city, _zip));
        } catch (Throwable t) {
            return new AsyncTaskResult<List<StationDTO>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<StationDTO>> result) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Completed Task");
        }
        fireCompleted(result);
    }
}
