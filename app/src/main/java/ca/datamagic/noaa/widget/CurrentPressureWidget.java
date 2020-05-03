package ca.datamagic.noaa.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.graphics.Color;
import android.widget.RemoteViews;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.PressureCalculatorDTO;
import ca.datamagic.noaa.dto.PressureUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class CurrentPressureWidget extends BaseWidget {
    private static Logger _logger = LogFactory.getLogger(CurrentPressureWidget.class);
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

    @Override
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        _logger.info("updateAppWidget:" + appWidgetId);
        addWidget(appWidgetId, this);
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
        // Construct the RemoteViews object
        _views = new RemoteViews(context.getPackageName(), R.layout.current_pressure_widget);
        _views.setTextViewText(R.id.appwidget_text, formattedPressure);
        _views.setTextColor(R.id.appwidget_text, color);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, _views);
    }
}
