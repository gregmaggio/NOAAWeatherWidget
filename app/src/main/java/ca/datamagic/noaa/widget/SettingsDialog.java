package ca.datamagic.noaa.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.HeightUnitsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.PressureUnitsDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.VisibilityUnitsDTO;
import ca.datamagic.noaa.dto.WindSpeedUnitsDTO;

public class SettingsDialog extends Dialog implements View.OnClickListener {
    private static final String[] _hours = new String[]
            {
                    "1 Hour",
                    "2 Hours",
                    "3 Hours",
                    "4 Hours",
                    "5 Hours",
                    "6 Hours",
                    "7 Hours",
                    "8 Hours",
                    "9 Hours",
                    "10 Hours",
                    "11 Hours",
                    "12 Hours",
                    "13 Hours",
                    "14 Hours",
                    "15 Hours"
            };
    private static final String[] _seconds = new String[]
            {
                    "1 Second",
                    "2 Seconds",
                    "3 Seconds",
                    "4 Seconds",
                    "5 Seconds",
                    "6 Seconds",
                    "7 Seconds",
                    "8 Seconds",
                    "9 Seconds",
                    "10 Seconds"
            };
    private RadioGroup _temperatureUnits = null;
    private RadioGroup _windSpeedUnits = null;
    private RadioGroup _pressureUnits = null;
    private RadioGroup _visibilityUnits = null;
    private RadioGroup _heightUnits = null;
    private CheckBox _textOnly = null;
    private RadioGroup _dateFormats = null;
    private RadioGroup _timeFormats = null;
    private RadioGroup _widgetFontColor = null;
    private Spinner _radarTotalMinutes = null;
    private Spinner _radarDelaySeconds = null;
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

    private static int radarTotalMinutesToIndex(int radarTotalMinutes) {
        int hours = (int)Math.floor((double)radarTotalMinutes / 60.0);
        return hours - 1;
    }

    private static int indexToRadarTotalMinutes(int index) {
        int hours = index + 1;
        return  hours * 60;
    }

    private static int radarDelaySecondsToIndex(int radarDelaySeconds) {
        return  radarDelaySeconds - 1;
    }

    private static int indexToRadarDelaySeconds(int index) {
        return index + 1;
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
        _dateFormats = (RadioGroup)findViewById(R.id.dateFormats);
        _timeFormats = (RadioGroup)findViewById(R.id.timeFormats);
        _widgetFontColor = (RadioGroup)findViewById(R.id.widgetFontColor);
        _saveSettingsButton = (Button)findViewById(R.id.saveSettings);
        _cancelSettingsButton = (Button)findViewById(R.id.cancelSettings);
        _saveSettingsProgress = (ProgressBar)findViewById(R.id.saveSettingsProgress);
        _radarTotalMinutes = (Spinner)findViewById(R.id.radarTotalMinutes);
        _radarDelaySeconds = (Spinner)findViewById(R.id.radarDelaySeconds);
        _saveSettingsButton.setOnClickListener(this);
        _cancelSettingsButton.setOnClickListener(this);

        ArrayAdapter<String> radarTotalMinutesAdapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, _hours);
        _radarTotalMinutes.setAdapter(radarTotalMinutesAdapter);

