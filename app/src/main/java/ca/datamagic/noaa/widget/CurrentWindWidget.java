package ca.datamagic.noaa.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RemoteViews;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentObservation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.WindSpeedCalculatorDTO;
import ca.datamagic.noaa.dto.WindSpeedUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.WindDirectionConverter;

public class CurrentWindWidget extends AppWidgetProvider {
    private static Logger _logger = LogFactory.getLogger(CurrentWindWidget.class);
    public static final String WIDGET_IDS_KEY = "CurrentWindWidget";
    public static final String PACKAGE_NAME = "ca.datamagic.noaaweatherwidget";
    public static final String CLASS_NAME = "ca.datamagic.noaa.widget.CurrentWindWidget";
    private static DecimalFormat _windFormat = new DecimalFormat("0");

    private static String getFormattedWind(Context context, Double windSpeed, String windSpeedUnits, Double windDirection, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((windSpeed != null) && (windSpeedUnits != null)) {
            windSpeed = WindSpeedCalculatorDTO.compute(windSpeed, windSpeedUnits, preferencesDTO.getWindSpeedUnits());
            if (windSpeed != null) {
                buffer.append(_windFormat.format(windSpeed.doubleValue()));
                buffer.append(" ");
                if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    buffer.append(context.getResources().getString(R.string.knots));
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    buffer.append(context.getResources().getString(R.string.milesPerHour));
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    buffer.append(context.getResources().getString(R.string.kilometersPerHour));
                }
                if (windDirection != null) {
                    String compass = WindDirectionConverter.degreesToCompass(windDirection);
                    if (compass != null) {
                        buffer.append(" ");
                        buffer.append(compass);
                    }
                }
            }
        }
        return buffer.toString();
    }

    private static String getFormattedWindGusts(Context context, Double windGust, String windGustUnits, PreferencesDTO preferencesDTO) {
        if ((windGust != null) && (windGustUnits != null)) {
            windGust = WindSpeedCalculatorDTO.compute(windGust, windGustUnits, preferencesDTO.getWindSpeedUnits());
            if (windGust != null) {
                String units = "";
                if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    units = context.getString(R.string.knots);
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    units = context.getString(R.string.milesPerHour);
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    units = context.getString(R.string.kilometersPerHour);
                }
                String windGustFormat = context.getString(R.string.wind_gust);
                return MessageFormat.format(windGustFormat, _windFormat.format(windGust.doubleValue()), units);
            }
        }
        return "";
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            _logger.info("updateAppWidget: " + appWidgetId);
            String formattedWind = "";
            String formattedWindGusts = "";
            int color = Color.WHITE;
            ObservationDTO observation = CurrentObservation.getObervation();
            if (observation != null) {
                Double windDirection = observation.getWindDirection();
                Double windSpeed = observation.getWindSpeed();
                String windSpeedUnits = observation.getWindSpeedUnits();
                Double windGust = observation.getWindGust();
                String windGustUnits = observation.getWindGustUnits();
                PreferencesDAO preferencesDAO = new PreferencesDAO(context);
                PreferencesDTO preferences = preferencesDAO.read();
                color = preferences.getWidgetFontColor();
                formattedWind = getFormattedWind(context, windSpeed, windSpeedUnits, windDirection, preferences);
                formattedWindGusts = getFormattedWindGusts(context, windGust, windGustUnits, preferences);
            }
            if ((formattedWind == null) || (formattedWind.length() < 1)) {
                formattedWind = "N/A";
            }
            _logger.info("formattedWind: " + formattedWind);
            _logger.info("formattedWindGusts: " + formattedWindGusts);

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_wind_widget);
            views.setTextViewText(R.id.appwidget_text, formattedWind);
            if ((formattedWindGusts != null) && (formattedWindGusts.length() > 0)) {
                views.setTextViewText(R.id.windgusts_text, formattedWindGusts);
                views.setTextColor(R.id.windgusts_text, color);
            } else {
                views.setViewVisibility(R.id.windgusts_text, View.GONE);
            }
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
            CurrentWidgets.enableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME, context);
        } else {
            CurrentWidgets.disableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME, context);
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
        CurrentWidgets.enableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME, context);
    }

    @Override
    public void onDisabled(Context context) {
        _logger.info("onDisabled");
        MainActivity mainActivity = MainActivity.getThisInstance();
        CurrentWidgets.disableWidget(WIDGET_IDS_KEY, PACKAGE_NAME, CLASS_NAME, context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        _logger.info("onRestored");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
