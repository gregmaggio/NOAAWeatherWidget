package ca.datamagic.noaa.async;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.dto.RadarTimeDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarTimeStampsTask extends AsyncTaskBase<Void, Void, List<RadarTimeDTO>> {
    private static Logger _logger = LogFactory.getLogger(RadarTimeStampsTask.class);
    private RadarDAO _dao = new RadarDAO();

    @Override
    protected AsyncTaskResult<List<RadarTimeDTO>> doInBackground(Void... params) {
        _logger.info("Retrieving radar timestamps...");
        try {
            List<RadarTimeDTO> timeStamps = _dao.getTimeStamps();
            return new AsyncTaskResult<List<RadarTimeDTO>>(timeStamps);
        } catch (Throwable t) {
            return new AsyncTaskResult<List<RadarTimeDTO>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<RadarTimeDTO>> result) {
        _logger.info("...radar timestamps retrieved.");
        fireCompleted(result);
    }
}
