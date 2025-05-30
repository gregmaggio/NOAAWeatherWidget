package ca.datamagic.noaa.widget;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
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
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ca.datamagic.noaa.async.AccountingTask;
import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.async.RenderTask;
import ca.datamagic.noaa.current.CurrentForecasts;
import ca.datamagic.noaa.current.CurrentHazards;
import ca.datamagic.noaa.current.CurrentHourlyForecast;
import ca.datamagic.noaa.current.CurrentObservation;
import ca.datamagic.noaa.current.CurrentStation;
import ca.datamagic.noaa.current.CurrentTimeZone;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.FeatureDTO;
import ca.datamagic.noaa.dto.FeaturePropertiesDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.HeightCalculatorDTO;
import ca.datamagic.noaa.dto.HeightUnitsDTO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.PressureCalculatorDTO;
import ca.datamagic.noaa.dto.PressureUnitsDTO;
import ca.datamagic.noaa.dto.StationDTO;
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.dto.TemperatureUnitsDTO;
import ca.datamagic.noaa.dto.VisibilityCalculatorDTO;
import ca.datamagic.noaa.dto.VisibilityUnitsDTO;
import ca.datamagic.noaa.dto.WindSpeedCalculatorDTO;
import ca.datamagic.noaa.dto.WindSpeedUnitsDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.FeelsLikeTemperatureCalculator;
import ca.datamagic.noaa.util.WindDirectionConverter;

/**
 * Created by Greg on 1/10/2016.
 */
public class ObservationFragment extends Fragment implements Renderer, NonSwipeableFragment {
    private static Logger _logger = LogFactory.getLogger(ObservationFragment.class);
    private static char _degrees = (char)0x00B0;
    private static DecimalFormat _coordinatesFormat = new DecimalFormat("0.0");
    private static DecimalFormat _elevationFormat = new DecimalFormat("0.0");
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static DecimalFormat _humidityFormat = new DecimalFormat("0%");
    private static DecimalFormat _pressureFormat = new DecimalFormat("0.00");
    private static DecimalFormat _windFormat = new DecimalFormat("0");
    private static DecimalFormat _visibilityFormat = new DecimalFormat("0.00");

    public ForecastsDTO getForecasts() {
        return CurrentForecasts.getForecasts();
    }

    public ObservationDTO getObservation() {
        return CurrentObservation.getObervation();
    }

    public String getTimeZoneId() {
        return CurrentTimeZone.getTimeZone().getTimeZoneId();
    }

    public List<String> getHazards() {
        return CurrentHazards.getHazards();
    }

