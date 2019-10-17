package ca.datamagic.noaa.async;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.HazardsDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HazardsTask extends AsyncTaskBase<Void, Void, List<String>> {
    private static Logger _logger = LogFactory.getLogger(HazardsTask.class);
    private HazardsDAO _dao = new HazardsDAO();
    private DWMLDTO _dwml = null;

    public HazardsTask() {
    }

    public void setDWML(DWMLDTO newVal) {
        _dwml = newVal;
    }

    @Override
    protected AsyncTaskResult<List<String>> doInBackground(Void... params) {
        try {
            _logger.info("Downloading hazards...");
            List<String> hazardsList = _dao.getHazards(_dwml);
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
