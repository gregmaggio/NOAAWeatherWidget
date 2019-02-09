package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.GooglePlacesDAO;
import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/24/2018.
 */

public class GooglePlaceTask extends AsyncTaskBase<Void, Void, PlaceDTO> {
    private Logger _logger = LogFactory.getLogger(GooglePlaceTask.class);
    private GooglePlacesDAO _googlePlacesDAO = null;
    private String _placeId = null;

    public GooglePlaceTask(String placeId) {
        _googlePlacesDAO = new GooglePlacesDAO();
        _placeId = placeId;
    }

    @Override
    protected AsyncTaskResult<PlaceDTO> doInBackground(Void... voids) {
        try {
            _logger.info("Loading place...");
            PlaceDTO place = _googlePlacesDAO.loadPlace(_placeId);
            return new AsyncTaskResult<PlaceDTO>(place);
        } catch (Throwable t) {
            return new AsyncTaskResult<PlaceDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<PlaceDTO> result) {
        _logger.info("...Place loaded.");
        fireCompleted(result);
    }
}
