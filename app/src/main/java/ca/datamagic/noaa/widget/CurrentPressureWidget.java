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

import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.PressureCalculatorDTO;
import ca.datamagic.noaa.dto.PressureUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class CurrentPressureWidget extends AppWidgetProvider {
    private static Logger _logger = LogFactory.getLogger(CurrentPressureWidget.class);
    public static final String WIDGET_IDS_KEY = "CurrentPressureWidget";
    public static final String PACKAGE_NAME = "ca.datamagic.noaaweatherwidget";
    public static final String CLASS_NAME = "ca.datamagic.noaa.widget.CurrentPressureWidget";
    private static DecimalFormat _pressureFormat = new DecimalFormat("0.00");

    private static String getFormattedPressure(Context context, Double pressure, String pressureUnits, Double elevation, String elevationUnits, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((pressure != null) && (pressureUnits != null) && (elevation != null) && (elevationUnits != null)) {
            pressure = PressureCalculatorDTO.compute(pressure, pressureUnits, preferencesDTO.getPressureUnits(), elevation, elevationUnits);
            if (pressure != null) {
                buffer.append(_pressureFormat.format(pressure.doubleValue()));
                if (preferencesDTO.getPressureUnits().compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                    buffer.append(" ");
                    buffer.append(context.getResources().getString(R.string.inchesOfMercury));
                } else if (preferencesDTO.getPressureUnits().compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                    buffer.append(" ");
                    buffer.append(context.getResources().getString(R.string.kiloPascals));
                } else if (preferencesDTO.getPressureUnits().compareToIgnoreCase(PressureUnitsDTO.StationPressure) == 0) {
                    buffer.append(" ");
                    buffer.append(context.getResources().getString(R.string.inchesOfMercury));
                }
            }
        }
        return buffer.toString();
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            _logger.info("updateAppWidget:" + appWidgetId);
            String formattedPressure = "";
            int color = Color.WHITE;
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                ObservationDTO observation = mainActivity.getObervation();
                if (observation != null) {
                    Double pressure = observation.getPressure();
                    String pressureUnits = observation.getPressureUnits();
                    Double elevation = observation.getElevation();
                    String elevationUnits = observation.getElevationUnits();
                    PreferencesDAO preferencesDAO = new PreferencesDAO(context);
                    PreferencesDTO preferences = preferencesDAO.read();
                    color = preferences.getWidgetFontColor();
                    formattedPressure = getFormattedPressure(context, pressure, pressureUnits, elevation, elevationUnits, preferences);
                }
            }
            if ((formattedPressure == null) || (formattedPressure.length() < 1)) {
                formattedPressure = "N/A";
            }
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_pressure_widget);
            views.setTextViewText(R.id.appwidget_text, formattedPressure);
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
    }

    @Override
    public void onDisabled(Context context) {
        _logger.info("onDisabled");
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        _logger.info("onRestored");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
