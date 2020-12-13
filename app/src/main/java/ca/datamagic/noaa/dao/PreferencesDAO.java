package ca.datamagic.noaa.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.preference.PreferenceManager;
import ca.datamagic.noaa.dto.HeightUnitsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.PressureUnitsDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.VisibilityUnitsDTO;
import ca.datamagic.noaa.dto.WindSpeedUnitsDTO;
import ca.datamagic.noaa.widget.BuildConfig;

public class PreferencesDAO {
    private SharedPreferences _preferences = null;

    public PreferencesDAO(Context context) {
        _preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public PreferencesDTO read() {
        PreferencesDTO dto = new PreferencesDTO();
        dto.setLatitude((double)_preferences.getFloat("latitude", 38.9967f));
        dto.setLongitude((double)_preferences.getFloat("longitude", -76.9275f));
        dto.setNumDays((int)_preferences.getLong("numDays", 7));
        dto.setUnit(_preferences.getString("unit", "e"));
        dto.setFormat(_preferences.getString("format", "24 hourly"));
        dto.setTemperatureUnits(_preferences.getString("temperatureUnits", TemperatureUnitsDTO.Fahrenheit));
        dto.setWindSpeedUnits(_preferences.getString("windSpeedUnits", WindSpeedUnitsDTO.Knots));
        dto.setPressureUnits(_preferences.getString("pressureUnits", PressureUnitsDTO.InchesOfMercury));
        dto.setVisibilityUnits(_preferences.getString("visibilityUnits", VisibilityUnitsDTO.StatuteMiles));
        dto.setHeightUnits(_preferences.getString("heightUnits", HeightUnitsDTO.Feet));
        dto.setTextOnly(_preferences.getBoolean("textOnly", Boolean.FALSE));
        dto.setDateFormat(_preferences.getString("dateFormat", "yyyy-MM-dd"));
        dto.setTimeFormat(_preferences.getString("timeFormat", "HH:mm"));
        dto.setWidgetFontColor(_preferences.getInt("widgetFontColor", Color.WHITE));
        dto.setShowNewFeatures(_preferences.getBoolean("showNewFeatures." + BuildConfig.VERSION_CODE, Boolean.TRUE));
        return dto;
    }

    public void write(PreferencesDTO dto) {
        double latitude = (dto.getLatitude() != null) ? dto.getLatitude().doubleValue() : 38.9967;
        double longitude = (dto.getLongitude() != null) ? dto.getLongitude().doubleValue() : -76.9275;
        int numDays = (dto.getNumDays() != null) ? dto.getNumDays().intValue() : 7;
        String unit = (dto.getUnit() != null) ? dto.getUnit() : "e";
        String format = (dto.getFormat() != null) ? dto.getFormat() : "24 hourly";
        String temperatureUnits = (dto.getTemperatureUnits() != null) ? dto.getTemperatureUnits() : TemperatureUnitsDTO.Fahrenheit;
        String windSpeedUnits = (dto.getWindSpeedUnits() != null) ? dto.getWindSpeedUnits() : WindSpeedUnitsDTO.Knots;
        String pressureUnits = (dto.getPressureUnits() != null) ? dto.getPressureUnits() : PressureUnitsDTO.InchesOfMercury;
        String visibilityUnits = (dto.getVisibilityUnits() != null) ? dto.getVisibilityUnits() : VisibilityUnitsDTO.StatuteMiles;
        String heightUnits = (dto.getHeightUnits() != null) ? dto.getHeightUnits() : HeightUnitsDTO.Feet;
        Boolean textOnly = (dto.isTextOnly() != null) ? dto.isTextOnly() : Boolean.FALSE;
        String dateFormat = (dto.getDateFormat() != null) ? dto.getDateFormat() : "yyyy-MM-dd";
        String timeFormat = (dto.getTimeFormat() != null) ? dto.getTimeFormat() : "HH:mm";
        Integer widgetFontColor = (dto.getWidgetFontColor() != null) ? dto.getWidgetFontColor() : Color.WHITE;
        Boolean showNewFeatures = (dto.getShowNewFeatures() != null) ? dto.getShowNewFeatures() : Boolean.TRUE;
        SharedPreferences.Editor editor = _preferences.edit();
        editor.putFloat("latitude", (float)latitude);
        editor.putFloat("longitude", (float)longitude);
        editor.putLong("editor", numDays);
        editor.putString("unit", unit);
        editor.putString("format", format);
        editor.putString("temperatureUnits", temperatureUnits);
        editor.putString("windSpeedUnits", windSpeedUnits);
        editor.putString("pressureUnits", pressureUnits);
        editor.putString("visibilityUnits", visibilityUnits);
        editor.putString("heightUnits", heightUnits);
        editor.putBoolean("textOnly", textOnly);
        editor.putString("dateFormat", dateFormat);
        editor.putString("timeFormat", timeFormat);
        editor.putInt("widgetFontColor", widgetFontColor);
        editor.putBoolean("showNewFeatures." + BuildConfig.VERSION_CODE, showNewFeatures);
        editor.commit();
    }
}
