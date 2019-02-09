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

import ca.datamagic.noaa.dto.LocationDTO;

/**
 * Created by Greg on 1/28/2017.
 */

public class LocationsAdapter extends BaseAdapter implements View.OnClickListener {
    private LayoutInflater _inflater = null;
    private List<LocationDTO> _locations = null;
    private List<LocationsAdapterListener> _listeners = new ArrayList<LocationsAdapterListener>();

    public LocationsAdapter(Context context, List<LocationDTO> locations) {
        _inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _locations = locations;
    }

    public void addListener(LocationsAdapterListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(LocationsAdapterListener listener) {
        _listeners.remove(listener);
    }

    private void fireLocationAdded(LocationDTO location) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onLocationAdded(location);
            } catch (Throwable t) {
                // TODO
            }
        }
    }

    private void fireLocationSelect(LocationDTO location) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onLocationSelect(location);
            } catch (Throwable t) {
                // TODO
            }
        }
    }

    private void fireLocationRemove(LocationDTO location) {
        for (int ii = 0; ii < _listeners.size(); ii++) {
            try {
                _listeners.get(ii).onLocationRemove(location);
            } catch (Throwable t) {
                // TODO
            }
        }
    }

    @Override
    public int getCount() {
        return _locations.size();
    }

    @Override
    public Object getItem(int position) {
        return _locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<LocationDTO> getLocations() {
        return _locations;
    }

    public void add(LocationDTO location) {
        boolean exists = false;
        for (int ii = 0; ii < _locations.size(); ii++) {
            if (_locations.get(ii).getDescription().compareToIgnoreCase(location.getDescription()) == 0) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            _locations.add(location);
            fireLocationAdded(location);
        }
    }

    public void remove(LocationDTO location) {
        for (int ii = 0; ii < _locations.size(); ii++) {
            if (_locations.get(ii).getDescription().compareToIgnoreCase(location.getDescription()) == 0) {
                _locations.remove(ii);
                break;
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocationDTO location = null;
        if (_locations.size() > 0) {
            location = _locations.get(position);
        }
        View view = convertView;
        boolean viewInflated = false;
        if (view == null) {
            view = _inflater.inflate(R.layout.location_choose, null);
            viewInflated = true;
        }
        TextView stationName = (TextView)view.findViewById(R.id.location_name);
        if (location != null) {
            stationName.setTag(location);
            stationName.setText(location.getDescription());
        }
        if (viewInflated) {
            stationName.setOnClickListener(this);
        }
        ImageView removeStation = (ImageView)view.findViewById(R.id.remove_location);
        removeStation.setClickable(true);
        if (location != null) {
            removeStation.setTag(location);
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
            if (tag instanceof LocationDTO) {
                if (v instanceof TextView) {
                    fireLocationSelect((LocationDTO) tag);
                } else  if (v instanceof ImageView) {
                    fireLocationRemove((LocationDTO) tag);
                }
            }
        }
    }

    public interface LocationsAdapterListener {
        public void onLocationAdded(LocationDTO location);
        public void onLocationSelect(LocationDTO location);
        public void onLocationRemove(LocationDTO location);
    }
}
