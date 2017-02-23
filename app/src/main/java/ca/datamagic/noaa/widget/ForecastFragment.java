package ca.datamagic.noaa.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
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
import java.util.Calendar;
import java.util.Hashtable;
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
public class ForecastFragment extends Fragment {
    private static SimpleDateFormat _dayMonthFormat = new SimpleDateFormat("d/M");
    private static SimpleDateFormat _dayOfWeekFormat = new SimpleDateFormat("E");
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;
    private TableLayout _forecastTable = null;
    private LayoutInflater _inflater = null;
    private DWMLDTO _dwml = null;
    private Hashtable<String, TimeLayoutDTO> _timeLayouts = null;
    private TimeLayoutDTO _detailedTimeLayout = null;
    private TimeLayoutDTO _dayTimeLayout = null;
    private TimeLayoutDTO _nightTimeLayout = null;
    private TemperatureDTO _dayTimeMaxTemperature = null;
    private TemperatureDTO _nightTimeMinTemperature = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_main, container, false);
        _forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        _inflater = inflater;
        render(_dwml);
        return view;
    }

    private Hashtable<String, TimeLayoutDTO> getTimeLayouts() {
        if (_timeLayouts == null) {
            _timeLayouts = new Hashtable<String, TimeLayoutDTO>();
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                if (data != null) {
                    List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                    if (timeLayouts != null) {
                        for (int ii = 0; ii < timeLayouts.size(); ii++) {
                            _timeLayouts.put(timeLayouts.get(ii).getLayoutKey(), timeLayouts.get(ii));
                        }
                    }
                }
            }
        }
        return _timeLayouts;
    }

    private TimeLayoutDTO getDetailedTimeLayout() {
        if (_detailedTimeLayout == null) {
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                if (data != null) {
                    List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                    if ((timeLayouts != null) && (timeLayouts.size() > 0)) {
                        _detailedTimeLayout = timeLayouts.get(0);
                    }
                }
            }
        }
        return _detailedTimeLayout;
    }

    private TimeLayoutDTO getDayTimeLayout() {
        if (_dayTimeLayout == null) {
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                if (data != null) {
                    List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                    if ((timeLayouts != null) && (timeLayouts.size() > 1)) {
                        _dayTimeLayout = timeLayouts.get(1);
                    }
                }
            }
        }
        return _dayTimeLayout;
    }

    private TimeLayoutDTO getNightTimeLayout() {
        if (_nightTimeLayout == null) {
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                if (data != null) {
                    List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                    if ((timeLayouts != null) && (timeLayouts.size() > 2)) {
                        _nightTimeLayout = timeLayouts.get(2);
                    }
                }
            }
        }
        return _nightTimeLayout;
    }

    private TemperatureDTO getDayTimeMaxTemperature() {
        if (_dayTimeMaxTemperature == null) {
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                if (data != null) {
                    List<TemperatureDTO> temperatures = data.getParameters().getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            if (temperatures.get(ii).getType().compareToIgnoreCase("maximum") == 0) {
                                _dayTimeMaxTemperature = temperatures.get(ii);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return _dayTimeMaxTemperature;
    }

    private TemperatureDTO getNightTimeMinTemperature() {
        if (_nightTimeMinTemperature == null) {
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                if (data != null) {
                    List<TemperatureDTO> temperatures = data.getParameters().getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            if (temperatures.get(ii).getType().compareToIgnoreCase("minimum") == 0) {
                                _nightTimeMinTemperature = temperatures.get(ii);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return _nightTimeMinTemperature;
    }

    public void render(DWMLDTO dwml) {
        _dwml = dwml;
        if (_forecastTable != null) {
            _forecastTable.removeAllViews();
            if (_dwml != null) {
                DataDTO data = _dwml.getForecast();
                TimeLayoutDTO detailedTimeLayout = getDetailedTimeLayout();
                TimeLayoutDTO dayTimeLayout = getDayTimeLayout();
                TimeLayoutDTO nightTimeLayout = getNightTimeLayout();
                TemperatureDTO dayTimeMaxTemperature = getDayTimeMaxTemperature();
                TemperatureDTO nightTimeMinTemperature = getNightTimeMinTemperature();

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
                            int temperatureIndex = -1;
                            long startTimeInMillis = startTime.getTimeStamp().getTimeInMillis();
                            for (int jj = 0; jj < dayTimeLayout.getStartTimes().size(); jj++) {
                                long timeInMillis = dayTimeLayout.getStartTimes().get(jj).getTimeStamp().getTimeInMillis();
                                long difference = Math.abs(startTimeInMillis - timeInMillis);
                                if (difference < 3600001) {
                                    temperatureIndex = jj;
                                    break;
                                }
                            }
                            if (temperatureIndex > -1) {
                                if (temperatureIndex < dayTimeMaxTemperature.getValues().size()) {
                                    temperatureString = _temperatureFormat.format(dayTimeMaxTemperature.getValues().get(temperatureIndex));
                                }
                            } else {
                                for (int jj = 0; jj < nightTimeLayout.getStartTimes().size(); jj++) {
                                    long timeInMillis = nightTimeLayout.getStartTimes().get(jj).getTimeStamp().getTimeInMillis();
                                    long difference = Math.abs(startTimeInMillis - timeInMillis);
                                    if (difference < 3600001) {
                                        temperatureIndex = jj;
                                        break;
                                    }
                                }
                                if (temperatureIndex > -1) {
                                    if (temperatureIndex < nightTimeMinTemperature.getValues().size()) {
                                        temperatureString = _temperatureFormat.format(nightTimeMinTemperature.getValues().get(temperatureIndex));
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

                                ImageTask imageTask = new ImageTask(imageUrl, conditionsView);
                                imageTask.execute((Void[]) null);
                            }
                        }
                    }
                }
            }
        }
    }
}
