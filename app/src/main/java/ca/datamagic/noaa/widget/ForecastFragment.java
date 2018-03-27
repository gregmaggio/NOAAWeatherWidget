package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;

/**
 * Created by Greg on 1/10/2016.
 */
public class ForecastFragment extends Fragment implements Renderer {
    private static SimpleDateFormat _dayMonthFormat = new SimpleDateFormat("d/M");
    private static SimpleDateFormat _dayOfWeekFormat = new SimpleDateFormat("E");
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;
    private TableLayout _forecastTable = null;
    private LayoutInflater _inflater = null;
    private DWMLDTO _dwml = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_main, container, false);
        _forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        _inflater = inflater;
        return view;
    }

    public DWMLDTO getDWML() {
        return _dwml;
    }

    public void setDWML(DWMLDTO newVal) {
        _dwml = newVal;
    }

    private TimeLayoutDTO getDetailedTimeLayout() {
        if (_dwml != null) {
            DataDTO data = _dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if ((timeLayouts != null) && (timeLayouts.size() > 0)) {
                    return timeLayouts.get(0);
                }
            }
        }
        return null;
    }

    private TimeLayoutDTO getTimeLayout1() {
        if (_dwml != null) {
            DataDTO data = _dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if ((timeLayouts != null) && (timeLayouts.size() > 1)) {
                    return timeLayouts.get(1);
                }
            }
        }
        return null;
    }

    private TimeLayoutDTO getTimeLayout2() {
        if (_dwml != null) {
            DataDTO data = _dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if ((timeLayouts != null) && (timeLayouts.size() > 2)) {
                    return timeLayouts.get(2);
                }
            }
        }
        return null;
    }

    private TemperatureDTO getTemperature(String timeLayout) {
        if (_dwml != null) {
            DataDTO data = _dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            if (temperatures.get(ii).getTimeLayout().compareToIgnoreCase(timeLayout) == 0){
                                return temperatures.get(ii);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void render() {
        if (_forecastTable != null) {
            int count = _forecastTable.getChildCount();
            for (int ii = 0; ii < count; ii++) {
                View child = _forecastTable.getChildAt(ii);
                if (child instanceof TableRow) {
                    ((ViewGroup)child).removeAllViews();
                }
            }
            _forecastTable.removeAllViews();
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                TimeLayoutDTO detailedTimeLayout = getDetailedTimeLayout();
                TimeLayoutDTO timeLayout1 = getTimeLayout1();
                TimeLayoutDTO timeLayout2 = getTimeLayout2();
                TemperatureDTO temperature1 = getTemperature(timeLayout1.getLayoutKey());
                TemperatureDTO temperature2 = getTemperature(timeLayout2.getLayoutKey());

                if (detailedTimeLayout != null) {
                    List<ValidTimeDTO> forecastDays = detailedTimeLayout.getStartTimes();
                    if (forecastDays != null) {
                        for (int ii = 0; ii < forecastDays.size(); ii++) {
                            ValidTimeDTO startTime = forecastDays.get(ii);
                            String dayMonth = "";
                            String dayOfWeek = startTime.getPeriodName();
                            if (startTime.getTimeStamp() != null) {
                                dayMonth = _dayMonthFormat.format(startTime.getTimeStamp().getTime());
                            }

                            String weatherSummary = "";
                            if (ii < data.getParameters().getWeather().getWeatherConditions().size())
                                weatherSummary = data.getParameters().getWeather().getWeatherConditions().get(ii).getWeatherSummary();

                            String imageUrl = "";
                            if (ii < data.getParameters().getConditionsIcon().getIconLink().size())
                                imageUrl = data.getParameters().getConditionsIcon().getIconLink().get(ii);

                            String temperatureString = "";
                            if (temperatureString.length() < 1) {
                                if (temperature1 != null) {
                                    for (int jj = 0; jj < timeLayout1.getStartTimes().size(); jj++) {
                                        if (startTime.getPeriodName().compareToIgnoreCase(timeLayout1.getStartTimes().get(jj).getPeriodName()) == 0) {
                                            if (temperature1.getValues().get(jj) != Double.NaN) {
                                                temperatureString = _temperatureFormat.format(temperature1.getValues().get(jj));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                            if (temperatureString.length() < 1) {
                                if (temperature2 != null) {
                                    for (int jj = 0; jj < timeLayout2.getStartTimes().size(); jj++) {
                                        if (startTime.getPeriodName().compareToIgnoreCase(timeLayout2.getStartTimes().get(jj).getPeriodName()) == 0) {
                                            if (temperature2.getValues().get(jj) != Double.NaN) {
                                                temperatureString = _temperatureFormat.format(temperature2.getValues().get(jj));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }

                            if ((dayMonth != null) && (dayMonth.length() > 0) &&
                                    (dayOfWeek != null) && (dayOfWeek.length() > 0) &&
                                    (weatherSummary != null) && (weatherSummary.length() > 0) &&
                                    (imageUrl != null) && (imageUrl.length() > 0)) {
                                if (ii > 0) {
                                    TableRow spacerRow = new TableRow(getContext());
                                    spacerRow.setVisibility(View.VISIBLE);
                                    spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    LinearLayout forecastDivider = (LinearLayout) _inflater.inflate(R.layout.forecast_divider, null);
                                    forecastDivider.setVisibility(View.VISIBLE);
                                    _forecastTable.addView(spacerRow);
                                }
                                TableRow row = new TableRow(getContext());
                                row.setVisibility(View.VISIBLE);
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                LinearLayout item = (LinearLayout) _inflater.inflate(R.layout.forecast_item, null);
                                item.setVisibility(View.VISIBLE);

                                ImageView conditionsView = (ImageView) item.findViewById(R.id.conditions);
                                TextView weatherSummaryView = (TextView) item.findViewById(R.id.weatherSummary);
                                TextView dayMonthView = (TextView) item.findViewById(R.id.dayMonth);
                                TextView dayOfWeekView = (TextView) item.findViewById(R.id.dayOfWeek);
                                TextView temperature = (TextView) item.findViewById(R.id.temperature);

                                weatherSummaryView.setText(weatherSummary);
                                dayMonthView.setText(dayMonth);
                                dayOfWeekView.setText(dayOfWeek);

                                if (temperatureString.length() > 0) {
                                    temperature.setText(temperatureString + _degrees);
                                }

                                row.addView(item);
                                _forecastTable.addView(row);

                                ImageTask imageTask = new ImageTask(imageUrl.replace("http", "https"), conditionsView);
                                imageTask.execute((Void[]) null);
                            }
                        }
                    }
                }
            }
        }
    }
}
