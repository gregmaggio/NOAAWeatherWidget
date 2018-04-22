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

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dto.ObservationDTO;
import ca.datamagic.noaa.logging.LogFactory;
import ca.datamagic.noaa.util.FeelsLikeTemperatureCalculator;
import ca.datamagic.noaa.util.WindDirectionConverter;

/**
 * Created by Greg on 1/10/2016.
 */
public class ObservationFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(ObservationFragment.class);
    private static DecimalFormat _coordinatesFormat = new DecimalFormat("0.0");
    private static DecimalFormat _elevationFormat = new DecimalFormat("0.0");
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static DecimalFormat _humidityFormat = new DecimalFormat("0");
    private static DecimalFormat _pressureFormat = new DecimalFormat("0");
    private static DecimalFormat _windFormat = new DecimalFormat("0");
    private static DecimalFormat _visibilityFormat = new DecimalFormat("0.00");

    public ObservationDTO getObservation() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getParcelable("observation");
        }
        return null;
    }

    public void setObservation(ObservationDTO newVal) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putParcelable("observation", newVal);
        }
    }

    public static ObservationFragment newInstance() {
        return newInstance(null);
    }

    public static ObservationFragment newInstance(ObservationDTO observation) {
        ObservationFragment fragment = new ObservationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("observation", observation);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static int getLayoutId() {
        return R.layout.observation_main;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.observation_main, container, false);
        render(view, inflater);
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
        View view = getView();
        LayoutInflater inflater = getLayoutInflater();
        if ((view != null) && (inflater != null)) {
            render(view, inflater);
        }
    }

    private void render(View view, LayoutInflater inflater) {
        TableLayout observationTable = (TableLayout)view.findViewById(R.id.observationTable);
        observationTable.removeAllViews();
        ObservationDTO observation = getObservation();
        if (observation != null) {
            String locationText = observation.getLocationText();
            Double latitude = observation.getLatitude();
            Double longitude = observation.getLongitude();
            Double elevation = observation.getElevation();
            String elevationUnits = observation.getElevationUnits();
            Double temperature = observation.getTemperature();
            Double dewPoint = observation.getDewPoint();
            Double windDirection = observation.getWindDirection();
            Double windSpeed = observation.getWindSpeed();
            String windSpeedUnits = observation.getWindSpeedUnits();
            Double windGust = observation.getWindGust();
            String windGustUnits = observation.getWindGustUnits();
            Double humidity = observation.getRelativeHumidity();
            Double feelsLike = FeelsLikeTemperatureCalculator.computeFeelsLikeTemperature(temperature, humidity, windSpeed);
            Double pressure = observation.getPressure();
            String pressureUnits = observation.getPressureUnits();
            String conditionsIcon = observation.getIconUrl();
            Double visibility = observation.getVisibility();
            String visibilityUnits = observation.getVisibilityUnits();

            TableRow row = new TableRow(getContext());
            row.setVisibility(View.VISIBLE);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            LinearLayout item = (LinearLayout)inflater.inflate(R.layout.observation_item, null);
            item.setVisibility(View.VISIBLE);

            if (observation.isCached()) {
                LinearLayout cached = (LinearLayout)item.findViewById(R.id.cached);
                cached.setVisibility(View.VISIBLE);
            }

            if ((locationText != null) && (locationText.length() > 0)) {
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
            if (temperature != null) {
                temperatureText.setText(_temperatureFormat.format(temperature));
            } else {
                temperatureText.setText(R.string.not_available);
            }

            TextView feelsLikeTemperatureText = (TextView)item.findViewById(R.id.feelsLikeTemperature);
            String feelsLikeFormatted = null;
            if (feelsLike != null) {
                feelsLikeFormatted = _temperatureFormat.format(feelsLike.doubleValue());
            }
            if ((feelsLikeFormatted != null) && (feelsLikeFormatted.length() > 0) && (feelsLikeFormatted.compareToIgnoreCase("NaN") != 0)) {
                feelsLikeTemperatureText.setVisibility(View.VISIBLE);
                feelsLikeTemperatureText.setText("(Feels Like " + feelsLikeFormatted + ")");
            } else {
                feelsLikeTemperatureText.setVisibility(View.GONE);
            }

            TextView dewPointText = (TextView) item.findViewById(R.id.dewPoint);
            if (dewPoint != null) {
                dewPointText.setText(_temperatureFormat.format(dewPoint.doubleValue()));
            } else {
                dewPointText.setText(R.string.not_available);
            }

            TextView humidityText = (TextView) item.findViewById(R.id.humidity);
            if (humidity != null) {
                humidityText.setText(_humidityFormat.format(humidity.doubleValue()));
            } else {
                humidityText.setText(R.string.not_available);
            }

            TextView wind = (TextView)item.findViewById(R.id.wind);
            if ((windSpeed != null) && (windSpeedUnits != null) && (windSpeedUnits.length() > 0)) {
                StringBuffer windBuffer = new StringBuffer();
                windBuffer.append(_windFormat.format(windSpeed.doubleValue()));
                windBuffer.append(" ");
                windBuffer.append(windSpeedUnits);
                if (windDirection != null) {
                    String compass = WindDirectionConverter.degreesToCompass(windDirection);
                    windBuffer.append(" ");
                    windBuffer.append(compass);
                }
                wind.setText(windBuffer.toString());
            } else {
                wind.setText(R.string.not_available);
            }

            if ((windGust != null) && (windGustUnits != null) && (windGustUnits.length() > 0)) {
                LinearLayout windGustView = (LinearLayout)item.findViewById(R.id.windGustView);
                TextView windGustText = (TextView)item.findViewById(R.id.windGust);
                String windGustFormat = getResources().getString(R.string.wind_gust);
                windGustText.setText(MessageFormat.format(windGustFormat, _windFormat.format(windGust.doubleValue()), windGustUnits));
                windGustView.setVisibility(View.VISIBLE);
            }

            TextView visibilityText = (TextView)item.findViewById(R.id.visibility);
            if ((visibility != null) && (visibilityUnits != null)) {
                visibilityText.setText(_visibilityFormat.format(visibility.doubleValue()) + " " + visibilityUnits);
            } else {
                visibilityText.setText(R.string.not_available);
            }

            TextView pressureText = (TextView) item.findViewById(R.id.pressure);
            if ((pressure != null) && (pressureUnits != null)) {
                pressureText.setText(_pressureFormat.format(pressure.doubleValue()) + " " + pressureUnits);
            } else {
                pressureText.setText(R.string.not_available);
            }

            row.addView(item);
            observationTable.addView(row);
        }
    }
}