        ArrayAdapter<String> radarDelaySecondsAdapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, _seconds);
        _radarDelaySeconds.setAdapter(radarDelaySecondsAdapter);

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
        if (dto.getDateFormat() != null) {
            if (dto.getDateFormat().compareToIgnoreCase("yyyy-MM-dd") == 0) {
                _dateFormats.check(R.id.dateFormatsYYYYMMDDDashes);
            } else if (dto.getDateFormat().compareToIgnoreCase("MM/dd/yyyy") == 0) {
                _dateFormats.check(R.id.dateFormatsMMDDYYYYSlashes);
            } else if (dto.getDateFormat().compareToIgnoreCase("dd/MM/yyyy") == 0) {
                _dateFormats.check(R.id.dateFormatsDDMMYYYYSlashes);
            } else if (dto.getDateFormat().compareToIgnoreCase("dd-MM-yyyy") == 0) {
                _dateFormats.check(R.id.dateFormatsDDMMYYYYDashes);
            }
        }
        if (dto.getTimeFormat() != null) {
            if (dto.getTimeFormat().compareToIgnoreCase("HH:mm") == 0) {
                _timeFormats.check(R.id.timeFormats24Hour);
            } else  if (dto.getTimeFormat().compareToIgnoreCase("h:mm a") == 0) {
                _timeFormats.check(R.id.timeFormats12Hour);
            }
        }
        if (dto.getWidgetFontColor() != null) {
            if (dto.getWidgetFontColor().intValue() == Color.BLACK) {
                _widgetFontColor.check(R.id.widgetFontColorBlack);
            } else if (dto.getWidgetFontColor().intValue() == Color.WHITE) {
                _widgetFontColor.check(R.id.widgetFontColorWhite);
            } else if (dto.getWidgetFontColor().intValue() == Color.RED) {
                _widgetFontColor.check(R.id.widgetFontColorRed);
            } else if (dto.getWidgetFontColor().intValue() == Color.GREEN) {
                _widgetFontColor.check(R.id.widgetFontColorGreen);
            } else if (dto.getWidgetFontColor().intValue() == Color.BLUE) {
                _widgetFontColor.check(R.id.widgetFontColorBlue);
            } else if (dto.getWidgetFontColor().intValue() == Color.YELLOW) {
                _widgetFontColor.check(R.id.widgetFontColorYellow);
            }
        }
        this.setDateTimeHint(R.id.dateFormatsYYYYMMDDDashes);
        this.setDateTimeHint(R.id.dateFormatsMMDDYYYYSlashes);
        this.setDateTimeHint(R.id.dateFormatsDDMMYYYYSlashes);
        this.setDateTimeHint(R.id.dateFormatsDDMMYYYYDashes);
        _radarTotalMinutes.setSelection(radarTotalMinutesToIndex(dto.getRadarTotalMinutes()));
        _radarDelaySeconds.setSelection(radarDelaySecondsToIndex(dto.getRadarDelaySeconds()));
        (new AccountingTask("Settings", "Show")).execute();
    }

    private void setDateTimeHint(int id) {
        RadioButton radioButton = (RadioButton)findViewById(id);
        SimpleDateFormat dateFormat = new SimpleDateFormat(radioButton.getText().toString());
        radioButton.setHint(dateFormat.format(new Date()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveSettings:
                saveSettings();
                (new AccountingTask("Settings", "Save")).execute();
                break;
            case R.id.cancelSettings:
                dismiss();
                (new AccountingTask("Settings", "Cancel")).execute();
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
            RadioButton selectedDateFormat = (RadioButton)findViewById(_dateFormats.getCheckedRadioButtonId());
            dto.setDateFormat(selectedDateFormat.getText().toString());
            switch (_timeFormats.getCheckedRadioButtonId()) {
                case R.id.timeFormats24Hour: dto.setTimeFormat("HH:mm"); break;
                case R.id.timeFormats12Hour: dto.setTimeFormat("h:mm a"); break;
            }
            switch (_widgetFontColor.getCheckedRadioButtonId()) {
                case R.id.widgetFontColorBlack:
                    dto.setWidgetFontColor(Color.BLACK);
                    break;
                case R.id.widgetFontColorWhite:
                    dto.setWidgetFontColor(Color.WHITE);
                    break;
                case R.id.widgetFontColorRed:
                    dto.setWidgetFontColor(Color.RED);
                    break;
                case R.id.widgetFontColorGreen:
                    dto.setWidgetFontColor(Color.GREEN);
                    break;
                case R.id.widgetFontColorBlue:
                    dto.setWidgetFontColor(Color.BLUE);
                    break;
                case R.id.widgetFontColorYellow:
                    dto.setWidgetFontColor(Color.YELLOW);
                    break;
            }

            dto.setRadarTotalMinutes(indexToRadarTotalMinutes(_radarTotalMinutes.getSelectedItemPosition()));
            dto.setRadarDelaySeconds(indexToRadarDelaySeconds(_radarDelaySeconds.getSelectedItemPosition()));

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
