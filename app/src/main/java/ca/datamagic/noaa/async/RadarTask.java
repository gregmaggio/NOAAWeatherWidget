package ca.datamagic.noaa.async;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarImagesDAO;
import ca.datamagic.noaa.dto.RadarDTO;
import ca.datamagic.noaa.dto.StringListDTO;
import ca.datamagic.noaa.dto.WFODTO;
import ca.datamagic.noaa.logging.LogFactory;

public class RadarTask extends AsyncTaskBase<Void, Void, RadarDTO> {
    private static Logger _logger = LogFactory.getLogger(RadarTask.class);
    private static int _maxImages = 15;
    private static RadarImagesDAO _radarImagesDAO = new RadarImagesDAO();
    private WFODTO _wfo = null;

    public RadarTask() {

    }

    public void setWFO(WFODTO newVal) {
        _wfo = newVal;
    }

    @Override
    protected AsyncTaskResult<RadarDTO> doInBackground(Void... params) {
        _logger.info("Retrieving radar...");
        try {
            RadarDTO dto = new RadarDTO();
            if (_wfo != null) {
                List<String> backgroundImages = new ArrayList<String>();
                backgroundImages.add(MessageFormat.format("https://radar.weather.gov/ridge/Overlays/Topo/Short/{0}_Topo_Short.jpg", _wfo.getRadar()));
                backgroundImages.add(MessageFormat.format("https://radar.weather.gov/ridge/Overlays/County/Short/{0}_County_Short.gif", _wfo.getRadar()));
                backgroundImages.add(MessageFormat.format("https://radar.weather.gov/ridge/Overlays/Rivers/Short/{0}_Rivers_Short.gif", _wfo.getRadar()));
                backgroundImages.add(MessageFormat.format("https://radar.weather.gov/ridge/Overlays/Highways/Short/{0}_Highways_Short.gif", _wfo.getRadar()));
                backgroundImages.add(MessageFormat.format("https://radar.weather.gov/ridge/Overlays/Cities/Short/{0}_Highways_City.gif", _wfo.getRadar()));

                List<String> radarImages = _radarImagesDAO.loadRadarImages(_wfo.getRadar());
                List<String> recentImages = new ArrayList<String>();
                if ((radarImages != null) && (radarImages.size() > 0)) {
                    _logger.info("radarImages: " + Integer.toString(radarImages.size()));
                    for (int ii = 0; ii < radarImages.size(); ii++) {
                        _logger.info("radarImage: " + radarImages.get(ii));
                    }
                    for (int ii = radarImages.size() - 1, jj = 0; (ii > -1) && (jj < _maxImages); ii--, jj++) {
                        recentImages.add(radarImages.get(ii));
                    }
                }

                dto.setBackgroundImages(new StringListDTO(backgroundImages));
                dto.setRadarImages(new StringListDTO(recentImages));
            }
            return new AsyncTaskResult<RadarDTO>(dto);
        } catch (Throwable t) {
            return new AsyncTaskResult<RadarDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<RadarDTO> result) {
        _logger.info("...radar retrieved.");
        fireCompleted(result);
    }
}
