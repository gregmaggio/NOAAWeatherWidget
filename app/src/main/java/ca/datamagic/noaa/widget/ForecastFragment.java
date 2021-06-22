package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.current.CurrentDailyForecast;
import ca.datamagic.noaa.current.CurrentFeature;
import ca.datamagic.noaa.current.CurrentForecasts;
import ca.datamagic.noaa.current.CurrentStation;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.dto.ForecastDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.PeriodDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.TimeStampDTO;
import ca.datamagic.noaa.dto.WindSpeedCalculatorDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class ForecastFragment extends Fragment implements Renderer, NonSwipeableFragment {
    private static Logger _logger = LogFactory.getLogger(ForecastFragment.class);
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static DecimalFormat _windFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;
    private static Pattern _windSpeedPattern = Pattern.compile("(\\d+)\\s(\\w+)", Pattern.CASE_INSENSITIVE);
    private static Pattern _windSpeedRangePattern = Pattern.compile("(\\d+)\\sto\\s(\\d+)\\s(\\w+)", Pattern.CASE_INSENSITIVE);

    private int convertDipToPixels(float dips) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dips * density + 0.5f);
    }

    public String getTimeZoneId() {
        String timeZoneId = null;
        StationDTO station = CurrentStation.getStation();
        if (station != null) {
            timeZoneId = station.getTimeZoneId();
        }
        if ((timeZoneId == null) || (timeZoneId.length() < 1)) {
            FeatureDTO dailyForecastFeature = CurrentDailyForecast.getDailyForecastFeature();
            if (dailyForecastFeature != null) {
                FeaturePropertiesDTO featureProperties = dailyForecastFeature.getProperties();
                if (featureProperties != null) {
                    timeZoneId = featureProperties.getTimeZone();
                }
            }
        }
        return timeZoneId;
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
        try {
            _logger.info("render");
            if (!MainActivity.getThisInstance().isFragmentActive(this)) {
                return;
            }
            View view = getView();
            _logger.info("view: " + view);
            LayoutInflater inflater = getLayoutInflater();
            _logger.info("inflater: " + inflater);
            if ((view != null) && (inflater != null)) {
                render(view, inflater);
            }
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute((Void[])null);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean canSwipe(float x, float y) {
        return true;
    }

    private void render(View view, LayoutInflater inflater) {
        TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        int childCount = forecastTable.getChildCount();
        _logger.info("childCount: " + childCount);
        if (childCount > 0) {
            forecastTable.removeAllViews();
        }
        int totalWidth = forecastTable.getWidth();
        _logger.info("totalWidth: " + totalWidth);
        if (totalWidth < 1) {
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute((Void[])null);
            _logger.info("Excute Render Task");
            return;
        }
        FeatureDTO feature = CurrentFeature.getFeature();
        FeatureDTO dailyForecastFeature = CurrentDailyForecast.getDailyForecastFeature();
        ForecastsDTO forecasts = CurrentForecasts.getForecasts();
        if ((feature != null) && (dailyForecastFeature != null)) {
            TextView forecastErrorLabel = view.findViewById(R.id.forecast_error_label);
            forecastErrorLabel.setVisibility(View.GONE);
            render(view, inflater, feature, dailyForecastFeature);
        } else  if (forecasts != null) {
            TextView forecastErrorLabel = view.findViewById(R.id.forecast_error_label);
            forecastErrorLabel.setVisibility(View.GONE);
            render(view, inflater, forecasts);
        } else {
            // Render something here
            TextView forecastErrorLabel = view.findViewById(R.id.forecast_error_label);
            forecastErrorLabel.setVisibility(View.VISIBLE);
        }
        (new AccountingTask("Daily", "Render")).execute((Void[])null);
    }

    private void render(final View view, LayoutInflater inflater, FeatureDTO feature, final FeatureDTO dailyForecastFeature) {
        final TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        final List<TableRow> rows = new ArrayList<TableRow>();
        int totalWidth = forecastTable.getWidth();
        String city = null;
        String state = null;
        FeaturePropertiesDTO featureProperties = null;
        if (feature != null) {
            featureProperties = feature.getProperties();
        }
        if (featureProperties != null) {
            city = featureProperties.getCity();
            state = featureProperties.getState();
        }
        FeaturePropertiesDTO dailyForecastFeatureProperties = null;
        if (dailyForecastFeature != null) {
            dailyForecastFeatureProperties = dailyForecastFeature.getProperties();
        }
        String timeZoneId = getTimeZoneId();
        if ((dailyForecastFeatureProperties != null) && (timeZoneId != null)) {
            if ((city != null) && (state != null)) {
                TableRow descriptionRow = new TableRow(getContext());
                descriptionRow.setVisibility(View.VISIBLE);
                descriptionRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                LinearLayout description = (LinearLayout) inflater.inflate(R.layout.forecast_description, null);
                TextView descriptionText = description.findViewById(R.id.description);
                descriptionText.setText(city + ", " + state);
                description.setVisibility(View.VISIBLE);
                descriptionRow.addView(description);
                rows.add(descriptionRow);
            }
            TimeStampDTO timeStampDTO = new TimeStampDTO(timeZoneId);
            PeriodDTO[] periods = dailyForecastFeatureProperties.getPeriods();
            if (periods != null) {
                PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
                PreferencesDTO preferencesDTO = preferencesDAO.read();
                String prevDayOfMonth = null;
                String currDayOfMonth = null;
                for (int ii = 0; ii < periods.length; ii++) {
                    String startTime = periods[ii].getStartTime();
                    if (startTime != null) {
                        timeStampDTO.setTimeStamp(startTime);
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
                        rows.add(forecastDayRow);

                        TableRow spacerRow = new TableRow(getContext());
                        spacerRow.setVisibility(View.VISIBLE);
                        spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                        forecastDivider.setVisibility(View.VISIBLE);
                        spacerRow.addView(forecastDivider);
                        rows.add(spacerRow);
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
                    LinearLayout temperatureWindLayout = item.findViewById(R.id.temperatureWindLayout);
                    TextView temperatureView = (TextView) item.findViewById(R.id.temperature);
                    TextView windTextView = item.findViewById(R.id.wind);
                    int temperatureViewWidth = convertDipToPixels(64);
                    int weatherSummaryViewWidth = (totalWidth - dayOfWeekViewPaddingLeft - dayOfWeekViewWidth - dayOfWeekViewPaddingRight - conditionsViewPaddingLeft - conditionsViewWidth - conditionsViewPaddingRight - weatherSummaryViewPaddingLeft - weatherSummaryViewPaddingRight - temperatureViewWidth);
                    weatherSummaryView.setLayoutParams(new LinearLayout.LayoutParams(weatherSummaryViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

                    String shortForecast = periods[ii].getShortForecast();
                    if (shortForecast != null) {
                        weatherSummaryView.setText(shortForecast);
                    }
                    String name = periods[ii].getName();
                    if (name != null) {
                        dayOfWeekView.setText(name);
                    }

                    Double temperature = periods[ii].getTemperature();
                    String temperatureUnit = periods[ii].getTemperatureUnit();
                    if ((temperature != null) && (temperatureUnit != null)) {
                        if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0) {
                            Double fahrenheit = TemperatureCalculatorDTO.compute(temperature, temperatureUnit, TemperatureUnitsDTO.Fahrenheit);
                            Double celsius = TemperatureCalculatorDTO.compute(temperature, temperatureUnit, TemperatureUnitsDTO.Celsius);
                            if ((fahrenheit != null) && (celsius != null)) {
                                temperatureView.setText(_temperatureFormat.format(fahrenheit.doubleValue()) + "/" + _temperatureFormat.format(celsius.doubleValue()) + _degrees);
                            }
                        } else if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0) {
                            Double fahrenheit = TemperatureCalculatorDTO.compute(temperature, temperatureUnit, TemperatureUnitsDTO.Fahrenheit);
                            Double celsius = TemperatureCalculatorDTO.compute(temperature, temperatureUnit, TemperatureUnitsDTO.Celsius);
                            if ((fahrenheit != null) && (celsius != null)) {
                                temperatureView.setText(_temperatureFormat.format(celsius.doubleValue()) + "/" + _temperatureFormat.format(fahrenheit.doubleValue()) + _degrees);
                            }
                        } else {
                            temperature = TemperatureCalculatorDTO.compute(temperature, temperatureUnit, preferencesDTO.getTemperatureUnits());
                            if (temperature != null) {
                                temperatureView.setText(_temperatureFormat.format(temperature.doubleValue()) + _degrees);
                            }
                        }
                    }

                    String formattedWind = "";
                    String windSpeed = periods[ii].getWindSpeed();
                    String windDirection = periods[ii].getWindDirection();
                    if ((windSpeed != null) && (windSpeed.length() > 0)) {
                        Matcher windSpeedMatcher = _windSpeedPattern.matcher(windSpeed);
                        if (windSpeedMatcher.matches()) {
                            Double speed = Double.parseDouble(windSpeedMatcher.group(1));
                            String units = windSpeedMatcher.group(2);
                            Double finalSpeed = WindSpeedCalculatorDTO.compute(speed, units, preferencesDTO.getWindSpeedUnits());
                            if (finalSpeed != null) {
                                formattedWind = _windFormat.format(finalSpeed);
                                if ((windDirection != null) && (windDirection.length() > 0)) {
                                    formattedWind += " " + windDirection;
                                }
                            }
                        } else {
                            Matcher windSpeedRangeMatcher = _windSpeedRangePattern.matcher(windSpeed);
                            if (windSpeedRangeMatcher.matches()) {
                                Double speed1 = Double.parseDouble(windSpeedRangeMatcher.group(1));
                                Double speed2 = Double.parseDouble(windSpeedRangeMatcher.group(2));
                                String units = windSpeedRangeMatcher.group(3);
                                Double finalSpeed1 = WindSpeedCalculatorDTO.compute(speed1, units, preferencesDTO.getWindSpeedUnits());
                                Double finalSpeed2 = WindSpeedCalculatorDTO.compute(speed2, units, preferencesDTO.getWindSpeedUnits());
                                if ((finalSpeed1 != null) && (finalSpeed2 != null)) {
                                    formattedWind = _windFormat.format(finalSpeed1);
                                    formattedWind += " to " + _windFormat.format(finalSpeed2);
                                    if ((windDirection != null) && (windDirection.length() > 0)) {
                                        formattedWind += " " + windDirection;
                                    }
                                }
                            }
                        }
                    }
                    windTextView.setText(formattedWind);

                    row.addView(item);
                    rows.add(row);

                    TableRow spacerRow = new TableRow(getContext());
                    spacerRow.setVisibility(View.VISIBLE);
                    spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                    forecastDivider.setVisibility(View.VISIBLE);
                    spacerRow.addView(forecastDivider);
                    rows.add(spacerRow);

                    if (!preferencesDTO.isTextOnly()) {
                        String icon = periods[ii].getIcon();
                        if ((icon != null) && (icon.length() > 0)) {
                            ImageTask imageTask = new ImageTask(icon, conditionsView, false);
                            imageTask.execute((Void[]) null);
                        }
                    }

                    prevDayOfMonth = currDayOfMonth;
                }
            }
        }
        if (rows.size() > 0) {
            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (TableRow row : rows) {
                        forecastTable.addView(row);
                    }
                }
            });
        } else {
            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView forecastErrorLabel = view.findViewById(R.id.forecast_error_label);
                    if ((dailyForecastFeature != null) && (dailyForecastFeature.getDetail() != null) && (dailyForecastFeature.getDetail().length() > 0)) {
                        forecastErrorLabel.setText(dailyForecastFeature.getDetail());
                    } else {
                        forecastErrorLabel.setText(R.string.forecast_error);
                    }
                    forecastErrorLabel.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void render(View view, LayoutInflater inflater, ForecastsDTO forecasts) {
        final TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        final List<TableRow> rows = new ArrayList<TableRow>();
        int totalWidth = forecastTable.getWidth();
        if ((forecasts != null) && (forecasts.getDescription() != null)) {
            TableRow descriptionRow = new TableRow(getContext());
            descriptionRow.setVisibility(View.VISIBLE);
            descriptionRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout description = (LinearLayout)inflater.inflate(R.layout.forecast_description, null);
            TextView descriptionText = description.findViewById(R.id.description);
            descriptionText.setText(forecasts.getDescription());
            description.setVisibility(View.VISIBLE);
            descriptionRow.addView(description);
            rows.add(descriptionRow);
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
                        rows.add(forecastDayRow);

                        TableRow spacerRow = new TableRow(getContext());
                        spacerRow.setVisibility(View.VISIBLE);
                        spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                        forecastDivider.setVisibility(View.VISIBLE);
                        spacerRow.addView(forecastDivider);
                        rows.add(spacerRow);
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
                    int temperatureViewWidth = convertDipToPixels(64);
                    int weatherSummaryViewWidth = (totalWidth - dayOfWeekViewPaddingLeft - dayOfWeekViewWidth - dayOfWeekViewPaddingRight - conditionsViewPaddingLeft - conditionsViewWidth - conditionsViewPaddingRight - weatherSummaryViewPaddingLeft - weatherSummaryViewPaddingRight - temperatureViewWidth);
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
                    rows.add(row);

                    TableRow spacerRow = new TableRow(getContext());
                    spacerRow.setVisibility(View.VISIBLE);
                    spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                    forecastDivider.setVisibility(View.VISIBLE);
                    spacerRow.addView(forecastDivider);
                    rows.add(spacerRow);

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
        if (rows.size() > 0) {
            MainActivity.getThisInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (TableRow row : rows) {
                        forecastTable.addView(row);
                    }
                }
            });
        } else {
            TextView forecastErrorLabel = view.findViewById(R.id.forecast_error_label);
            forecastErrorLabel.setText(R.string.forecast_error);
            forecastErrorLabel.setVisibility(View.VISIBLE);
        }
    }
}
