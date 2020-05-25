package ca.datamagic.noaa.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentObservation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class CurrentHumidityWidget extends AppWidgetProvider {
    private static Logger _logger = LogFactory.getLogger(CurrentHumidityWidget.class);
    public static final String WIDGET_IDS_KEY = "CurrentHumidityWidget";
    public static final String PACKAGE_NAME = "ca.datamagic.noaaweatherwidget";
    public static final String CLASS_NAME = "ca.datamagic.noaa.widget.CurrentHumidityWidget";
    private static DecimalFormat _humidityFormat = new DecimalFormat("0%");

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            _logger.info("updateAppWidget: " + appWidgetId);
            String formattedHumidity = "";
            int color = Color.WHITE;
            ObservationDTO observation = CurrentObservation.getObervation();
            if (observation != null) {
                Double humidity = observation.getRelativeHumidity();
                PreferencesDAO preferencesDAO = new PreferencesDAO(context);
                PreferencesDTO preferences = preferencesDAO.read();
                color = preferences.getWidgetFontColor();
                if (humidity != null) {
                    formattedHumidity = _humidityFormat.format(humidity.doubleValue() / 100.0);
                }
            }
            if ((formattedHumidity == null) || (formattedHumidity.length() < 1)) {
                formattedHumidity = "N/A";
            }
            _logger.info("formattedHumidity: " + formattedHumidity);

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_humidity_widget);
            views.setTextViewText(R.id.appwidget_text, formattedHumidity);
            views.setTextColor(R.id.appwidget_text_label, color);
            views.setTextColor(R.id.appwidget_text, color);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (Throwable t) {
            _logger.log(Level.WARNING, "Unknown Exception in updateAppWidget.", t);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        _logger.info("onReceive");
        _logger.info("intent: " + intent);
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        _logger.info("onUpdate");
        for (int appWidgetId : appWidgetIds) {
            _logger.info("appWidgetId: " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        if (appWidgetIds.length > 0) {
            CurrentWidgets.enableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME);
        } else {
            CurrentWidgets.disableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        _logger.info("onDeleted");
        for (int ii = 0; ii < appWidgetIds.length; ii++) {
            _logger.info("removeWidget: " + appWidgetIds[ii]);
        }
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        _logger.info("onEnabled");
        CurrentWidgets.enableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME);
    }

    @Override
    public void onDisabled(Context context) {
        _logger.info("onDisabled");
        CurrentWidgets.disableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        _logger.info("onRestored");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
