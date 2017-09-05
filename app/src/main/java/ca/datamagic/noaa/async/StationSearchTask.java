package ca.datamagic.noaa.async;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/28/2017.
 */

public class StationSearchTask extends AsyncTaskBase<Void, Void, List<StationDTO>> {
    private Logger _logger = LogFactory.getLogger(StationSearchTask.class);
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
        _logger.info("Performing station search...");
        try {
            return new AsyncTaskResult<List<StationDTO>>(_dao.list(_city, _zip));
        } catch (Throwable t) {
            return new AsyncTaskResult<List<StationDTO>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<StationDTO>> result) {
        _logger.info("...station search completed.");
        fireCompleted(result);
    }
}
