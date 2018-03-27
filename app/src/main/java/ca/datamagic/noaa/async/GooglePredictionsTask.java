package ca.datamagic.noaa.async;

import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.GooglePlacesDAO;
import ca.datamagic.noaa.dto.PredictionDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/24/2018.
 */

public class GooglePredictionsTask extends AsyncTaskBase<Void, Void, List<PredictionDTO>> {
    private Logger _logger = LogFactory.getLogger(GooglePredictionsTask.class);
    private GooglePlacesDAO _dao = null;
    private String _searchText = null;

    public GooglePredictionsTask(String searchText) {
        _dao = new GooglePlacesDAO();
        _searchText = searchText;
    }

    @Override
    protected AsyncTaskResult<List<PredictionDTO>> doInBackground(Void... voids) {
        try {
            _logger.info("Loading predictions...");
            List<PredictionDTO> predictions = _dao.loadAutoCompletePredictions(_searchText);
            return new AsyncTaskResult<List<PredictionDTO>>(predictions);
        } catch (Throwable t) {
            return new AsyncTaskResult<List<PredictionDTO>>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<PredictionDTO>> result) {
        _logger.info("...Predictions loaded.");
        fireCompleted(result);
    }
}
