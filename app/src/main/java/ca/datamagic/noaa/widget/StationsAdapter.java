package ca.datamagic.noaa.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 1/28/2017.
 */

public class StationsAdapter extends BaseAdapter implements View.OnClickListener {
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

    private void fireStationAdded(StationDTO station) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onStationAdded(station);
            } catch (Throwable t) {
                // TODO
            }
        }
    }

    private void fireStationSelect(StationDTO station) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onStationSelect(station);
            } catch (Throwable t) {
                // TODO
            }
        }
    }

    private void fireStationRemove(StationDTO station) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onStationRemove(station);
            } catch (Throwable t) {
                // TODO
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
            fireStationAdded(station);
        }
    }

    public void remove(StationDTO station) {
        for (int ii = 0; ii < _stations.size(); ii++) {
            if (_stations.get(ii).getStationId().compareToIgnoreCase(station.getStationId()) == 0) {
                _stations.remove(ii);
                break;
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationDTO station = null;
        if (_stations.size() > 0) {
            station = _stations.get(position);
        }
        View view = convertView;
        boolean viewInflated = false;
        if (view == null) {
            view = _inflater.inflate(R.layout.station_choose, null);
            viewInflated = true;
        }
        TextView stationName = (TextView)view.findViewById(R.id.station_name);
        if (station != null) {
            stationName.setTag(station);
            stationName.setText(station.getStationName());
        }
        if (viewInflated) {
            stationName.setOnClickListener(this);
        }
        ImageView removeStation = (ImageView)view.findViewById(R.id.remove_station);
        removeStation.setClickable(true);
        if (station != null) {
            removeStation.setTag(station);
        }
        if (viewInflated) {
            removeStation.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            if (tag instanceof StationDTO) {
                if (v instanceof TextView) {
                    fireStationSelect((StationDTO) tag);
                } else  if (v instanceof ImageView) {
                    fireStationRemove((StationDTO) tag);
                }
            }
        }
    }

    public interface StationsAdapterListener {
        public void onStationAdded(StationDTO station);
        public void onStationSelect(StationDTO station);
        public void onStationRemove(StationDTO station);
    }
}
