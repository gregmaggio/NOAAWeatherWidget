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
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dao.PreferencesDAO;
import ca.datamagic.noaa.dto.ForecastDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;
import ca.datamagic.noaa.dto.PreferencesDTO;
import ca.datamagic.noaa.dto.TemperatureCalculatorDTO;
import ca.datamagic.noaa.logging.LogFactory;

/**
 * Created by Greg on 1/10/2016.
 */
public class ForecastFragment extends Fragment implements Renderer {
    private static Logger _logger = LogFactory.getLogger(ForecastFragment.class);
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;

    public ForecastsDTO getForecasts() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getParcelable("forecasts");
        }
        return null;
    }

    public void setForecasts(ForecastsDTO newVal) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putParcelable("forecasts", newVal);
        }
    }

    public static ForecastFragment newInstance() {
        return newInstance(null);
    }

    public static ForecastFragment newInstance(ForecastsDTO forecasts) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("forecasts", forecasts);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_main, container, false);
        //render(view, inflater);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        _logger.info("onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        ForecastsDTO forecasts = getForecasts();
        _logger.info("forecasts: " + forecasts);
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
            ForecastsDTO forecasts = getForecasts();
            if (forecasts != null) {
                List<ForecastDTO> items = forecasts.getItems();
                if (items != null) {
                    PreferencesDAO preferencesDAO = new PreferencesDAO(getContext());
                    PreferencesDTO preferencesDTO = preferencesDAO.read();

                    if (forecasts.isCached()) {
                        TableRow row = new TableRow(getContext());
                        row.setVisibility(View.VISIBLE);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        LinearLayout item = (LinearLayout)inflater.inflate(R.layout.cached_forecast, null);
                        item.setVisibility(View.VISIBLE);

                        row.addView(item);
                        forecastTable.addView(row);

                        TableRow spacerRow = new TableRow(getContext());
                        spacerRow.setVisibility(View.VISIBLE);
                        spacerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        LinearLayout forecastDivider = (LinearLayout)inflater.inflate(R.layout.forecast_divider, null);
                        forecastDivider.setVisibility(View.VISIBLE);
                        spacerRow.addView(forecastDivider);
                        forecastTable.addView(spacerRow);
                    }
                    for (int ii = 0; ii < items.size(); ii++) {
                        if (ii > 0) {
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

                        LinearLayout item = (LinearLayout)inflater.inflate(R.layout.forecast_item, null);
                        item.setVisibility(View.VISIBLE);

                        ImageView conditionsView = (ImageView) item.findViewById(R.id.conditions);
                        TextView weatherSummaryView = (TextView) item.findViewById(R.id.weatherSummary);
                        TextView dayMonthView = (TextView) item.findViewById(R.id.dayMonth);
                        TextView dayOfWeekView = (TextView) item.findViewById(R.id.dayOfWeek);
                        TextView temperatureView = (TextView) item.findViewById(R.id.temperature);

                        if (items.get(ii).getSummary() != null) {
                            weatherSummaryView.setText(items.get(ii).getSummary());
                        }
                        if (items.get(ii).getDayOfMonth() != null) {
                            dayMonthView.setText(items.get(ii).getDayOfMonth());
                        }
                        if (items.get(ii).getDayOfWeek() != null) {
                            dayOfWeekView.setText(items.get(ii).getDayOfWeek());
                        }
                        if (items.get(ii).getTemperature() != null) {
                            Double temperature = TemperatureCalculatorDTO.compute(items.get(ii).getTemperature(), items.get(ii).getTemperatureUnits(), preferencesDTO.getTemperatureUnits());
                            if (temperature != null) {
                                temperatureView.setText(_temperatureFormat.format(temperature.doubleValue()) + _degrees);
                            }
                        }

                        row.addView(item);
                        forecastTable.addView(row);

                        if ((items.get(ii).getImageUrl() != null) && (items.get(ii).getImageUrl().length() > 0)) {
                            ImageTask imageTask = new ImageTask(items.get(ii).getImageUrl(), conditionsView);
                            imageTask.execute((Void[]) null);
                        }
                    }
                }
            }
        }
    }
}
