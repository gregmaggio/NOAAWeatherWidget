package ca.datamagic.noaa.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ca.datamagic.noaa.current.CurrentContext;
import ca.datamagic.noaa.current.CurrentLocation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.PreferencesDTO;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferencesDAO dao = new PreferencesDAO(context);
        PreferencesDTO dto = dao.read();

        // Get last location
        CurrentLocation.setLatitude(dto.getLatitude());
        CurrentLocation.setLongitude(dto.getLongitude());

        // Refresh the widgets
        CurrentWidgets.refreshWidgets(context);
    }
}
