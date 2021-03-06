package ca.datamagic.noaa.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.datamagic.noaa.dto.ConditionsIconDTO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.DirectionDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.HumidityDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.PressureDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;
import ca.datamagic.noaa.dto.ValueDTO;
import ca.datamagic.noaa.dto.VisibilityDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.noaa.dto.WindSpeedDTO;

/**
 * Created by Greg on 4/14/2018.
 */

public class ObservationDAO {
    private static final Pattern _observationTimePattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})(\\x2D|\\x2B)(\\d{2}):(\\d{2})", Pattern.CASE_INSENSITIVE);

    public static ObservationDTO getObservation(DWMLDTO dwml) {
        ObservationDTO observation = new ObservationDTO();
        if (dwml != null) {
            DataDTO data = dwml.getObservation();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if ((timeLayouts != null) && (timeLayouts.size() > 0)) {
                    TimeLayoutDTO timeLayout = timeLayouts.get(0);
                    List<ValidTimeDTO> startTimes = timeLayout.getStartTimes();
                    if ((startTimes != null) && (startTimes.size() > 0)) {
                        ValidTimeDTO startTime = startTimes.get(0);
                        String timeStamp = startTime.getTimeStamp();
                        if (timeStamp != null) {
                            Matcher observationTimeMatcher = _observationTimePattern.matcher(timeStamp);
                            if (observationTimeMatcher.matches()) {
                                int year = Integer.parseInt(observationTimeMatcher.group(1));
                                int month = Integer.parseInt(observationTimeMatcher.group(2));
                                int day = Integer.parseInt(observationTimeMatcher.group(3));
                                int hour = Integer.parseInt(observationTimeMatcher.group(4));
                                int minute = Integer.parseInt(observationTimeMatcher.group(5));
                                int second = Integer.parseInt(observationTimeMatcher.group(6));
                                String sign = observationTimeMatcher.group(7);
                                int hourAdjust = Integer.parseInt(observationTimeMatcher.group(8));
                                if (sign.compareToIgnoreCase("+") == 0) {
                                    hourAdjust *= -1;
                                }
                                int minuteAdjust = Integer.parseInt(observationTimeMatcher.group(9));
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month - 1);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, second);
                                calendar.add(Calendar.HOUR_OF_DAY, hourAdjust);
                                calendar.add(Calendar.MINUTE, minuteAdjust);
                                Date dateUTC = calendar.getTime();
                                observation.setObservationTimeUTC(dateUTC);
                            }
                        }
                    }
                }
                LocationDTO location = data.getLocation();
                if (location != null) {
                    observation.setDescription(location.getDescription());
                    PointDTO point = location.getPoint();
                    if (point != null) {
                        Double latitude = point.getLatitude();
                        Double longitude = point.getLongitude();
                        if ((latitude != null) && (!Double.isNaN(latitude.doubleValue())) && (longitude != null) && (!Double.isNaN(longitude.doubleValue()))) {
                            observation.setLatitude(latitude);
                            observation.setLongitude(longitude);
                        }
                    }
                    HeightDTO height = location.getHeight();
                    if (height != null) {
                        Double elevation = height.getValue();
                        String elevationUnits = height.getHeightUnits();
                        if ((elevation != null) && (!Double.isNaN(elevation.doubleValue())) && (elevationUnits != null) && (elevationUnits.length() > 0)) {
                            observation.setElevation(elevation);
                            observation.setElevationUnits(elevationUnits);
                        }
                    }
                }
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            String type = temperature.getType();
                            String units = temperature.getUnits();
                            List<Double> values = temperature.getValues();
                            if ((type != null) && (values != null) && (values.size() > 0)) {
                                Double value = values.get(0);
                                if ((value != null) && (!Double.isNaN(value.doubleValue()))) {
                                    if (type.compareToIgnoreCase("apparent") == 0) {
                                        observation.setTemperature(value);
                                        observation.setTemperatureUnits(units);
                                    } else if (type.compareToIgnoreCase("dew point") == 0) {
                                        observation.setDewPoint(value);
                                        observation.setDewPointUnits(units);
                                    }
                                }
                            }
                        }
                    }
                    HumidityDTO humidity = parameters.getHumidity();
                    if (humidity != null) {
                        Double value = humidity.getValue();
                        if ((value != null) && (!Double.isNaN(value.doubleValue()))) {
                            observation.setRelativeHumidity(value);
                        }
                    }
                    WeatherDTO weather = parameters.getWeather();
                    if (weather != null) {
                        List<WeatherConditionsDTO> weatherConditions = weather.getWeatherConditions();
                        if (weatherConditions != null) {
                            for (int ii = 0; ii < weatherConditions.size(); ii++) {
                                String weatherSummary = weatherConditions.get(ii).getWeatherSummary();
                                if ((weatherSummary != null) && (weatherSummary.length() > 0)) {
                                    observation.setSummary(weatherConditions.get(ii).getWeatherSummary());
                                } else {
                                    List<ValueDTO> values = weatherConditions.get(ii).getValues();
                                    if (values != null) {
                                        for (int jj = 0; jj < values.size(); jj++) {
                                            VisibilityDTO visibility = values.get(jj).getVisibility();
                                            if (visibility != null) {
                                                Double value = visibility.getValue();
                                                String units = visibility.getUnits();
                                                if ((value != null) && (!Double.isNaN(value.doubleValue())) && (units != null) && (units.length() > 0)) {
                                                    observation.setVisibility(value);
                                                    observation.setVisibilityUnits(units);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ConditionsIconDTO conditionsIcon = parameters.getConditionsIcon();
                    if (conditionsIcon != null) {
                        List<String> iconLink = conditionsIcon.getIconLink();
                        if ((iconLink != null) && (iconLink.size() > 0)) {
                            observation.setIconUrl(iconLink.get(0).replace("http://", "https://"));
                        }
                    }
                    DirectionDTO direction = parameters.getDirection();
                    if (direction != null) {
                        Double value = direction.getValue();
                        String units = direction.getUnits();
                        if ((value != null) && (!Double.isNaN(value.doubleValue())) && (units != null) && (units.length() > 0)) {
                            observation.setWindDirection(value);
                            observation.setWindDirectionUnits(units);
                        }
                    }
                    List<WindSpeedDTO> windSpeeds = parameters.getWindSpeeds();
                    if (windSpeeds != null) {
                        for (int ii = 0; ii < windSpeeds.size(); ii++) {
                            WindSpeedDTO windSpeed = windSpeeds.get(ii);
                            String type = windSpeed.getType();
                            Double value = windSpeed.getValue();
                            String units = windSpeed.getUnits();
                            if ((type != null) && (type.length() > 0) && (value != null) && (!Double.isNaN(value.doubleValue())) && (units != null) && (units.length() > 0)) {
                                if (type.compareToIgnoreCase("sustained") == 0) {
                                    observation.setWindSpeed(value);
                                    observation.setWindSpeedUnits(units);
                                } else if (type.compareToIgnoreCase("gust") == 0) {
                                    observation.setWindGust(value);
                                    observation.setWindGustUnits(units);
                                }
                            }
                        }
                    }
                    PressureDTO pressure = parameters.getPressure();
                    if (pressure != null) {
                        Double value = pressure.getValue();
                        String units = pressure.getUnits();
                        if ((value != null) && (!Double.isNaN(value.doubleValue())) && (units != null) && (units.length() > 0)) {
                            observation.setPressure(value);
                            observation.setPressureUnits(units);
                        }
                    }
                }
            }
        }
        return observation;
    }
}
