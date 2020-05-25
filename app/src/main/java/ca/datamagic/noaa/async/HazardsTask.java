package ca.datamagic.noaa.async;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentDWML;
import ca.datamagic.noaa.dao.HazardsDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HazardsTask extends AsyncTaskBase<Void, Void, List<String>> {
    private static Logger _logger = LogFactory.getLogger(HazardsTask.class);
    private HazardsDAO _dao = new HazardsDAO();

    public HazardsTask() {
    }

    @Override
    protected AsyncTaskResult<List<String>> doInBackground(Void... params) {
        try {
            _logger.info("Downloading hazards...");
            DWMLDTO dwml = CurrentDWML.getDWML();
            List<String> hazardsList = new ArrayList<String>();
            if (dwml != null) {
                hazardsList = _dao.getHazards(dwml);
            }
            return new AsyncTaskResult<List<String>>(hazardsList);
        } catch (Throwable t) {
            return new AsyncTaskResult<List<String>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<String>> result) {
        _logger.info("...hazards downloaded.");
        fireCompleted(result);
    }
}
