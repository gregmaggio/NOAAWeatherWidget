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
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Implementation of App Widget functionality.
 */
public class CurrentTemperatureWidget extends AppWidgetProvider {
    private static Logger _logger = LogFactory.getLogger(CurrentTemperatureWidget.class);
    public static final String WIDGET_IDS_KEY = "CurrentTemperatureWidget";
    public static final String PACKAGE_NAME = "ca.datamagic.noaaweatherwidget";
    public static final String CLASS_NAME = "ca.datamagic.noaa.widget.CurrentTemperatureWidget";
    private static char _degrees = (char)0x00B0;
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");

    private static String getFormattedTemperature(Context context, Double temperature, String temperatureUnits, PreferencesDTO preferences) {
        StringBuffer buffer = new StringBuffer();
        if ((temperature != null) && (temperatureUnits != null) && (preferences != null)) {
            if (preferences.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0) {
                Double fahrenheit = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Fahrenheit);
                Double celsius = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Celsius);
                if ((fahrenheit != null) && (celsius != null)) {
                    buffer.append(_temperatureFormat.format(fahrenheit));
                    buffer.append("/");
                    buffer.append(_temperatureFormat.format(celsius));
                    buffer.append(_degrees);
                }
            } else if (preferences.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0) {
                Double fahrenheit = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Fahrenheit);
                Double celsius = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Celsius);
                if ((fahrenheit != null) && (celsius != null)) {
                    buffer.append(_temperatureFormat.format(celsius));
                    buffer.append("/");
                    buffer.append(_temperatureFormat.format(fahrenheit));
                    buffer.append(_degrees);
                }
            } else {
                temperature = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, preferences.getTemperatureUnits());
                if (temperature != null) {
                    buffer.append(_temperatureFormat.format(temperature));
                    buffer.append(_degrees);
                    if (preferences.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0) {
                        buffer.append(context.getResources().getString(R.string.temperatureFahrenheit));
                    } else if (preferences.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0) {
                        buffer.append(context.getResources().getString(R.string.temperatureCelsius));
                    }
                }
            }
        }
        return buffer.toString();
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            _logger.info("updateAppWidget: " + appWidgetId);
            String formattedTemperature = "";
            int color = Color.WHITE;
            MainActivity mainActivity = MainActivity.getThisInstance();
            if (mainActivity != null) {
                ObservationDTO observation = mainActivity.getObervation();
                if (observation != null) {
                    Double temperature = observation.getTemperature();
                    String temperatureUnits = observation.getTemperatureUnits();
                    PreferencesDAO preferencesDAO = new PreferencesDAO(context);
                    PreferencesDTO preferences = preferencesDAO.read();
                    color = preferences.getWidgetFontColor();
                    formattedTemperature = getFormattedTemperature(context, temperature, temperatureUnits, preferences);
                }
            }
            if ((formattedTemperature == null) || (formattedTemperature.length() < 1)) {
                formattedTemperature = "N/A";
            }
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_temperature_widget);
            views.setTextViewText(R.id.appwidget_text, formattedTemperature);
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

