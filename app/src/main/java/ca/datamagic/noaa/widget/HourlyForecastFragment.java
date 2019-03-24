package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.dto.PeriodDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TimeStampDTO;
import ca.datamagic.noaa.dto.TimeZoneDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HourlyForecastFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(HourlyForecastFragment.class);
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;

    public FeatureDTO getHourlyForecastFeature() {
        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            return mainActivity.getHourlyForecastFeature();
        }
        return null;
    }

    public TimeZoneDTO getTimeZone() {
        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            return mainActivity.getTimeZone();
        }
        return null;
    }

    public static HourlyForecastFragment newInstance() {
        HourlyForecastFragment fragment = new HourlyForecastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_main, container, false);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        _logger.info("onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void render() {
        View view = getView();
        LayoutInflater inflater = getLayoutInflater();
        if ((view != null) && (inflater != null)) {
            render(view, inflater);
        }
    }

    @Override
    public void cleanup() {

    }

    private void render(View view, LayoutInflater inflater) {
        TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        forecastTable.removeAllViews();
        if (forecastTable != null) {
            FeatureDTO hourlyForecastFeature = getHourlyForecastFeature();
            FeaturePropertiesDTO properties = null;
            if (hourlyForecastFeature != null) {
                properties = hourlyForecastFeature.getProperties();
            }
            TimeZoneDTO timeZone = getTimeZone();
            if ((properties != null) && (timeZone != null)) {
                TimeStampDTO timeStampDTO = new TimeStampDTO(timeZone.getTimeZoneId());
                PeriodDTO[] periods = properties.getPeriods();
                if (periods != null) {
                    PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
                    PreferencesDTO preferencesDTO = preferencesDAO.read();
                    String currentDayOfWeek = null;
                    for (int ii = 0; ii < periods.length; ii++) {
                        String dayOfWeek = null;
                        String hourOfDay = null;
                        if (periods[ii].getStartTime() != null) {
                            timeStampDTO.setTimeStamp(periods[ii].getStartTime());
                            dayOfWeek = timeStampDTO.getDayOfWeek();
                            hourOfDay = timeStampDTO.get12HourOfDay();
                        }
                        if (dayOfWeek != null) {
                            if ((currentDayOfWeek == null) || (currentDayOfWeek.compareToIgnoreCase(dayOfWeek) != 0)) {
                                TableRow row = new TableRow(getContext());
                                row.setVisibility(View.VISIBLE);
                                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                LinearLayout item = (LinearLayout)inflater.inflate(R.layout.hourly_forecast_day, null);
                                item.setVisibility(View.VISIBLE);

                                TextView dayOfWeekView = (TextView) item.findViewById(R.id.dayOfWeek);
                                dayOfWeekView.setText(dayOfWeek);

                                row.addView(item);
                                forecastTable.addView(row);
                            }
                        }

                        TableRow spacerRow = new TableRow(getContext());
                        spacerRow.setVisibility(View.VISIBLE);
                        spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                        forecastDivider.setVisibility(View.VISIBLE);
                        spacerRow.addView(forecastDivider);
                        forecastTable.addView(spacerRow);

                        TableRow row = new TableRow(getContext());
                        row.setVisibility(View.VISIBLE);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setTag(ii);

                        LinearLayout item = (LinearLayout)inflater.inflate(R.layout.hourly_forecast_item, null);
                        item.setVisibility(View.VISIBLE);

                        ImageView conditionsView = (ImageView) item.findViewById(R.id.conditions);
                        TextView weatherSummaryView = (TextView) item.findViewById(R.id.weatherSummary);
                        TextView hourOfDayView = (TextView) item.findViewById(R.id.hourOfDay);
                        TextView temperatureView = (TextView) item.findViewById(R.id.temperature);

                        if (hourOfDay != null) {
                            hourOfDayView.setText(hourOfDay);
                        }
                        if (periods[ii].getShortForecast() != null) {
                            weatherSummaryView.setText(periods[ii].getShortForecast());
                        }
                        if (periods[ii].getTemperature() != null) {
                            Double temperature = TemperatureCalculatorDTO.compute(periods[ii].getTemperature(), periods[ii].getTemperatureUnit(), preferencesDTO.getTemperatureUnits());
                            if (temperature != null) {
                                temperatureView.setText(_temperatureFormat.format(temperature.doubleValue()) + _degrees);
                            }
                        }

                        row.addView(item);
                        forecastTable.addView(row);

                        if ((periods[ii].getIcon() != null) && (periods[ii].getIcon().length() > 0)) {
                            ImageTask imageTask = new ImageTask(periods[ii].getIcon(), conditionsView, false);
                            imageTask.execute((Void[]) null);
                        }

                        currentDayOfWeek = dayOfWeek;
                    }
                }
            }
        }
    }
}
