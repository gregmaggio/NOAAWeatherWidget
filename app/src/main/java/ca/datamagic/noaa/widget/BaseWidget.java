package ca.datamagic.noaa.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.logging.LogFactory;

public abstract class BaseWidget extends AppWidgetProvider {
    private static final Logger _logger = LogFactory.getLogger(BaseWidget.class);
    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";
    private static Hashtable<Integer, BaseWidget> _widgets = new Hashtable<Integer, BaseWidget>();
    protected RemoteViews _views = null;

    public BaseWidget() {
    }

    public RemoteViews getViews() {
        return _views;
    }

    protected static synchronized void addWidget(int appWidgetId, BaseWidget widget) {
        if (!_widgets.containsKey(appWidgetId)) {
            _widgets.put(appWidgetId, widget);
        }
    }

    protected static synchronized void removeWidget(int appWidgetId) {
        if (_widgets.containsKey(appWidgetId)) {
            _widgets.remove(appWidgetId);
        }
    }

    public static Enumeration<BaseWidget> getWidgets() {
        return _widgets.elements();
    }

    public abstract void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId);

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
            removeWidget(appWidgetIds[ii]);
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
