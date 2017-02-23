package ca.datamagic.noaa.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 1/28/2017.
 */

public class StationsAdapter extends BaseAdapter implements View.OnClickListener {
    private static Logger _logger = LogManager.getLogger(StationsAdapter.class);
    private LayoutInflater _inflater = null;
    private List<StationDTO> _stations = null;
    private List<StationsAdapterListener> _listeners = new ArrayList<StationsAdapterListener>();

    public StationsAdapter(Context context, List<StationDTO> stations) {
        _inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _stations = stations;
    }

    public void addListener(StationsAdapterListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(StationsAdapterListener listener) {
        _listeners.remove(listener);
    }

    private void FireStationClick(StationDTO station) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onStationClick(station);
            } catch (Throwable t) {
                _logger.warn("Exception", t);
            }
        }
    }

    @Override
    public int getCount() {
        return _stations.size();
    }

    @Override
    public Object getItem(int position) {
        return _stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<StationDTO> getStations() {
        return _stations;
    }

    public void add(StationDTO station) {
        boolean exists = false;
        for (int ii = 0; ii < _stations.size(); ii++) {
            if (_stations.get(ii).getStationId().compareToIgnoreCase(station.getStationId()) == 0) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            _stations.add(station);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        boolean viewInflated = false;
        if (view == null) {
            view = _inflater.inflate(R.layout.station_choose, null);
            viewInflated = true;
        }
        TextView stationName = (TextView)view.findViewById(R.id.station_name);
        if (viewInflated) {
            stationName.setOnClickListener(this);
        }
        if (_stations.size() > 0) {
            stationName.setText(_stations.get(position).getStationName());
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        TextView stationName = (TextView)v;
        for (int ii = 0; ii < _stations.size(); ii++) {
            if (_stations.get(ii).getStationName().compareToIgnoreCase(stationName.getText().toString()) == 0) {
                FireStationClick(_stations.get(ii));
                break;
            }
        }
    }

    public interface StationsAdapterListener {
        public void onStationClick(StationDTO station);
    }
}
