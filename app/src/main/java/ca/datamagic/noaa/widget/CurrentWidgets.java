package ca.datamagic.noaa.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.datamagic.noaa.current.CurrentContext;
import ca.datamagic.noaa.dto.WidgetInfoDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.service.AppService;

public class CurrentWidgets {
    private static final Logger _logger = LogFactory.getLogger(CurrentWidgets.class);
    private static Set<WidgetInfoDTO> _allWidgets = new HashSet<WidgetInfoDTO>();
    private static Set<String> _enabledWidgets = new HashSet<String>();

    static {
        addWidget(CurrentTemperatureWidget.WIDGET_IDS_KEY, CurrentTemperatureWidget.PACKAGE_NAME, CurrentTemperatureWidget.CLASS_NAME);
        addWidget(CurrentPressureWidget.WIDGET_IDS_KEY, CurrentPressureWidget.PACKAGE_NAME, CurrentPressureWidget.CLASS_NAME);
        addWidget(CurrentVisibilityWidget.WIDGET_IDS_KEY, CurrentVisibilityWidget.PACKAGE_NAME, CurrentVisibilityWidget.CLASS_NAME);
        addWidget(CurrentDewPointWidget.WIDGET_IDS_KEY, CurrentDewPointWidget.PACKAGE_NAME, CurrentDewPointWidget.CLASS_NAME);
        addWidget(CurrentWindWidget.WIDGET_IDS_KEY, CurrentWindWidget.PACKAGE_NAME, CurrentWindWidget.CLASS_NAME);
        addWidget(CurrentHumidityWidget.WIDGET_IDS_KEY, CurrentHumidityWidget.PACKAGE_NAME, CurrentHumidityWidget.CLASS_NAME);
    }

    private static void addWidget(String widgetKey, String packageName, String className) {
        try {
            WidgetInfoDTO widgetInfo = new WidgetInfoDTO(widgetKey, packageName, className);
            _allWidgets.add(widgetInfo);
        } catch (Throwable t) {
            _logger.warning("Exception adding widget: " + widgetKey + ", Error: " + t.getMessage());
        }
    }

    public static synchronized void enableWidget(String widgetKey, String packageName, String className) {
        try {
            _logger.info("enableWidget");
            WidgetInfoDTO widgetInfo = new WidgetInfoDTO(widgetKey, packageName, className);
            _logger.info("widgetInfo: " + widgetInfo);
            _enabledWidgets.add(widgetInfo.toString());
            setEnabledWidgets(_enabledWidgets);
        } catch (Throwable t) {
            _logger.warning("Exception enabling widget: " + widgetKey + ", Error: " + t.getMessage());
        }
    }

    public static synchronized void disableWidget(String widgetKey, String packageName, String className) {
        try {
            _logger.info("disableWidget");
            WidgetInfoDTO widgetInfo = new WidgetInfoDTO(widgetKey, packageName, className);
            _logger.info("widgetInfo: " + widgetInfo);
            _enabledWidgets.remove(widgetInfo.toString());
            setEnabledWidgets(_enabledWidgets);
        } catch (Throwable t) {
            _logger.warning("Exception disabling widget: " + widgetKey + ", Error: " + t.getMessage());
        }
    }

    private static void setEnabledWidgets(Set<String> enabledWidgets) {
        if (enabledWidgets.size() > 0) {
            if (!AppService.isRunning()) {
                Context context = CurrentContext.getContext();
                if (context != null) {
                    context.startService(new Intent(context, AppService.class));
                }
            }
        } else {
            if (AppService.isRunning()) {
                Context context = CurrentContext.getContext();
                if (context != null) {
                    context.stopService(new Intent(context, AppService.class));
                }
            }
        }
    }

    public static synchronized void refreshWidgets(Context context) {
        try {
            _logger.info("refreshWidgets");
            if (context != null) {
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                for (WidgetInfoDTO widgetInfo : _allWidgets) {
                    _logger.info("widgetInfo: " + widgetInfo);
                    ComponentName componentName = new ComponentName(widgetInfo.getPackageName(), widgetInfo.getClassName());
                    int[] ids = manager.getAppWidgetIds(componentName);
                    Intent updateIntent = new Intent();
                    updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    updateIntent.putExtra(widgetInfo.getWidgetKey(), ids);
                    context.sendBroadcast(updateIntent);
                }
            }
        } catch (Throwable t) {
            _logger.log(Level.WARNING, "Unknown Exception in refresh widgets.", t);
        }
    }
}
