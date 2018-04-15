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

import ca.datamagic.noaa.async.ImageTask;
import ca.datamagic.noaa.dto.ForecastDTO;
import ca.datamagic.noaa.dto.ForecastsDTO;

/**
 * Created by Greg on 1/10/2016.
 */
public class ForecastFragment extends Fragment implements Renderer {
    private static DecimalFormat _temperatureFormat = new DecimalFormat("0");
    private static char _degrees = (char)0x00B0;
    private ForecastsDTO _forecasts = null;

    public void setForecasts(ForecastsDTO newVal) {
        _forecasts = newVal;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_main, container, false);
        render(view, inflater);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            ForecastsDTO forecasts = (ForecastsDTO) savedInstanceState.getParcelable("forecasts");
            _forecasts = forecasts;
        } catch (NullPointerException ex) {
            // Do Nothing
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("forecasts", ((_forecasts == null) ? (new ForecastsDTO()) : _forecasts));
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
        TableLayout forecastTable = (TableLayout)view.findViewById(R.id.forecastTable);
        forecastTable.removeAllViews();
        if (forecastTable != null) {
            if (_forecasts != null) {
                List<ForecastDTO> items = _forecasts.getItems();
                if (items != null) {
                    if (_forecasts.isCached()) {
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
                            temperatureView.setText(_temperatureFormat.format(items.get(ii).getTemperature().doubleValue()) + _degrees);
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
