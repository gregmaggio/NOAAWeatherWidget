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

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.ForecastDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.TimeStampDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class ForecastFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(ForecastFragment.class);
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;

    private int convertDipToPixels(float dips) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dips * density + 0.5f);
    }

    public ForecastsDTO getForecasts() {
        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            return mainActivity.getForecasts();
        }
        return null;
    }

    public String getTimeZoneId() {
        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            if ((mainActivity.getTimeZoneId() != null) && (mainActivity.getTimeZoneId().length() > 0)) {
                return mainActivity.getTimeZoneId();
            }
        }
        if (TimeZone.getDefault() != null) {
            return TimeZone.getDefault().getID();
        }
        return null;
    }

    public static ForecastFragment newInstance() {
        ForecastFragment fragment = new ForecastFragment();
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
        int totalWidth = forecastTable.getWidth();
        if (forecastTable != null) {
            ForecastsDTO forecasts = getForecasts();
            if ((forecasts != null) && (forecasts.getDescription() != null)) {
                TableRow descriptionRow = new TableRow(getContext());
                descriptionRow.setVisibility(View.VISIBLE);
                descriptionRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                LinearLayout description = (LinearLayout)inflater.inflate(R.layout.forecast_description, null);
                TextView descriptionText = description.findViewById(R.id.description);
                descriptionText.setText(forecasts.getDescription());
                description.setVisibility(View.VISIBLE);
                descriptionRow.addView(description);
                forecastTable.addView(descriptionRow);
            }
            String timeZoneId = getTimeZoneId();
            if ((forecasts != null) && (timeZoneId != null)) {
                TimeStampDTO timeStampDTO = new TimeStampDTO(timeZoneId);
                List<ForecastDTO> items = forecasts.getItems();
                if (items != null) {
                    PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
                    PreferencesDTO preferencesDTO = preferencesDAO.read();
                    String prevDayOfMonth = null;
                    String currDayOfMonth = null;
                    for (int ii = 0; ii < items.size(); ii++) {
                        if (items.get(ii).getTimeStamp() != null) {
                            timeStampDTO.setTimeStamp(items.get(ii).getTimeStamp());
                            currDayOfMonth = timeStampDTO.getDayOfMonth();
                        }
                        boolean showForecastDay = true;
                        if ((prevDayOfMonth != null) && (currDayOfMonth != null)) {
                            showForecastDay = prevDayOfMonth.compareToIgnoreCase(currDayOfMonth) != 0;
                        }
                        if (showForecastDay) {
                            TableRow forecastDayRow = new TableRow(getContext());
                            forecastDayRow.setVisibility(View.VISIBLE);
                            forecastDayRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            LinearLayout forecastDay = (LinearLayout)inflater.inflate(R.layout.forecast_day, null);
                            TextView dateView = forecastDay.findViewById(R.id.date);
                            dateView.setText(currDayOfMonth);
                            forecastDay.setVisibility(View.VISIBLE);
                            forecastDayRow.addView(forecastDay);
                            forecastTable.addView(forecastDayRow);

                            TableRow spacerRow = new TableRow(getContext());
                            spacerRow.setVisibility(View.VISIBLE);
                            spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                            forecastDivider.setVisibility(View.VISIBLE);
                            spacerRow.addView(forecastDivider);
                            forecastTable.addView(spacerRow);
                        }

                        TableRow row = new TableRow(getContext());
                        row.setVisibility(View.VISIBLE);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row.setTag(ii);

                        LinearLayout item = null;
                        if (preferencesDTO.isTextOnly()) {
                            item = (LinearLayout)inflater.inflate(R.layout.forecast_item_text, null);
                        } else {
                            item = (LinearLayout)inflater.inflate(R.layout.forecast_item, null);
                        }
                        item.setVisibility(View.VISIBLE);

                        TextView dayOfWeekView = (TextView) item.findViewById(R.id.dayOfWeek);
                        int dayOfWeekViewWidth = convertDipToPixels(120);
                        int dayOfWeekViewPaddingLeft = dayOfWeekView.getPaddingLeft();
                        int dayOfWeekViewPaddingRight = dayOfWeekView.getPaddingRight();
                        ImageView conditionsView = null;
                        int conditionsViewWidth = 0;
                        int conditionsViewPaddingLeft = 0;
                        int conditionsViewPaddingRight = 0;
                        if (!preferencesDTO.isTextOnly()) {
                            conditionsView = (ImageView) item.findViewById(R.id.conditions);
                            conditionsViewWidth = convertDipToPixels(40);
                            conditionsViewPaddingLeft = conditionsView.getPaddingLeft();
                            conditionsViewPaddingRight = conditionsView.getPaddingRight();
                        }
                        TextView weatherSummaryView = (TextView) item.findViewById(R.id.weatherSummary);
                        int weatherSummaryViewPaddingLeft = weatherSummaryView.getPaddingLeft();
                        int weatherSummaryViewPaddingRight = weatherSummaryView.getPaddingRight();
                        TextView temperatureView = (TextView) item.findViewById(R.id.temperature);
                        int temperatureViewWidth = convertDipToPixels(40);
                        if ((preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0) || (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0)) {
                            temperatureViewWidth = convertDipToPixels(47);
                            temperatureView.setLayoutParams(new LinearLayout.LayoutParams(temperatureViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                        }
                        int temperatureViewPaddingLeft = temperatureView.getPaddingLeft();
                        int temperatureViewPaddingRight = temperatureView.getPaddingRight();
                        int weatherSummaryViewWidth = (totalWidth - dayOfWeekViewPaddingLeft - dayOfWeekViewWidth - dayOfWeekViewPaddingRight - conditionsViewPaddingLeft - conditionsViewWidth - conditionsViewPaddingRight - weatherSummaryViewPaddingLeft - weatherSummaryViewPaddingRight - temperatureViewPaddingLeft - temperatureViewWidth - temperatureViewPaddingRight);
                        weatherSummaryView.setLayoutParams(new LinearLayout.LayoutParams(weatherSummaryViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                        if (items.get(ii).getSummary() != null) {
                            weatherSummaryView.setText(items.get(ii).getSummary());
                        }
                        if (items.get(ii).getPeriodName() != null) {
                            dayOfWeekView.setText(items.get(ii).getPeriodName());
                        }
                        if (items.get(ii).getTemperature() != null) {
                            if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0) {
                                Double fahrenheit = TemperatureCalculatorDTO.compute(items.get(ii).getTemperature(), items.get(ii).getTemperatureUnits(), TemperatureUnitsDTO.Fahrenheit);
                                Double celsius = TemperatureCalculatorDTO.compute(items.get(ii).getTemperature(), items.get(ii).getTemperatureUnits(), TemperatureUnitsDTO.Celsius);
                                if ((fahrenheit != null) && (celsius != null)) {
                                    temperatureView.setText(_temperatureFormat.format(fahrenheit.doubleValue()) + "/" + _temperatureFormat.format(celsius.doubleValue()) + _degrees);
                                }
                            } else if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0) {
                                Double fahrenheit = TemperatureCalculatorDTO.compute(items.get(ii).getTemperature(), items.get(ii).getTemperatureUnits(), TemperatureUnitsDTO.Fahrenheit);
                                Double celsius = TemperatureCalculatorDTO.compute(items.get(ii).getTemperature(), items.get(ii).getTemperatureUnits(), TemperatureUnitsDTO.Celsius);
                                if ((fahrenheit != null) && (celsius != null)) {
                                    temperatureView.setText(_temperatureFormat.format(celsius.doubleValue()) + "/" + _temperatureFormat.format(fahrenheit.doubleValue()) + _degrees);
                                }
                            } else {
                                Double temperature = TemperatureCalculatorDTO.compute(items.get(ii).getTemperature(), items.get(ii).getTemperatureUnits(), preferencesDTO.getTemperatureUnits());
                                if (temperature != null) {
                                    temperatureView.setText(_temperatureFormat.format(temperature.doubleValue()) + _degrees);
                                }
                            }
                        }

                        row.addView(item);
                        forecastTable.addView(row);

                        TableRow spacerRow = new TableRow(getContext());
                        spacerRow.setVisibility(View.VISIBLE);
                        spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                        forecastDivider.setVisibility(View.VISIBLE);
                        spacerRow.addView(forecastDivider);
                        forecastTable.addView(spacerRow);

                        if (!preferencesDTO.isTextOnly()) {
                            if ((items.get(ii).getImageUrl() != null) && (items.get(ii).getImageUrl().length() > 0)) {
                                ImageTask imageTask = new ImageTask(items.get(ii).getImageUrl(), conditionsView, false);
                                imageTask.execute((Void[]) null);
                            }
                        }

                        prevDayOfMonth = currDayOfMonth;
                    }
                }
            }
        }
        (new AccountingTask("Daily", "Render")).execute((Void[])null);
    }
}
