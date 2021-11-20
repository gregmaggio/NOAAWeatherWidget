package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarDAO;
import ca.datamagic.noaa.dto.RadarImageMetaDataDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarImageMetaDataTask extends AsyncTaskBase<Void, Void, RadarImageMetaDataDTO> {
    private static final Logger _logger = LogFactory.getLogger(RadarImageMetaDataTask.class);
    private String _imageUrl = null;

    public RadarImageMetaDataTask(String imageUrl) {
        _imageUrl = imageUrl;
    }

    @Override
    protected AsyncTaskResult<RadarImageMetaDataDTO> doInBackground(Void... params) {
        try {
            _logger.info("Loading radar image meta data...");
            RadarDAO dao = new RadarDAO();
            RadarImageMetaDataDTO metaData = dao.loadMetaData(_imageUrl);
            return new AsyncTaskResult<RadarImageMetaDataDTO>(metaData);
        } catch (Throwable t) {
            return new AsyncTaskResult<RadarImageMetaDataDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<RadarImageMetaDataDTO> result) {
        _logger.info("...radar image meta data loaded.");
        fireCompleted(result);
    }
}
