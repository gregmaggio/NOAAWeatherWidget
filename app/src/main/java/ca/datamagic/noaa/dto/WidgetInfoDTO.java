package ca.datamagic.noaa.dto;

import androidx.annotation.Nullable;

public class WidgetInfoDTO {
    private String _widgetKey = null;
    private String _packageName = null;
    private String _className = null;
    private Class<?> _widgetClass = null;

    public WidgetInfoDTO() {

    }

    public WidgetInfoDTO(String widgetKey, String packageName, String className) throws ClassNotFoundException {
        _widgetKey = widgetKey;
        _packageName = packageName;
        _className = className;
        _widgetClass = Class.forName(_className);
    }

    public WidgetInfoDTO(String text) throws ClassNotFoundException {
        String[] items = text.split("|");
        if (items.length == 3) {
            _widgetKey = items[0];
            _packageName = items[1];
            _className = items[2];
            _widgetClass = Class.forName(_className);
        }
    }

    public String getWidgetKey() {
        return _widgetKey;
    }

    public String getPackageName() {
        return _packageName;
    }

    public String getClassName() {
        return _className;
    }


    @Override
    public int hashCode() {
        return _className.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null) {
            if (obj instanceof WidgetInfoDTO) {
                return toString().compareToIgnoreCase(obj.toString()) == 0;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(_widgetKey);
        builder.append("|");
        builder.append(_packageName);
        builder.append("|");
        builder.append(_className);
        return builder.toString();
    }
}
