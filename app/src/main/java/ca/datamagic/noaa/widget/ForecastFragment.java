package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class ForecastFragment extends Fragment {
    private static final String _tag = "ForecastFragment";
    private static SimpleDateFormat _dayMonthFormat = new SimpleDateFormat("d/M");
    private static SimpleDateFormat _dayOfWeekFormat = new SimpleDateFormat("E");
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;
    private String _format = "24 hourly";
    private TableLayout _forecastTable = null;
    private LayoutInflater _inflater = null;
    private DWMLDTO _forecast = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_main, container, false);
        _forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        _inflater = inflater;
        render(_forecast);
        return view;
    }

    private TimeLayoutDTO getSelectedTimeLayout() {
        if (_forecast != null) {
            DataDTO data = _forecast.getData();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if (timeLayouts != null) {
                    for (int ii = 0; ii < timeLayouts.size(); ii++) {
                        TimeLayoutDTO timeLayout = timeLayouts.get(ii);
                        String summarization = timeLayout.getSummarization();
                        if (summarization != null) {
                            if (summarization.compareToIgnoreCase("24hourly") == 0) {
                                if (_format.compareToIgnoreCase("24 hourly") == 0) {
                                    return timeLayout;
                                }
                            }
                            if (summarization.compareToIgnoreCase("12hourly") == 0) {
                                if (_format.compareToIgnoreCase("12 hourly") == 0) {
                                    return timeLayout;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TemperatureDTO getMaximumTemperature() {
        if (_forecast != null) {
            DataDTO data = _forecast.getData();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            String type = temperature.getType();
                            if (type != null) {
                                if (type.compareToIgnoreCase("maximum") == 0) {
                                    return temperature;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TemperatureDTO getMinimumTemperature() {
        if (_forecast != null) {
            DataDTO data = _forecast.getData();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            String type = temperature.getType();
                            if (type != null) {
                                if (type.compareToIgnoreCase("minimum") == 0) {
                                    return temperature;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void render(DWMLDTO forecast) {
        _forecast = forecast;
        if (_forecastTable != null) {
            _forecastTable.removeAllViews();
            if (_forecast != null) {
                TimeLayoutDTO selectedTimeLayout = getSelectedTimeLayout();
                TemperatureDTO maximum = getMaximumTemperature();
                TemperatureDTO minimum = getMinimumTemperature();

                if (selectedTimeLayout != null) {
                    List<ValidTimeDTO> startTimes = selectedTimeLayout.getStartTimes();
                    if (startTimes != null) {
                        for (int ii = 0; ii < startTimes.size(); ii++) {
                            Log.d(_tag, "Processing day " + ii);
                            String dayMonth = "";
                            String dayOfWeek = "";
                            ValidTimeDTO startTime = startTimes.get(ii);
                            if (startTime.getTimeStamp() != null) {
                                dayMonth = _dayMonthFormat.format(startTime.getTimeStamp().getTime());
                                dayOfWeek = _dayOfWeekFormat.format(startTime.getTimeStamp().getTime());
                            }
                            Log.d(_tag, "dayMonth: " + dayMonth);
                            Log.d(_tag, "dayOfWeek: " + dayOfWeek);

                            String weatherSummary = "";
                            if (ii < _forecast.getData().getParameters().getWeather().getWeatherConditions().size())
                                weatherSummary = _forecast.getData().getParameters().getWeather().getWeatherConditions().get(ii).getWeatherSummary();
                            Log.d(_tag, "weatherSummary: " + weatherSummary);

                            String imageUrl = "";
                            if (ii < _forecast.getData().getParameters().getConditionsIcon().getIconLink().size())
                                imageUrl = _forecast.getData().getParameters().getConditionsIcon().getIconLink().get(ii);
                            Log.d(_tag, "imageUrl: " + imageUrl);

                            String min = "";
                            if (ii < minimum.getValues().size())
                                min = _temperatureFormat.format(minimum.getValues().get(ii));
                            Log.d(_tag, "min: " + min);

                            String max = "";
                            if (ii < maximum.getValues().size())
                                max = _temperatureFormat.format(maximum.getValues().get(ii));
                            Log.d(_tag, "max: " + max);

                            if ((dayMonth != null) && (dayMonth.length() > 0) &&
                                (dayOfWeek != null) && (dayOfWeek.length() > 0) &&
                                (weatherSummary != null) && (weatherSummary.length() > 0) &&
                                (imageUrl != null) && (imageUrl.length() > 0) &&
                                (min != null) && (min.length() > 0) &&
                                (max != null) && (max.length() > 0)) {
                                TableRow row = new TableRow(getContext());
                                row.setVisibility(View.VISIBLE);
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                LinearLayout item = (LinearLayout) _inflater.inflate(R.layout.forecast_item, null);
                                item.setVisibility(View.VISIBLE);

                                ImageView conditionsView = (ImageView) item.findViewById(R.id.conditions);
                                TextView weatherSummaryView = (TextView) item.findViewById(R.id.weatherSummary);
                                TextView dayMonthView = (TextView) item.findViewById(R.id.dayMonth);
                                TextView dayOfWeekView = (TextView) item.findViewById(R.id.dayOfWeek);
                                TextView minView = (TextView) item.findViewById(R.id.min);
                                TextView maxView = (TextView) item.findViewById(R.id.max);

                                weatherSummaryView.setText(weatherSummary);
                                dayMonthView.setText(dayMonth);
                                dayOfWeekView.setText(dayOfWeek);
                                minView.setText(min + _degrees);
                                maxView.setText(max + _degrees);

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
