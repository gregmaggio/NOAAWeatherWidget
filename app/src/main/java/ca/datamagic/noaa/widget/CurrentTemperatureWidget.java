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
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Implementation of App Widget functionality.
 */
public class CurrentTemperatureWidget extends BaseWidget {
    private static Logger _logger = LogFactory.getLogger(CurrentTemperatureWidget.class);
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

    @Override
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        _logger.info("updateAppWidget: " + appWidgetId);
        addWidget(appWidgetId, this);
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
        // Construct the RemoteViews object
        _views = new RemoteViews(context.getPackageName(), R.layout.current_temperature_widget);
        _views.setTextViewText(R.id.appwidget_text, formattedTemperature);
        _views.setTextColor(R.id.appwidget_text, color);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, _views);
    }
}

