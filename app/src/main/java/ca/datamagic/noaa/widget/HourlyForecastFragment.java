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

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.dto.GeometryDTO;
import ca.datamagic.noaa.dto.PeriodDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.TimeStampDTO;
import ca.datamagic.noaa.logging.LogFactory;

public class HourlyForecastFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(HourlyForecastFragment.class);
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;

    private int convertDipToPixels(float dips) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dips * density + 0.5f);
    }

    public FeatureDTO getHourlyForecastFeature() {
        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            return mainActivity.getHourlyForecastFeature();
        }
        return null;
    }

    public String getTimeZoneId() {
        String timeZoneId = null;
        MainActivity mainActivity = MainActivity.getThisInstance();
        if (mainActivity != null) {
            if (mainActivity.getStation() != null) {
                timeZoneId = mainActivity.getStation().getTimeZoneId();
            }
            if ((timeZoneId == null) || (timeZoneId.length() < 1)) {
                if (mainActivity.getHourlyForecastFeature() != null) {
                    FeaturePropertiesDTO featureProperties = mainActivity.getHourlyForecastFeature().getProperties();
                    if (featureProperties != null) {
                        timeZoneId = featureProperties.getTimeZone();
                    }
                }
            }
        }
        return timeZoneId;
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
        try {
            if (!MainActivity.getThisInstance().isFragmentActive(this)) {
                return;
            }
            View view = getView();
            LayoutInflater inflater = getLayoutInflater();
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

    private void render(View view, LayoutInflater inflater) {
        TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        forecastTable.removeAllViews();
        int totalWidth = forecastTable.getWidth();
        if (forecastTable != null) {
            FeatureDTO hourlyForecastFeature = getHourlyForecastFeature();
            GeometryDTO geometry = null;
            FeaturePropertiesDTO properties = null;
            if (hourlyForecastFeature != null) {
                properties = hourlyForecastFeature.getProperties();
                geometry = hourlyForecastFeature.getGeometry();
            }
            Double latitude = null;
            Double longitude = null;
            if (geometry != null) {
                latitude = geometry.getLatitude();
                longitude = geometry.getLongitude();
            }
            String timeZoneId = getTimeZoneId();
            SunriseSunsetCalculator calculator = null;
            if ((properties != null) && (timeZoneId != null)) {
                TimeStampDTO timeStampDTO = new TimeStampDTO(timeZoneId);
                TimeZone tz = TimeZone.getTimeZone(timeZoneId);
                if ((latitude != null) && (longitude != null)) {
                    calculator = new SunriseSunsetCalculator(new Location(latitude, longitude), timeZoneId);
                }
                PeriodDTO[] periods = properties.getPeriods();
                if (periods != null) {
                    PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
                    PreferencesDTO preferencesDTO = preferencesDAO.read();
                    String currentDayOfWeek = null;
                    for (int ii = 0; ii < periods.length; ii++) {
                        Calendar calendar = null;
                        Calendar sunrise = null;
                        Calendar sunset = null;
                        String dayOfWeek = null;
                        String hourOfDay = null;
                        if (periods[ii].getStartTime() != null) {
                            timeStampDTO.setTimeStamp(periods[ii].getStartTime());
                            dayOfWeek = timeStampDTO.getDayOfWeek();
                            hourOfDay = timeStampDTO.get12HourOfDay();
                            calendar = (Calendar)timeStampDTO.getCalendar().clone();
                            calendar.setTimeZone(tz);
                            _logger.info("Current Time: " + calendar.toString());
                            if (calculator != null) {
                                sunrise = calculator.getOfficialSunriseCalendarForDate(calendar);
                                _logger.info("sunrise: " + sunrise.toString());
                                sunset = calculator.getOfficialSunsetCalendarForDate(calendar);
                                _logger.info("sunset: " + sunset.toString());
                            }
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

                        LinearLayout item = null;
                        if (preferencesDTO.isTextOnly()) {
                            item = (LinearLayout) inflater.inflate(R.layout.hourly_forecast_item_text, null);
                        } else {
                            item = (LinearLayout) inflater.inflate(R.layout.hourly_forecast_item, null);
                        }
                        item.setVisibility(View.VISIBLE);

                        TextView hourOfDayView = (TextView) item.findViewById(R.id.hourOfDay);
                        int hourOfDayViewWidth = convertDipToPixels(60);
                        int hourOfDayViewPaddingLeft = hourOfDayView.getPaddingLeft();
                        int hourOfDayViewPaddingRight = hourOfDayView.getPaddingRight();
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
                        int weatherSummaryViewWidth = (totalWidth - hourOfDayViewPaddingLeft - hourOfDayViewWidth - hourOfDayViewPaddingRight - conditionsViewPaddingLeft - conditionsViewWidth - conditionsViewPaddingRight - weatherSummaryViewPaddingLeft - weatherSummaryViewPaddingRight - temperatureViewPaddingLeft - temperatureViewWidth - temperatureViewPaddingRight);
                        weatherSummaryView.setLayoutParams(new LinearLayout.LayoutParams(weatherSummaryViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
                        if (hourOfDay != null) {
                            hourOfDayView.setText(hourOfDay);
                        }
                        if (periods[ii].getShortForecast() != null) {
                            weatherSummaryView.setText(periods[ii].getShortForecast());
                        }
                        if (periods[ii].getTemperature() != null) {
                            if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0) {
                                Double fahrenheit = TemperatureCalculatorDTO.compute(periods[ii].getTemperature(), periods[ii].getTemperatureUnit(), TemperatureUnitsDTO.Fahrenheit);
                                Double celsius = TemperatureCalculatorDTO.compute(periods[ii].getTemperature(), periods[ii].getTemperatureUnit(), TemperatureUnitsDTO.Celsius);
                                if ((fahrenheit != null) && (celsius != null)) {
                                    temperatureView.setText(_temperatureFormat.format(fahrenheit.doubleValue()) + "/" + _temperatureFormat.format(celsius.doubleValue()) + _degrees);
                                }
                            } else if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0) {
                                Double fahrenheit = TemperatureCalculatorDTO.compute(periods[ii].getTemperature(), periods[ii].getTemperatureUnit(), TemperatureUnitsDTO.Fahrenheit);
                                Double celsius = TemperatureCalculatorDTO.compute(periods[ii].getTemperature(), periods[ii].getTemperatureUnit(), TemperatureUnitsDTO.Celsius);
                                if ((fahrenheit != null) && (celsius != null)) {
                                    temperatureView.setText(_temperatureFormat.format(celsius.doubleValue()) + "/" + _temperatureFormat.format(fahrenheit.doubleValue()) + _degrees);
                                }
                            } else {
                                Double temperature = TemperatureCalculatorDTO.compute(periods[ii].getTemperature(), periods[ii].getTemperatureUnit(), preferencesDTO.getTemperatureUnits());
                                if (temperature != null) {
                                    temperatureView.setText(_temperatureFormat.format(temperature.doubleValue()) + _degrees);
                                }
                            }
                        }

                        row.addView(item);
                        forecastTable.addView(row);

                        if (!preferencesDTO.isTextOnly()) {
                            if ((periods[ii].getIcon() != null) && (periods[ii].getIcon().length() > 0)) {
                                String iconUrl = periods[ii].getIcon();
                                if ((calendar != null) && (sunrise != null) && (sunset != null)) {
                                    boolean afterSunrise = calendar.after(sunrise);
                                    _logger.info("afterSunrise: " + afterSunrise);
                                    boolean beforeSunset = calendar.before(sunset);
                                    _logger.info("beforeSunset: " + beforeSunset);
                                    boolean dayTime = afterSunrise && beforeSunset;
                                    _logger.info("dayTime: " + dayTime);
                                    if (dayTime) {
                                        iconUrl = iconUrl.replace("night", "day");
                                    } else {
                                        iconUrl = iconUrl.replace("day", "night");
                                    }
                                }
                                ImageTask imageTask = new ImageTask(iconUrl, conditionsView, false);
                                imageTask.execute((Void[]) null);
                            }
                        }
                        currentDayOfWeek = dayOfWeek;
                    }
                }
            }
            (new AccountingTask("Hourly", "Render")).execute((Void[])null);
        }
    }
}