    public static ObservationFragment newInstance() {
        ObservationFragment fragment = new ObservationFragment();
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.observation_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.observation_main, container, false);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        ObservationDTO observation = getObservation();
        _logger.info("observation: " + observation);
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
            } else {
                RenderTask renderTask = new RenderTask(this);
                renderTask.execute();
            }
            (new AccountingTask("Observation", "Render")).execute();
        } catch (IllegalStateException ex) {
            _logger.warning("IllegalStateException: " + ex.getMessage());
            RenderTask renderTask = new RenderTask(this);
            renderTask.execute();
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
        TextView observationErrorLabel = view.findViewById(R.id.observation_error_label);
        TableLayout observationTable = (TableLayout)view.findViewById(R.id.observationTable);
        observationTable.removeAllViews();
        ForecastsDTO forecasts = getForecasts();
        ObservationDTO observation = getObservation();

        TableRow row = new TableRow(getContext());
        row.setVisibility(View.VISIBLE);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        if (observation != null) {
            observationErrorLabel.setVisibility(View.GONE);

            PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
            PreferencesDTO preferencesDTO = preferencesDAO.read();

            Date observationTimeUTC = observation.getObservationTimeUTC();
            String description = observation.getDescription();
            Double latitude = observation.getLatitude();
            Double longitude = observation.getLongitude();
            Double elevation = observation.getElevation();
            String elevationUnits = observation.getElevationUnits();
            if ((latitude == null) || (longitude == null)) {
                if (forecasts != null) {
                    latitude = forecasts.getLatitude();
                    longitude = forecasts.getLongitude();
                }
            }
            if ((elevation == null) || (elevationUnits == null) || (elevationUnits.length() < 1)) {
                if (forecasts != null) {
                    elevation = forecasts.getElevation();
                    elevationUnits = forecasts.getElevationUnits();
                }
            }

            Double temperature = observation.getTemperature();
            String temperatureUnits = observation.getTemperatureUnits();
            Double dewPoint = observation.getDewPoint();
            String dewPointUnits = observation.getDewPointUnits();
            Double windDirection = observation.getWindDirection();
            Double windSpeed = observation.getWindSpeed();
            String windSpeedUnits = observation.getWindSpeedUnits();
            Double windGust = observation.getWindGust();
            String windGustUnits = observation.getWindGustUnits();
            Double humidity = observation.getRelativeHumidity();
            Double pressure = observation.getPressure();
            String pressureUnits = observation.getPressureUnits();
            String conditionsIcon = observation.getIconUrl();
            Double visibility = observation.getVisibility();
            String visibilityUnits = observation.getVisibilityUnits();

            LinearLayout item = null;
            if (preferencesDTO.isTextOnly()) {
                item = (LinearLayout)inflater.inflate(R.layout.observation_item_text, null);
            } else {
                item = (LinearLayout)inflater.inflate(R.layout.observation_item, null);
            }
            item.setVisibility(View.VISIBLE);

            if (observationTimeUTC != null) {
                TextView observationTime = (TextView) item.findViewById(R.id.observationTime);
                observationTime.setText(getFormattedObservationTime(observationTimeUTC, preferencesDTO));
            } else {
                LinearLayout observationTimeLayout = item.findViewById(R.id.observationTimeLayout);
                observationTimeLayout.setVisibility(View.GONE);
            }
            if ((description != null) && (description.length() > 0)) {
                TextView locationView = (TextView) item.findViewById(R.id.location);
                locationView.setText(description);
            }

            if ((latitude != null) && (longitude != null)) {
                TextView coordinates = (TextView) item.findViewById(R.id.coordinates);
                String formattedCoordinates = getFormattedCoordinates(latitude, longitude);
                if (formattedCoordinates.length() > 0) {
                    coordinates.setVisibility(View.VISIBLE);
                    coordinates.setText(formattedCoordinates);
                } else {
                    coordinates.setVisibility(View.GONE);
                    coordinates.setText("");
                }
            }

            if ((elevation != null) && (elevationUnits != null) && (elevationUnits.length() > 0)) {
                TextView elevationView = (TextView) item.findViewById(R.id.elevation);
                String formattedElevation = getFormattedElevation(elevation, elevationUnits, preferencesDTO);
                if (formattedElevation.length() > 0) {
                    elevationView.setVisibility(View.VISIBLE);
                    elevationView.setText(formattedElevation);
                } else {
                    elevationView.setVisibility(View.GONE);
                    elevationView.setText("");
                }
            }

            if (!preferencesDTO.isTextOnly()) {
                if (conditionsIcon != null) {
                    ImageView currentConditionsImage = (ImageView) item.findViewById(R.id.currentConditionsImage);
                    ImageTask imageTask = new ImageTask(conditionsIcon, currentConditionsImage);
                    imageTask.execute();
                }
            }

            TextView temperatureText = (TextView) item.findViewById(R.id.temperature);
            String formattedTemperature = getFormattedTemperature(temperature, temperatureUnits, preferencesDTO);
            if (formattedTemperature.length() > 0) {
                temperatureText.setText(formattedTemperature);
            } else {
                temperatureText.setText(R.string.not_available);
            }

            TextView feelsLikeTemperatureText = (TextView) item.findViewById(R.id.feelsLikeTemperature);
            String formattedFeelsLikeTemperature = getFormattedFeelsLikeTemperature(temperature, temperatureUnits, humidity, windSpeed, preferencesDTO);
            if (formattedFeelsLikeTemperature.length() > 0) {
                feelsLikeTemperatureText.setVisibility(View.VISIBLE);
                feelsLikeTemperatureText.setText(MessageFormat.format(getContext().getResources().getString(R.string.feelsLikeTemperatureFormat), formattedFeelsLikeTemperature));
            } else {
                feelsLikeTemperatureText.setVisibility(View.GONE);
                feelsLikeTemperatureText.setText("");
            }

            TextView dewPointText = (TextView) item.findViewById(R.id.dewPoint);
            String formattedDewPointTemperature = getFormattedTemperature(dewPoint, dewPointUnits, preferencesDTO);
            if (formattedDewPointTemperature.length() > 0) {
                dewPointText.setText(formattedDewPointTemperature);
            } else {
                dewPointText.setText(R.string.not_available);
            }

            TextView humidityText = (TextView) item.findViewById(R.id.humidity);
            if (humidity != null) {
                humidityText.setText(_humidityFormat.format(humidity.doubleValue() / 100.0));
            } else {
                humidityText.setText(R.string.not_available);
            }

            TextView wind = (TextView)item.findViewById(R.id.wind);
            String formattedWind = getFormattedWind(windSpeed, windSpeedUnits, windDirection, preferencesDTO);
            if (formattedWind.length() > 0) {
                wind.setText(formattedWind);
            } else {
                wind.setText(R.string.not_available);
            }

            LinearLayout windGustView = (LinearLayout)item.findViewById(R.id.windGustView);
            TextView windGustText = (TextView)item.findViewById(R.id.windGust);
            String formattedWindGusts = getFormattedWindGusts(windGust, windGustUnits, preferencesDTO);
            if (formattedWindGusts.length() > 0) {
                windGustView.setVisibility(View.VISIBLE);
                windGustText.setText(formattedWindGusts);
            } else {
                windGustView.setVisibility(View.GONE);
                windGustText.setText("");
            }

            TextView visibilityText = (TextView)item.findViewById(R.id.visibility);
            String formattedVisibility = getFormattedVisibility(visibility, visibilityUnits, preferencesDTO);
            if (formattedVisibility.length() > 0) {
                visibilityText.setText(formattedVisibility);
            } else {
                visibilityText.setText(R.string.not_available);
            }

            TextView pressureText = (TextView) item.findViewById(R.id.pressure);
            String formattedPressure = getFormattedPressure(pressure, pressureUnits, elevation, elevationUnits, preferencesDTO);
            if (formattedPressure.length() > 0) {
                pressureText.setText(formattedPressure);
            } else {
                pressureText.setText(R.string.not_available);
            }

            LinearLayout sunriseLayout = (LinearLayout) item.findViewById(R.id.sunriseLayout);
            LinearLayout sunsetLayout = (LinearLayout) item.findViewById(R.id.sunsetLayout);
            TextView sunriseText = (TextView) item.findViewById(R.id.sunrise);
            TextView sunsetText = (TextView) item.findViewById(R.id.sunset);
            String sunrise = "";
            String sunset = "";
            String timeZoneId = getTimeZoneId();
            TimeZone tz = TimeZone.getTimeZone(timeZoneId);
            if ((latitude != null) && (longitude != null) && (timeZoneId != null)) {
                Calendar today = Calendar.getInstance();
                today.setTimeZone(tz);
                SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(new Location(latitude, longitude), timeZoneId);
                sunrise = calculator.getOfficialSunriseForDate(today);
                sunset = calculator.getOfficialSunsetForDate(today);
            }
            if ((sunrise != null) && (sunrise.length() > 0) && (sunset != null) && (sunset.length() > 0)) {
                sunriseText.setText(getFormattedSunriseSunsetTime(sunrise, tz, preferencesDTO));
                sunsetText.setText(getFormattedSunriseSunsetTime(sunset, tz, preferencesDTO));
            } else {
                sunriseLayout.setVisibility(View.GONE);
                sunsetLayout.setVisibility(View.GONE);
            }

            LinearLayout hazardsLayout = (LinearLayout)item.findViewById(R.id.hazardsLayout);
            List<String> hazardList = getHazards();
            if ((hazardList != null) && (hazardList.size() > 0)) {
                TextView hazards = (TextView)item.findViewById(R.id.hazards);
                CharSequence text = hazards.getText();
                SpannableString spannableString = new SpannableString( text );
                spannableString.setSpan(new URLSpan(""), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                hazards.setText(spannableString, TextView.BufferType.SPANNABLE);
                hazards.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HazardsDialog dialog = new HazardsDialog(getContext());
                        dialog.show();
                    }
                });
            } else {
                hazardsLayout.setVisibility(View.GONE);
            }
            row.addView(item);
        } else {
            // Display a meaningful error message
            observationErrorLabel.setVisibility(View.VISIBLE);
        }
        observationTable.addView(row);
    }

    private String getFormattedObservationTime(Date observationTimeUTC, PreferencesDTO preferences) {
        StringBuffer buffer = new StringBuffer();
        TimeZone timeZone = TimeZone.getTimeZone(CurrentTimeZone.getTimeZone().getTimeZoneId());
        String dateFormat = preferences.getDateFormat();
        if ((dateFormat != null) && (dateFormat.length() > 0)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setTimeZone(timeZone);
            if (buffer.length() > 0) {
                buffer.append(" ");
            }
            buffer.append(simpleDateFormat.format(observationTimeUTC));
        }
        String timeFormat = preferences.getTimeFormat();
        if ((timeFormat != null) && (timeFormat.length() > 0)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
            simpleDateFormat.setTimeZone(timeZone);
            if (buffer.length() > 0) {
                buffer.append(" ");
            }
            buffer.append(simpleDateFormat.format(observationTimeUTC));
        }
        return buffer.toString();
    }

    private String getFormattedCoordinates(Double latitude, Double longitude) {
        StringBuffer buffer = new StringBuffer();
        if ((latitude != null) && (longitude != null)) {
            buffer.append(getContext().getResources().getString(R.string.latitudeAbbreviation));
            buffer.append(": ");
            buffer.append(_coordinatesFormat.format(Math.abs(latitude.doubleValue())));
            buffer.append(_degrees);
            if (latitude.doubleValue() > 0) {
                buffer.append(getContext().getResources().getString(R.string.latitudeN));
            } else if (latitude.doubleValue() < 0){
                buffer.append(getContext().getResources().getString(R.string.latitudeS));
            }
            buffer.append(", ");
            buffer.append(getContext().getResources().getString(R.string.longitudeAbbreviation));
            buffer.append(": ");
            buffer.append(_coordinatesFormat.format(Math.abs(longitude.doubleValue())));
            buffer.append(_degrees);
            if (longitude.doubleValue() > 0) {
                buffer.append(getContext().getResources().getString(R.string.longitudeE));
            } else if (longitude.doubleValue() < 0) {
                buffer.append(getContext().getResources().getString(R.string.longitudeW));
            }
        }
        return buffer.toString();
    }

    private String getFormattedElevation(Double elevation, String elevationUnits, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((elevation != null) && (elevationUnits != null)) {
            elevation = HeightCalculatorDTO.compute(elevation, elevationUnits, preferencesDTO.getHeightUnits());
            if (elevation != null) {
                buffer.append(getContext().getResources().getString(R.string.elevationAbbreviation));
                buffer.append(": ");
                buffer.append(_elevationFormat.format(elevation.doubleValue()));
                if (preferencesDTO.getHeightUnits().compareToIgnoreCase(HeightUnitsDTO.Feet) == 0) {
                    buffer.append(" ");
                    buffer.append(getContext().getResources().getString(R.string.elevationFeet));
                } else if (preferencesDTO.getHeightUnits().compareToIgnoreCase(HeightUnitsDTO.Meters) == 0) {
                    buffer.append(" ");
                    buffer.append(getContext().getResources().getString(R.string.elevationMeters));
                }
            }
        }
        return buffer.toString();
    }

    private String getFormattedTemperature(Double temperature, String temperatureUnits, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((temperature != null) && (temperatureUnits != null)) {
            if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.FC) == 0) {
                Double fahrenheit = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Fahrenheit);
                Double celsius = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Celsius);
                if ((fahrenheit != null) && (celsius != null)) {
                    buffer.append(_temperatureFormat.format(fahrenheit));
                    buffer.append("/");
                    buffer.append(_temperatureFormat.format(celsius));
                    buffer.append(_degrees);
                }
            } else if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.CF) == 0) {
                Double fahrenheit = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Fahrenheit);
                Double celsius = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, TemperatureUnitsDTO.Celsius);
                if ((fahrenheit != null) && (celsius != null)) {
                    buffer.append(_temperatureFormat.format(celsius));
                    buffer.append("/");
                    buffer.append(_temperatureFormat.format(fahrenheit));
                    buffer.append(_degrees);
                }
            } else {
                temperature = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, preferencesDTO.getTemperatureUnits());
                if (temperature != null) {
                    buffer.append(_temperatureFormat.format(temperature));
                    buffer.append(_degrees);
                    if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0) {
                        buffer.append(getContext().getResources().getString(R.string.temperatureFahrenheit));
                    } else if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0) {
                        buffer.append(getContext().getResources().getString(R.string.temperatureCelsius));
                    }
                }
            }
        }
        return buffer.toString();
    }

    private String getFormattedFeelsLikeTemperature(Double temperature, String temperatureUnits, Double relativeHumidity, Double windSpeed, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        temperature = FeelsLikeTemperatureCalculator.computeFeelsLikeTemperature(temperature, relativeHumidity, windSpeed);
        if ((temperature != null) && (temperatureUnits != null)) {
            temperature = TemperatureCalculatorDTO.compute(temperature, temperatureUnits, preferencesDTO.getTemperatureUnits());
            if (temperature != null) {
                buffer.append(_temperatureFormat.format(temperature));
                buffer.append(_degrees);
                if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.Fahrenheit) == 0) {
                    buffer.append(getContext().getResources().getString(R.string.temperatureFahrenheit));
                } else if (preferencesDTO.getTemperatureUnits().compareToIgnoreCase(TemperatureUnitsDTO.Celsius) == 0) {
                    buffer.append(getContext().getResources().getString(R.string.temperatureCelsius));
                }
            }
        }
        return buffer.toString();
    }

    private String getFormattedWind(Double windSpeed, String windSpeedUnits, Double windDirection, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((windSpeed != null) && (windSpeedUnits != null)) {
            windSpeed = WindSpeedCalculatorDTO.compute(windSpeed, windSpeedUnits, preferencesDTO.getWindSpeedUnits());
            if (windSpeed != null) {
                buffer.append(_windFormat.format(windSpeed.doubleValue()));
                buffer.append(" ");
                if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    buffer.append(getContext().getResources().getString(R.string.knots));
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    buffer.append(getContext().getResources().getString(R.string.milesPerHour));
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    buffer.append(getContext().getResources().getString(R.string.kilometersPerHour));
                }
                if (windDirection != null) {
                    String compass = WindDirectionConverter.degreesToCompass(windDirection);
                    if (compass != null) {
                        buffer.append(" ");
                        buffer.append(compass);
                    }
                }
            }
        }
        return buffer.toString();
    }

    private String getFormattedWindGusts(Double windGust, String windGustUnits, PreferencesDTO preferencesDTO) {
        if ((windGust != null) && (windGustUnits != null)) {
            windGust = WindSpeedCalculatorDTO.compute(windGust, windGustUnits, preferencesDTO.getWindSpeedUnits());
            if (windGust != null) {
                String units = "";
                if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.Knots) == 0) {
                    units = getResources().getString(R.string.knots);
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.MilesPerHour) == 0) {
                    units = getResources().getString(R.string.milesPerHour);
                } else if (preferencesDTO.getWindSpeedUnits().compareToIgnoreCase(WindSpeedUnitsDTO.KilometersPerHour) == 0) {
                    units = getResources().getString(R.string.kilometersPerHour);
                }
                String windGustFormat = getResources().getString(R.string.wind_gust);
                return MessageFormat.format(windGustFormat, _windFormat.format(windGust.doubleValue()), units);
            }
        }
        return "";
    }

    private String getFormattedVisibility(Double visibility, String visibilityUnits, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((visibility != null) && (visibilityUnits != null)) {
            visibility = VisibilityCalculatorDTO.compute(visibility, visibilityUnits, preferencesDTO.getVisibilityUnits());
            if (visibility != null) {
                buffer.append(_visibilityFormat.format(visibility.doubleValue()));
                if (preferencesDTO.getVisibilityUnits().compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    buffer.append(" ");
                    buffer.append(getResources().getString(R.string.statuteMiles));
                } else if (preferencesDTO.getVisibilityUnits().compareToIgnoreCase(VisibilityUnitsDTO.StatuteMiles) == 0) {
                    buffer.append(" ");
                    buffer.append(getResources().getString(R.string.miles));
                } else if (preferencesDTO.getVisibilityUnits().compareToIgnoreCase(VisibilityUnitsDTO.Kilometers) == 0) {
                    buffer.append(" ");
                    buffer.append(getResources().getString(R.string.kilometers));
                }
            }
        }
        return buffer.toString();
    }

    private String getFormattedPressure(Double pressure, String pressureUnits, Double elevation, String elevationUnits, PreferencesDTO preferencesDTO) {
        StringBuffer buffer = new StringBuffer();
        if ((pressure != null) && (pressureUnits != null) && (elevation != null) && (elevationUnits != null)) {
            pressure = PressureCalculatorDTO.compute(pressure, pressureUnits, preferencesDTO.getPressureUnits(), elevation, elevationUnits);
            if (pressure != null) {
                buffer.append(_pressureFormat.format(pressure.doubleValue()));
                if (preferencesDTO.getPressureUnits().compareToIgnoreCase(PressureUnitsDTO.InchesOfMercury) == 0) {
                    buffer.append(" ");
                    buffer.append(getResources().getString(R.string.inchesOfMercury));
                } else if (preferencesDTO.getPressureUnits().compareToIgnoreCase(PressureUnitsDTO.KiloPascals) == 0) {
                    buffer.append(" ");
                    buffer.append(getResources().getString(R.string.kiloPascals));
                } else if (preferencesDTO.getPressureUnits().compareToIgnoreCase(PressureUnitsDTO.StationPressure) == 0) {
                    buffer.append(" ");
                    buffer.append(getResources().getString(R.string.inchesOfMercury));
                }
            }
        }
        return buffer.toString();
    }

    private String getFormattedSunriseSunsetTime(String timeString, TimeZone timeZone, PreferencesDTO preferences) {
        int index = timeString.indexOf(':');
        int hours = Integer.parseInt(timeString.substring(0, index));
        int minutes = Integer.parseInt(timeString.substring(index + 1));
        Calendar today = Calendar.getInstance();
        today.setTimeZone(timeZone);
        today.set(Calendar.HOUR_OF_DAY, hours);
        today.set(Calendar.MINUTE, minutes);
        StringBuffer buffer = new StringBuffer();
        String timeFormat = preferences.getTimeFormat();
        if ((timeFormat != null) && (timeFormat.length() > 0)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
            simpleDateFormat.setTimeZone(timeZone);
            if (buffer.length() > 0) {
                buffer.append(" ");
            }
            buffer.append(simpleDateFormat.format(today.getTime()));
        }
        return buffer.toString();
    }
}
