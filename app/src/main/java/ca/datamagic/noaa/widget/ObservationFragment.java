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
import java.util.List;

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dto.ConditionsIconDTO;
import ca.datamagic.noaa.dto.DWMLDTO;
import ca.datamagic.noaa.dto.DataDTO;
import ca.datamagic.noaa.dto.DirectionDTO;
import ca.datamagic.noaa.dto.HeightDTO;
import ca.datamagic.noaa.dto.HumidityDTO;
import ca.datamagic.noaa.dto.LocationDTO;
import ca.datamagic.noaa.dto.ParametersDTO;
import ca.datamagic.noaa.dto.PointDTO;
import ca.datamagic.noaa.dto.PressureDTO;
import ca.datamagic.noaa.dto.TemperatureDTO;
import ca.datamagic.noaa.dto.ValueDTO;
import ca.datamagic.noaa.dto.VisibilityDTO;
import ca.datamagic.noaa.dto.WeatherConditionsDTO;
import ca.datamagic.noaa.dto.WeatherDTO;
import ca.datamagic.noaa.dto.WindSpeedDTO;
import ca.datamagic.noaa.util.FeelsLikeTemperatureCalculator;
import ca.datamagic.noaa.util.WindDirectionConverter;

/**
 * Created by Greg on 1/10/2016.
 */
