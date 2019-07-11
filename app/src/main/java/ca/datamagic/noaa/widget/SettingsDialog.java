package ca.datamagic.noaa.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.HeightUnitsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.PressureUnitsDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.VisibilityUnitsDTO;
import ca.datamagic.noaa.dto.WindSpeedUnitsDTO;

public class SettingsDialog extends Dialog implements View.OnClickListener {
    private RadioGroup _temperatureUnits = null;
    private RadioGroup _windSpeedUnits = null;
    private RadioGroup _pressureUnits = null;
    private RadioGroup _visibilityUnits = null;
    private RadioGroup _heightUnits = null;
    private CheckBox _textOnly = null;
    private Button _saveSettingsButton = null;
    private Button _cancelSettingsButton = null;
    private ProgressBar _saveSettingsProgress = null;
    private boolean _processing = false;

    public SettingsDialog(@NonNull Context context) {
        super(context);
    }

    public SettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SettingsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            Rect displayRectangle = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            View layout = getLayoutInflater().inflate(R.layout.settings_main, null);
            layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
            layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));
            setContentView(layout);
        } else {
            setContentView(R.layout.settings_main);
        }
        setCancelable(false);
        setTitle(R.string.settings_title);
        _temperatureUnits = (RadioGroup)findViewById(R.id.temperatureUnits);
        _windSpeedUnits = (RadioGroup)findViewById(R.id.windSpeedUnits);
        _pressureUnits = (RadioGroup)findViewById(R.id.pressureUnits);
        _visibilityUnits = (RadioGroup)findViewById(R.id.visibilityUnits);
        _heightUnits = (RadioGroup)findViewById(R.id.heightUnits);
        _textOnly = (CheckBox)findViewById(R.id.textOnly);
        _saveSettingsButton = (Button)findViewById(R.id.saveSettings);
        _cancelSettingsButton = (Button)findViewById(R.id.cancelSettings);
        _saveSettingsProgress = (ProgressBar)findViewById(R.id.saveSettingsProgress);
        _saveSettingsButton.setOnClickListener(this);
        _cancelSettingsButton.setOnClickListener(this);

        PreferencesDAO dao = new PreferencesDAO(getContext());
        PreferencesDTO dto = dao.read();

        String temperatureUnits = dto.getTemperatureUnits();
        if ((temperatureUnits != null) && (temperatureUnits.compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0)) {
            _temperatureUnits.check(R.id.temperatureUnitsFahrenheit);
        } else if ((temperatureUnits != null) && (temperatureUnits.compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0)) {
            _temperatureUnits.check(R.id.temperatureUnitsCelsius);
        } else if ((temperatureUnits != null) && (temperatureUnits.compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0)) {
            _temperatureUnits.check(R.id.temperatureUnitsFC);
        } else if ((temperatureUnits != null) && (temperatureUnits.compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0)) {
            _temperatureUnits.check(R.id.temperatureUnitsCF);
        }
        String windSpeedUnits = dto.getWindSpeedUnits();
        if ((windSpeedUnits != null) && (windSpeedUnits.compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0)) {
            _windSpeedUnits.check(R.id.windSpeedUnitsKnots);
        } else if ((windSpeedUnits != null) && (windSpeedUnits.compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0)) {
            _windSpeedUnits.check(R.id.windSpeedUnitsMilesPerHour);
        } else if ((windSpeedUnits != null) && (windSpeedUnits.compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0)) {
            _windSpeedUnits.check(R.id.windSpeedUnitsKilometersPerHour);
        } else {
            _windSpeedUnits.check(R.id.windSpeedUnitsKnots);
        }
        String pressureUnits = dto.getPressureUnits();
        if ((pressureUnits != null) && (pressureUnits.compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0)) {
            _pressureUnits.check(R.id.pressureUnitsInchesOfMercury);
        } else if ((pressureUnits != null) && (pressureUnits.compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0)) {
            _pressureUnits.check(R.id.pressureUnitsKiloPascals);
        } else if ((pressureUnits != null) && (pressureUnits.compareToIgnoreCase(PressureUnitsDTO.StationPressure) == 0)) {
            _pressureUnits.check(R.id.pressureUnitsStationPressure);
        } else {
            _pressureUnits.check(R.id.pressureUnitsInchesOfMercury);
        }
        String visibilityUnits = dto.getVisibilityUnits();
        if ((visibilityUnits != null) && (visibilityUnits.compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0)) {
            _visibilityUnits.check(R.id.visibilityUnitsStatuteMiles);
        } else if ((visibilityUnits != null) && (visibilityUnits.compareToIgnoreCase(VisibilityUnitsDTO.Miles) == 0)) {
            _visibilityUnits.check(R.id.visibilityUnitsMiles);
        } else if ((visibilityUnits != null) && (visibilityUnits.compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0)) {
            _visibilityUnits.check(R.id.visibilityUnitsKilometers);
        } else {
            _visibilityUnits.check(R.id.visibilityUnitsStatuteMiles);
        }
        String heightUnits = dto.getHeightUnits();
        if ((heightUnits != null) && (heightUnits.compareToIgnoreCase(HeightUnitsDTO.Feet) == 0)) {
            _heightUnits.check(R.id.heightUnitsFeet);
        } else if ((heightUnits != null) && (heightUnits.compareToIgnoreCase(HeightUnitsDTO.Meters) == 0)) {
            _heightUnits.check(R.id.heightUnitsMeters);
        } else {
            _heightUnits.check(R.id.heightUnitsFeet);
        }
        if (dto.isTextOnly() != null) {
            _textOnly.setChecked(dto.isTextOnly());
        } else {
            _textOnly.setChecked(false);
        }
        (new AccountingTask("Settings", "Show")).execute((Void[])null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveSettings:
                saveSettings();
                (new AccountingTask("Settings", "Save")).execute((Void[])null);
                break;
            case R.id.cancelSettings:
                dismiss();
                (new AccountingTask("Settings", "Cancel")).execute((Void[])null);
                break;
        }
    }

    private void saveSettings() {
        if (_processing) {
            return;
        }
        try {
            _processing = true;
            _saveSettingsProgress.setVisibility(View.VISIBLE);

            PreferencesDAO dao = new PreferencesDAO(getContext());
            PreferencesDTO dto = dao.read();

            switch (_temperatureUnits.getCheckedRadioButtonId()) {
                case R.id.temperatureUnitsFahrenheit:
                    dto.setTemperatureUnits(TemperatureUnitsDTO.Fahrenheit);
                    break;
                case R.id.temperatureUnitsCelsius:
                    dto.setTemperatureUnits(TemperatureUnitsDTO.Celsius);
                    break;
                case R.id.temperatureUnitsFC:
                    dto.setTemperatureUnits(TemperatureUnitsDTO.FC);
                    break;
                case R.id.temperatureUnitsCF:
                    dto.setTemperatureUnits(TemperatureUnitsDTO.CF);
                    break;
            }
            switch (_windSpeedUnits.getCheckedRadioButtonId()) {
                case R.id.windSpeedUnitsKnots:
                    dto.setWindSpeedUnits(WindSpeedUnitsDTO.Knots);
                    break;
                case R.id.windSpeedUnitsMilesPerHour:
                    dto.setWindSpeedUnits(WindSpeedUnitsDTO.MilesPerHour);
                    break;
                case R.id.windSpeedUnitsKilometersPerHour:
                    dto.setWindSpeedUnits(WindSpeedUnitsDTO.KilometersPerHour);
                    break;
            }
            switch (_pressureUnits.getCheckedRadioButtonId()) {
                case R.id.pressureUnitsInchesOfMercury:
                    dto.setPressureUnits(PressureUnitsDTO.InchesOfMercury);
                    break;
                case R.id.pressureUnitsKiloPascals:
                    dto.setPressureUnits(PressureUnitsDTO.KiloPascals);
                    break;
                case R.id.pressureUnitsStationPressure:
                    dto.setPressureUnits(PressureUnitsDTO.StationPressure);
                    break;
            }
            switch (_visibilityUnits.getCheckedRadioButtonId()) {
                case R.id.visibilityUnitsStatuteMiles:
                    dto.setVisibilityUnits(VisibilityUnitsDTO.StatuteMiles);
                    break;
                case R.id.visibilityUnitsMiles:
                    dto.setVisibilityUnits(VisibilityUnitsDTO.Miles);
                    break;
                case R.id.visibilityUnitsKilometers:
                    dto.setVisibilityUnits(VisibilityUnitsDTO.Kilometers);
                    break;
            }
            switch (_heightUnits.getCheckedRadioButtonId()) {
                case R.id.heightUnitsFeet:
                    dto.setHeightUnits(HeightUnitsDTO.Feet);
                    break;
                case R.id.heightUnitsMeters:
                    dto.setHeightUnits(HeightUnitsDTO.Meters);
                    break;
            }
            if (_textOnly.isChecked()) {
                dto.setTextOnly(true);
            } else {
                dto.setTextOnly(false);
            }
            dao.write(dto);

            _saveSettingsProgress.setVisibility(View.GONE);

            dismiss();

            if (MainActivity.getThisInstance() != null) {
                MainActivity.getThisInstance().actionRefresh();
            }
        } catch (Throwable t) {
            // TODO: Show error
            _processing = false;
            _saveSettingsProgress.setVisibility(View.GONE);
        }
    }
}
