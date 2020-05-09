package ca.datamagic.noaa.service;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AsyncTaskListener;
import ca.datamagic.noaa.async.AsyncTaskResult;
import ca.datamagic.noaa.async.DWMLTask;
import ca.datamagic.noaa.dao.ObservationDAO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;

public class AppTimerTask extends TimerTask {
    public static final long ONE_HOUR_MILLIS = 3600000l;
    private static final Logger _logger = LogFactory.getLogger(AppTimerTask.class);

    @Override
    public void run() {
        try {
            _logger.info("run");
            final MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                ObservationDTO observation = mainActivity.getObervation();
                if (observation != null) {
                    Date observationTimeUTC = observation.getObservationTimeUTC();
                    Double latitude = observation.getLatitude();
                    Double longitude = observation.getLongitude();
                    if ((observationTimeUTC != null) && (latitude != null) && (longitude != null)) {
                        Calendar now = Calendar.getInstance();
                        now.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date nowUTC = now.getTime();
                        long differenceMillis = nowUTC.getTime() - observationTimeUTC.getTime();
                        if (differenceMillis > ONE_HOUR_MILLIS) {
                            _logger.info("We should get a new observation...");
                            // We should get a new observation
                            DWMLTask task = new DWMLTask(latitude, longitude);
                            task.addListener(new AsyncTaskListener<DWMLDTO>() {
                                @Override
                                public void completed(AsyncTaskResult<DWMLDTO> result) {
                                    if (result.getResult() != null) {
                                        ObservationDTO observation = ObservationDAO.getObservation(result.getResult());
                                        if (observation != null) {
                                            _logger.info("...got a new observation");
                                            mainActivity.setObervation(observation);
                                            _logger.info("Refreshing widgets...");
                                            mainActivity.refreshWidgets();
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
                        }
                    }
                }
            }
        } catch (Throwable t) {
            _logger.warning("Error in AppTimerTask. Exception: " + t.getMessage());
        }
    }
}
