package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.GooglePlacesDAO;
import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.dto.PlaceDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 3/24/2018.
 */

public class GooglePlaceTask extends AsyncTaskBase<Void, Void, StationDTO> {
    private Logger _logger = LogFactory.getLogger(GooglePlaceTask.class);
    private GooglePlacesDAO _googlePlacesDAO = null;
    private StationDAO _stationDAO = null;
    private String _placeId = null;

    public GooglePlaceTask(String placeId) {
        _googlePlacesDAO = new GooglePlacesDAO();
        _stationDAO = new StationDAO();
        _placeId = placeId;
    }

    @Override
    protected AsyncTaskResult<StationDTO> doInBackground(Void... voids) {
        try {
            _logger.info("Loading place...");
            PlaceDTO place = _googlePlacesDAO.loadPlace(_placeId);
            _logger.info("Loading station...");
            StationDTO station = _stationDAO.nearest(place.getLatitude(), place.getLongitude());
            return new AsyncTaskResult<StationDTO>(station);
        } catch (Throwable t) {
            return new AsyncTaskResult<StationDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<StationDTO> result) {
        _logger.info("...Station loaded.");
        fireCompleted(result);
    }
}
