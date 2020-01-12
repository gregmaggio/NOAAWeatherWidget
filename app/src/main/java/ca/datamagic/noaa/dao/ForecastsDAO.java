package ca.datamagic.noaa.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ca.datamagic.noaa.dto.ConditionsIconDTO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.ForecastDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.ProbabilityOfPrecipitationDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.TimeLayoutDTO;
import ca.datamagic.noaa.dto.ValidTimeDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.noaa.dto.WordedForecastDTO;

/**
 * Created by Greg on 4/8/2018.
 */

public class ForecastsDAO {
    private static SimpleDateFormat _dayMonthFormat = new SimpleDateFormat("MMM d");

    public static ForecastsDTO getForecasts(DWMLDTO dwml) {
        List<ForecastDTO> items = new ArrayList<ForecastDTO>();
        TimeLayoutDTO detailedTimeLayout = getDetailedTimeLayout(dwml);
        TemperatureDTO maxTemperature = getMaximumTemperature(dwml);
        TemperatureDTO minTemperature = getMinimumTemperature(dwml);
        TimeLayoutDTO maxTemperatureTimeLayout = null;
        TimeLayoutDTO minTemperatureTimeLayout = null;
        if (maxTemperature != null) {
            maxTemperatureTimeLayout = getTimeLayout(dwml, maxTemperature.getTimeLayout());
        }
        if (minTemperature != null) {
            minTemperatureTimeLayout = getTimeLayout(dwml, minTemperature.getTimeLayout());
        }
        if (detailedTimeLayout != null) {
            List<ValidTimeDTO> forecastDays = detailedTimeLayout.getStartTimes();
            if (forecastDays != null) {
                for (int ii = 0; ii < forecastDays.size(); ii++) {
                    ValidTimeDTO startTime = forecastDays.get(ii);
                    String timeStamp = startTime.getTimeStamp();
                    String currentPeriodName = startTime.getPeriodName();
                    Double temperature = null;
                    String temperatureUnits = null;
                    if ((temperature == null) && (maxTemperatureTimeLayout != null)) {
                        String units = maxTemperature.getUnits();
                        List<Double> values = maxTemperature.getValues();
                        List<ValidTimeDTO> startTimes = maxTemperatureTimeLayout.getStartTimes();
                        if ((startTimes != null) && (values != null)) {
                            for (int jj = 0; jj < startTimes.size(); jj++) {
                                String periodName = startTimes.get(jj).getPeriodName();
                                if ((currentPeriodName != null) && (periodName != null)) {
                                    if (currentPeriodName.compareToIgnoreCase(periodName) == 0) {
                                        if (jj < values.size()) {
                                            temperature = values.get(jj);
                                            temperatureUnits = units;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if ((temperature == null) && (minTemperatureTimeLayout != null)) {
                        String units = minTemperature.getUnits();
                        List<Double> values = minTemperature.getValues();
                        List<ValidTimeDTO> startTimes = minTemperatureTimeLayout.getStartTimes();
                        if ((startTimes != null) && (values != null)) {
                            for (int jj = 0; jj < startTimes.size(); jj++) {
                                String periodName = startTimes.get(jj).getPeriodName();
                                if ((currentPeriodName != null) && (periodName != null)) {
                                    if (currentPeriodName.compareToIgnoreCase(periodName) == 0) {
                                        if (jj < values.size()) {
                                            temperature = values.get(jj);
                                            temperatureUnits = units;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Double pop = getPOP(ii, dwml);
                    String weatherSummary = getWeatherSummary(ii, dwml);
                    String imageUrl = getImageUrl(ii, dwml);
                    String wordedForecast = getWordedForecast(ii, dwml);

                    ForecastDTO forecast = new ForecastDTO();
                    forecast.setTimeStamp(timeStamp);
                    forecast.setPeriodName(currentPeriodName);
                    forecast.setTemperature(temperature);
                    forecast.setTemperatureUnits(temperatureUnits);
                    forecast.setPOP(pop);
                    forecast.setSummary(weatherSummary);
                    forecast.setImageUrl(imageUrl);
                    forecast.setWordedForecast(wordedForecast);
                    items.add(forecast);
                }
            }
        }
        ForecastsDTO forecasts = new ForecastsDTO();
        DataDTO data = dwml.getForecast();
        if (data != null) {
            LocationDTO location = data.getLocation();
            if (location != null) {
                forecasts.setDescription(location.getDescription());
                forecasts.setCity(location.getCity());
                forecasts.setState(location.getState());
                PointDTO point = location.getPoint();
                if (point != null) {
                    Double latitude = point.getLatitude();
                    Double longitude = point.getLongitude();
                    if ((latitude != null) && (!Double.isNaN(latitude.doubleValue())) && (longitude != null) && (!Double.isNaN(longitude.doubleValue()))) {
                        forecasts.setLatitude(latitude);
                        forecasts.setLongitude(longitude);
                    }
                }
                HeightDTO height = location.getHeight();
                if (height != null) {
                    Double elevation = height.getValue();
                    String elevationUnits = height.getHeightUnits();
                    if ((elevation != null) && (!Double.isNaN(elevation.doubleValue())) && (elevationUnits != null) && (elevationUnits.length() > 0)) {
                        forecasts.setElevation(elevation);
                        forecasts.setElevationUnits(elevationUnits);
                    }
                }
            }
        }
        forecasts.setItems(items);
        return forecasts;
    }

    private static TimeLayoutDTO getTimeLayout(DWMLDTO dwml, String layoutKey) {
        if ((dwml != null) && (layoutKey != null) && (layoutKey.length() > 0)) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if (timeLayouts != null) {
                    for (int ii = 0; ii < timeLayouts.size(); ii++) {
                        TimeLayoutDTO timeLayout = timeLayouts.get(ii);
                        if (timeLayout.getLayoutKey() != null) {
                            if (timeLayout.getLayoutKey().compareToIgnoreCase(layoutKey) == 0) {
                                return timeLayout;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static TimeLayoutDTO getDetailedTimeLayout(DWMLDTO dwml) {
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if (timeLayouts != null) {
                    for (int ii = 0; ii < timeLayouts.size(); ii++) {
                        TimeLayoutDTO timeLayout = timeLayouts.get(ii);
                        if (timeLayout.getStartTimes() != null) {
                            if (timeLayout.getStartTimes().size() > 12) {
                                return timeLayout;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static TimeLayoutDTO getTimeLayout1(DWMLDTO dwml) {
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if ((timeLayouts != null) && (timeLayouts.size() > 1)) {
                    return timeLayouts.get(1);
                }
            }
        }
        return null;
    }

    private static TimeLayoutDTO getTimeLayout2(DWMLDTO dwml) {
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                List<TimeLayoutDTO> timeLayouts = data.getTimeLayouts();
                if ((timeLayouts != null) && (timeLayouts.size() > 2)) {
                    return timeLayouts.get(2);
                }
            }
        }
        return null;
    }

    private static TemperatureDTO getMaximumTemperature(DWMLDTO dwml) {
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            if (temperature.getType() != null) {
                                if (temperature.getType().compareToIgnoreCase("maximum") == 0) {
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

    private static TemperatureDTO getMinimumTemperature(DWMLDTO dwml) {
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            if (temperature.getType() != null) {
                                if (temperature.getType().compareToIgnoreCase("minimum") == 0) {
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

    private static TemperatureDTO getTemperature(String timeLayout, DWMLDTO dwml) {
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
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

    private static Double getPOP(int index, DWMLDTO dwml) {
        Double pop = null;
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    ProbabilityOfPrecipitationDTO probabilityOfPrecipitation = parameters.getProbabilityOfPrecipitation();
                    if (probabilityOfPrecipitation != null) {
                        List<Double> values = probabilityOfPrecipitation.getValues();
                        if ((values != null) && (index < values.size())) {
                            pop = values.get(index);
                        }
                    }
                }
            }
        }
        return pop;
    }

    private static String getWeatherSummary(int index, DWMLDTO dwml) {
        String weatherSummary = "";
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    WeatherDTO weather = parameters.getWeather();
                    if (weather != null) {
                        List<WeatherConditionsDTO> weatherConditionsList = weather.getWeatherConditions();
                        if ((weatherConditionsList != null) && (index < weatherConditionsList.size())) {
                            WeatherConditionsDTO weatherConditions = weatherConditionsList.get(index);
                            if (weatherConditions != null) {
                                weatherSummary = weatherConditions.getWeatherSummary();
                            }
                        }
                    }
                }
            }
        }
        return weatherSummary;
    }

    private static String getImageUrl(int index, DWMLDTO dwml) {
        String imageUrl = "";
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    ConditionsIconDTO conditionsIcon = parameters.getConditionsIcon();
                    if (conditionsIcon != null) {
                        List<String> iconLink = conditionsIcon.getIconLink();
                        if ((iconLink != null) && (index < iconLink.size())) {
                            imageUrl = iconLink.get(index);
                            if ((imageUrl != null) && (imageUrl.length() > 0)) {
                                imageUrl = imageUrl.replace("http://", "https://");
                            }
                        }
                    }
                }
            }
        }
        return imageUrl;
    }

    private static String getWordedForecast(int index, DWMLDTO dwml) {
        String wordedForecast = "";
        if (dwml != null) {
            DataDTO data = dwml.getForecast();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    WordedForecastDTO wordedForecastDTO = parameters.getWordedForecast();
                    if (wordedForecastDTO != null) {
                        List<String> text = wordedForecastDTO.getText();
                        if ((text != null) && (index < text.size())) {
                            wordedForecast = text.get(index);
                        }
                    }
                }
            }
        }
        return wordedForecast;
    }
}
