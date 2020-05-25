package ca.datamagic.noaa.current;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.StationDAO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.widget.R;

public class CurrentStations {
    private static Logger _logger = LogFactory.getLogger(CurrentStations.class);
    private static StationDAO _stationDAO = null;

    static {
        InputStream inputStream = null;
        try {
            Context context = CurrentContext.getContext();
            if (context != null) {
                Resources resources = context.getResources();
                if (resources != null) {
                    inputStream = resources.openRawResource(R.raw.stations);
                    _stationDAO = new StationDAO(inputStream);
                }
            }

        } catch (Throwable t) {
            _logger.warning("Exception: " + t.getMessage());
        }
        IOUtils.closeQuietly(inputStream);
    }

    public static StationDAO getStationDAO() {
        return _stationDAO;
    }
}