public class ObservationFragment extends Fragment implements Renderer {
    private static DecimalFormat _coordinatesFormat = new DecimalFormat("0.0");
    private static DecimalFormat _elevationFormat = new DecimalFormat("0.0");
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static DecimalFormat _humidityFormat = new DecimalFormat("0");
    private static DecimalFormat _pressureFormat = new DecimalFormat("0");
    private static DecimalFormat _windFormat = new DecimalFormat("0");
    private static DecimalFormat _visibilityFormat = new DecimalFormat("0.00");
    private TableLayout _observationTable = null;
    private LayoutInflater _inflater = null;
    private DWMLDTO _dwml = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.observation_main, container, false);
        _observationTable = (TableLayout) view.findViewById(R.id.observationTable);
        _inflater = inflater;
        return view;
    }

    public DWMLDTO getDWML(){
        return _dwml;
    }

    public void setDWML(DWMLDTO newVal){
        _dwml = newVal;
    }

    private String getAreaDescription() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                LocationDTO location = data.getLocation();
                if (location != null) {
                    return  location.getAreaDescription();
                }
            }
        }
        return null;
    }

    private Double getLatitude() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                LocationDTO location = data.getLocation();
                if (location != null) {
                    PointDTO point = location.getPoint();
                    if (point != null) {
                        return point.getLatitude();
                    }
                }
            }
        }
        return null;
    }

    private Double getLongitude() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                LocationDTO location = data.getLocation();
                if (location != null) {
                    PointDTO point = location.getPoint();
                    if (point != null) {
                        return point.getLongitude();
                    }
                }
            }
        }
        return null;
    }

    private Double getElevation() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                LocationDTO location = data.getLocation();
                if (location != null) {
                    HeightDTO height = location.getHeight();
                    if (height != null) {
                        return height.getValue();
                    }
                }
            }
        }
        return null;
    }

    private String getElevationUnits() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                LocationDTO location = data.getLocation();
                if (location != null) {
                    HeightDTO height = location.getHeight();
                    if (height != null) {
                        return height.getHeightUnits();
                    }
                }
            }
        }
        return null;
    }

    private Double getApparentTemperature() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            String type = temperature.getType();
                            if (type.compareToIgnoreCase("apparent") == 0) {
                                if ((temperature.getValues() != null) && (temperature.getValues().size() > 0)) {
                                    return temperature.getValues().get(0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Double getDewPointTemperature() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<TemperatureDTO> temperatures = parameters.getTemperatures();
                    if (temperatures != null) {
                        for (int ii = 0; ii < temperatures.size(); ii++) {
                            TemperatureDTO temperature = temperatures.get(ii);
                            String type = temperature.getType();
                            if (type.compareToIgnoreCase("dew point") == 0) {
                                if ((temperature.getValues() != null) && (temperature.getValues().size() > 0)) {
                                    return temperature.getValues().get(0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Double getWindDirection() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    DirectionDTO direction = parameters.getDirection();
                    if (direction != null) {
                        return direction.getValue();
                    }
                }
            }
        }
        return null;
    }

    private Double getWindSpeed() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<WindSpeedDTO> windSpeeds = parameters.getWindSpeeds();
                    if (windSpeeds != null) {
                        for (int ii = 0; ii < windSpeeds.size(); ii++) {
                            WindSpeedDTO windSpeed = windSpeeds.get(ii);
                            String type = windSpeed.getType();
                            if (type.compareToIgnoreCase("sustained") == 0) {
                                return windSpeed.getValue();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Double getWindGust() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    List<WindSpeedDTO> windSpeeds = parameters.getWindSpeeds();
                    if (windSpeeds != null) {
                        for (int ii = 0; ii < windSpeeds.size(); ii++) {
                            WindSpeedDTO windSpeed = windSpeeds.get(ii);
                            String type = windSpeed.getType();
                            if (type.compareToIgnoreCase("gust") == 0) {
                                return windSpeed.getValue();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Double getHumidity() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    HumidityDTO humidity = parameters.getHumidity();
                    if (humidity != null) {
                        return humidity.getValue();
                    }
                }
            }
        }
        return null;
    }

    private Double getPressure() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    PressureDTO pressure = parameters.getPressure();
                    if (pressure != null) {
                        return pressure.getValue();
                    }
                }
            }
        }
        return null;
    }

    private String getConditionsIcon() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    ConditionsIconDTO conditionsIcon = parameters.getConditionsIcon();
                    if (conditionsIcon != null) {
                        List<String> iconLink = conditionsIcon.getIconLink();
                        if ((iconLink != null) && (iconLink.size() > 0)) {
                            return iconLink.get(0);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Double getVisibility() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    WeatherDTO weather = parameters.getWeather();
                    if (weather != null) {
                        List<WeatherConditionsDTO> weatherConditionsList = weather.getWeatherConditions();
                        if (weatherConditionsList != null) {
                            for (int ii = 0; ii < weatherConditionsList.size(); ii++) {
                                WeatherConditionsDTO weatherConditions = weatherConditionsList.get(ii);
                                List<ValueDTO> values = weatherConditions.getValues();
                                if (values != null) {
                                    for (int jj = 0; jj < values.size(); jj++) {
                                        ValueDTO value = values.get(jj);
                                        VisibilityDTO visibility = value.getVisibility();
                                        if (visibility != null) {
                                            return visibility.getValue();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getVisibilityUnits() {
        if (_dwml != null) {
            DataDTO data = _dwml.getObservation();
            if (data != null) {
                ParametersDTO parameters = data.getParameters();
                if (parameters != null) {
                    WeatherDTO weather = parameters.getWeather();
                    if (weather != null) {
                        List<WeatherConditionsDTO> weatherConditionsList = weather.getWeatherConditions();
                        if (weatherConditionsList != null) {
                            for (int ii = 0; ii < weatherConditionsList.size(); ii++) {
                                WeatherConditionsDTO weatherConditions = weatherConditionsList.get(ii);
                                List<ValueDTO> values = weatherConditions.getValues();
                                if (values != null) {
                                    for (int jj = 0; jj < values.size(); jj++) {
                                        ValueDTO value = values.get(jj);
                                        VisibilityDTO visibility = value.getVisibility();
                                        if (visibility != null) {
                                            return visibility.getUnits();
                                        }
                                    }
                                }
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
        _observationTable.removeAllViews();
        if (_dwml != null) {
            String locationText = getAreaDescription();
            Double latitude = getLatitude();
            Double longitude = getLongitude();
            Double elevation = getElevation();
            String elevationUnits = getElevationUnits();
            Double apparentTemperature = getApparentTemperature();
            Double dewPointTemperature = getDewPointTemperature();
            Double windDirection = getWindDirection();
            Double windSpeed = getWindSpeed();
            Double windGust = getWindGust();
            Double humidity = getHumidity();
            Double feelsLike = FeelsLikeTemperatureCalculator.computeFeelsLikeTemperature(apparentTemperature, humidity, windSpeed);
            Double pressure = getPressure();
            String conditionsIcon = getConditionsIcon();
            Double visibility = getVisibility();
            String visibilityUnits = getVisibilityUnits();

            TableRow row = new TableRow(getContext());
            row.setVisibility(View.VISIBLE);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            LinearLayout item = (LinearLayout) _inflater.inflate(R.layout.observation_item, null);
            item.setVisibility(View.VISIBLE);

            if (locationText != null) {
                TextView location = (TextView) item.findViewById(R.id.location);
                location.setText(locationText);
            }

            TextView coordinates = (TextView) item.findViewById(R.id.coordinates);
            if ((latitude != null) && (longitude != null)) {
                coordinates.setVisibility(View.VISIBLE);
                coordinates.setText("(" + _coordinatesFormat.format(latitude.doubleValue()) + ", " + _coordinatesFormat.format(longitude.doubleValue()) + ")");
            } else {
                coordinates.setVisibility(View.GONE);
            }

            TextView elevationText = (TextView) item.findViewById(R.id.elevation);
            if ((elevation != null) && (elevationUnits != null)) {
                elevationText.setVisibility(View.VISIBLE);
                elevationText.setText(_elevationFormat.format(elevation.doubleValue()) + " " + elevationUnits);
            } else {
                elevationText.setVisibility(View.GONE);
            }

            if (conditionsIcon != null) {
                ImageView currentConditionsImage = (ImageView) item.findViewById(R.id.currentConditionsImage);
                ImageTask imageTask = new ImageTask(conditionsIcon, currentConditionsImage);
                imageTask.execute((Void[]) null);
            }

            TextView temperatureText = (TextView) item.findViewById(R.id.temperature);
            if (apparentTemperature != null) {
                temperatureText.setText(_temperatureFormat.format(apparentTemperature));
            } else {
                temperatureText.setText("");
            }

            TextView feelsLikeTemperatureText = (TextView)item.findViewById(R.id.feelsLikeTemperature);
            if (feelsLike != null) {
                feelsLikeTemperatureText.setVisibility(View.VISIBLE);
                feelsLikeTemperatureText.setText("(Feels Like " + _temperatureFormat.format(feelsLike.doubleValue()) + ")");
            } else {
                feelsLikeTemperatureText.setVisibility(View.GONE);
            }

            TextView humidityLabel = (TextView) item.findViewById(R.id.humidityLabel);
            TextView humidityText = (TextView) item.findViewById(R.id.humidity);
            if (humidity != null) {
                humidityLabel.setVisibility(View.VISIBLE);
                humidityText.setVisibility(View.VISIBLE);
                humidityText.setText(_humidityFormat.format(humidity));
            } else {
                humidityLabel.setVisibility(View.GONE);
                humidityText.setVisibility(View.GONE);
            }

            TextView windLabel = (TextView)item.findViewById(R.id.windLabel);
            TextView windText = (TextView)item.findViewById(R.id.wind);
            if (windSpeed != null) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(_windFormat.format(windSpeed));
                if (windSpeed.doubleValue() > 0) {
                    if (windDirection != null) {
                        String compass = WindDirectionConverter.degreesToCompass(windDirection);
                        buffer.append(" " + compass);
                    }
                }
                windLabel.setVisibility(View.VISIBLE);
                windText.setVisibility(View.VISIBLE);
                windText.setText(buffer.toString());
            } else {
                windLabel.setVisibility(View.GONE);
                windText.setVisibility(View.GONE);
            }

            TextView dewPointLabel = (TextView) item.findViewById(R.id.dewPointLabel);
            TextView dewPointText = (TextView) item.findViewById(R.id.dewPoint);
            if (dewPointTemperature != null) {
                dewPointLabel.setVisibility(View.VISIBLE);
                dewPointText.setVisibility(View.VISIBLE);
                dewPointText.setText(_temperatureFormat.format(dewPointTemperature));
            } else {
                dewPointLabel.setVisibility(View.GONE);
                dewPointText.setVisibility(View.GONE);
            }

            TextView visibilityLabel = (TextView)item.findViewById(R.id.visibilityLabel);
            TextView visibilityText = (TextView)item.findViewById(R.id.visibility);
            if ((visibility != null) && (visibilityUnits != null)) {
                visibilityLabel.setVisibility(View.VISIBLE);
                visibilityText.setVisibility(View.VISIBLE);
                visibilityText.setText(_visibilityFormat.format(visibility) + " " + visibilityUnits);
            } else {
                visibilityLabel.setVisibility(View.GONE);
                visibilityText.setVisibility(View.GONE);
            }

            TextView pressureLabel = (TextView) item.findViewById(R.id.pressureLabel);
            TextView pressureText = (TextView) item.findViewById(R.id.pressure);
            if (pressure != null) {
                pressureLabel.setVisibility(View.VISIBLE);
                pressureText.setVisibility(View.VISIBLE);
                pressureText.setText(_pressureFormat.format(pressure));
            } else {
                pressureLabel.setVisibility(View.GONE);
                pressureText.setVisibility(View.GONE);
            }

            row.addView(item);
            _observationTable.addView(row);
        }
    }
}
