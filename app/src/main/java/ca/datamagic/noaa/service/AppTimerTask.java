package ca.datamagic.noaa.service;

import android.content.Context;

import java.util.TimerTask;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.DWMLTask;
import ca.datamagic.noaa.dao.ObservationDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.current.CurrentLocation;
import ca.datamagic.noaa.current.CurrentObservation;
import ca.datamagic.noaa.widget.CurrentWidgets;

public class AppTimerTask extends TimerTask {
    public static final long HALF_HOUR_MILLIS = 1800000L;
    private static final Logger _logger = LogFactory.getLogger(AppTimerTask.class);
    private Context _context = null;

    public AppTimerTask(Context context) {
        _context = context;
    }

    @Override
    public void run() {
        try {
            _logger.info("run");
            _logger.info("We should get a new observation...");
            // We should get a new observation
            DWMLTask task = new DWMLTask(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
            task.addListener(new AsyncTaskListener<DWMLDTO>() {
                @Override
                public void completed(AsyncTaskResult<DWMLDTO> result) {
                    if (result.getResult() != null) {
                        ObservationDTO observation = ObservationDAO.getObservation(result.getResult());
                        if (observation != null) {
                            _logger.info("...got a new observation");
                            CurrentObservation.setObervation(observation);
                            _logger.info("Refreshing widgets...");
                            CurrentWidgets.refreshWidgets(_context);
                            _logger.info("...refreshed widgets.");
                        } else {
                            _logger.info("...couldn't process DWML.");
                        }
                    } else {
                        _logger.info("...observation result was null");
                    }
                }
            });
            task.execute((Void) null);
        } catch (Throwable t) {
            _logger.warning("Error in AppTimerTask. Exception: " + t.getMessage());
        }
    }
}
