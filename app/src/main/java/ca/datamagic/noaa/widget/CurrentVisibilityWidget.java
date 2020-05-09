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
import ca.datamagic.noaa.dto.VisibilityCalculatorDTO;
import ca.datamagic.noaa.dto.VisibilityUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class CurrentVisibilityWidget extends AppWidgetProvider {
    private static Logger _logger = LogFactory.getLogger(CurrentVisibilityWidget.class);
    public static final String WIDGET_IDS_KEY = "CurrentVisibilityWidget";
    public static final String PACKAGE_NAME = "ca.datamagic.noaaweatherwidget";
    public static final String CLASS_NAME = "ca.datamagic.noaa.widget.CurrentVisibilityWidget";
    private static char _degrees = (char)0x00B0;
    private static DecimalFormat _visibilityFormat = new DecimalFormat("0.00");

    private static String getFormattedVisibility(Context context, Double visibility, String visibilityUnits, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((visibility != null) && (visibilityUnits != null)) {
            visibility = VisibilityCalculatorDTO.compute(visibility, visibilityUnits, preferencesDTO.getVisibilityUnits());
            if (visibility != null) {
                buffer.append(_visibilityFormat.format(visibility.doubleValue()));
                if (preferencesDTO.getVisibilityUnits().compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    buffer.append(" ");
                    buffer.append(context.getResources().getString(R.string.statuteMiles));
                } else if (preferencesDTO.getVisibilityUnits().compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    buffer.append(" ");
                    buffer.append(context.getResources().getString(R.string.miles));
                } else if (preferencesDTO.getVisibilityUnits().compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0) {
                    buffer.append(" ");
                    buffer.append(context.getResources().getString(R.string.kilometers));
                }
            }
        }
        return buffer.toString();
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            _logger.info("updateAppWidget: " + appWidgetId);
            String formattedVisibility = "";
            int color = Color.WHITE;
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                ObservationDTO observation = mainActivity.getObervation();
                if (observation != null) {
                    Double visibility = observation.getVisibility();
                    String visibilityUnits = observation.getVisibilityUnits();
                    PreferencesDAO preferencesDAO = new PreferencesDAO(context);
                    PreferencesDTO preferences = preferencesDAO.read();
                    color = preferences.getWidgetFontColor();
                    formattedVisibility = getFormattedVisibility(context, visibility, visibilityUnits, preferences);
                }
            }
            if ((formattedVisibility == null) || (formattedVisibility.length() < 1)) {
                formattedVisibility = "N/A";
            }
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_visibility_widget);
            views.setTextViewText(R.id.appwidget_text, formattedVisibility);
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
