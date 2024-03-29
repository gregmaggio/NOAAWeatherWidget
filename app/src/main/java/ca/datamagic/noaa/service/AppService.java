package ca.datamagic.noaa.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.Timer;
import java.util.logging.Logger;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import ca.datamagic.noaa.current.CurrentLocation;
import ca.datamagic.noaa.current.CurrentObservation;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.widget.MainActivity;
import ca.datamagic.noaa.widget.R;

public class AppService extends Service {
    private static final Logger _logger = LogFactory.getLogger(AppService.class);
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private Timer _timer = null;
    private AppTimerTask _appTimerTask = null;
    private static boolean _running = false;

    public static boolean isRunning() {
        return _running;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        _logger.info("onBind");
        return null;
    }

    @Override
    public void onCreate() {
        _logger.info("onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        _logger.info("onStartCommand");

        _appTimerTask = new AppTimerTask(getApplicationContext());
        _timer = new Timer();
        _timer.scheduleAtFixedRate(_appTimerTask, AppTimerTask.HALF_HOUR_MILLIS, AppTimerTask.HALF_HOUR_MILLIS);

        try {
            createNotificationChannel();
            Intent notificationIntent = new Intent(this, MainActivity.class);
            int pendingIntentFlags = (Build.VERSION.SDK_INT >= 23) ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, pendingIntentFlags);
            startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                    NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_drawer)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntent)
                    .build());
        } catch (Throwable t) {
            _logger.warning("Bullshit: " + t.getMessage());
        }

        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            mainActivity.serviceStartedStopped(true);
        }

        // Check if we have a current observation
        ObservationDTO observation = CurrentObservation.getObervation();
        if (observation == null) {
            // Make sure we have a location
            Double latitude = CurrentLocation.getLatitude();
            Double longitude = CurrentLocation.getLongitude();
            if ((latitude != null) && (longitude != null)) {
                // Run the timer task to get the current observation
                _appTimerTask.run();
            }
        }

        _running = true;
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        _logger.info("onDestroy");
        if (_timer != null) {
            _timer.cancel();
            _timer.purge();
        }
        _appTimerTask = null;
        _timer = null;

        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            mainActivity.serviceStartedStopped(false);
        }

        _running = false;
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        _logger.info("onLowMemory");
        super.onLowMemory();
    }

    private boolean createNotificationChannel() {
        try {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                return true;
            }
        } catch (Throwable t) {
            _logger.warning("Error creating notification channel: " + t.getMessage());
        }
        return false;
    }
}
