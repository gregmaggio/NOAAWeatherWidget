package ca.datamagic.noaa.async;

import java.util.logging.Logger;

import ca.datamagic.noaa.dao.DWMLDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.HeadDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.SourceDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 2/18/2017.
 */

public class DWMLTask extends AsyncTaskBase<Void, Void, DWMLDTO> {
    private static Logger _logger = LogFactory.getLogger(DWMLTask.class);
    private static DWMLDAO _dao = new DWMLDAO();
    private double _latitude = 0.0;
    private double _longitude = 0.0;
    private static String _unit = "e";

    public DWMLTask(double latitude, double longitude) {
        _latitude = latitude;
        _longitude = longitude;
    }

    @Override
    protected AsyncTaskResult<DWMLDTO> doInBackground(Void... params) {
        _logger.info("Loading DWML...");
        try {
            DWMLDTO dwml = _dao.load(_latitude, _longitude, _unit);

            HeadDTO head = dwml.getHead();
            if (head != null) {
                SourceDTO source = head.getSource();
                if (source != null) {
                    dwml.setWFOURL(source.getCredit());
                }
            }

            LocationDTO location = new LocationDTO();

            DataDTO forecast = dwml.getForecast();
            if (forecast != null) {
                if (forecast.getLocation() != null) {
                    location.setDescription(forecast.getLocation().getDescription());
                    location.setState(forecast.getLocation().getState());
                    location.setCity(forecast.getLocation().getCity());
                }
            }

            DataDTO observation = dwml.getObservation();
            if (observation != null) {
                if (observation.getLocation() != null) {
                    if ((location.getDescription() == null) || (location.getDescription().length() < 1)) {
                        location.setDescription(observation.getLocation().getDescription());
                    }
                    if ((location.getState() == null) || (location.getState().length() < 1)) {
                        location.setState(observation.getLocation().getState());
                    }
                    if ((location.getCity() == null) || (location.getCity().length() < 1)) {
                        location.setCity(observation.getLocation().getCity());
                    }
                }
            }

            location.setPoint(new PointDTO(_latitude, _longitude));
            dwml.setLocation(location);

            return new AsyncTaskResult<DWMLDTO>(dwml);
        } catch (Throwable t) {
            return new AsyncTaskResult<DWMLDTO>(t);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<DWMLDTO> result) {
        _logger.info("...DWML loaded.");
        fireCompleted(result);
    }
}
