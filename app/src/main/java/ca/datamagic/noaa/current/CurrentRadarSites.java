package ca.datamagic.noaa.current;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.RadarSiteDAO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.IOUtils;
import ca.datamagic.noaa.widget.R;

public class CurrentRadarSites {
    private static Logger logger = LogFactory.getLogger(CurrentRadarSites.class);
    private static RadarSiteDAO radarSiteDAO = null;

    static {
        InputStream inputStream = null;
        try {
            Context context = CurrentContext.getContext();
            if (context != null) {
                Resources resources = context.getResources();
                if (resources != null) {
                    inputStream = resources.openRawResource(R.raw.radar);
                    radarSiteDAO = new RadarSiteDAO(inputStream);
                }
            }

        } catch (Throwable t) {
            logger.warning("Exception: " + t.getMessage());
        }
        IOUtils.closeQuietly(inputStream);
    }

    public static RadarSiteDAO getRadarSiteDAO() {
        return radarSiteDAO;
    }
}
