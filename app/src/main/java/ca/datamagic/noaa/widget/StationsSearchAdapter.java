package ca.datamagic.noaa.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.datamagic.noaa.dto.StationDTO;

/**
 * Created by Greg on 2/4/2017.
 */

public class StationsSearchAdapter extends CursorAdapter {
    private List<StationDTO> _stations = null;

    public StationsSearchAdapter(Context context, Cursor cursor, List<StationDTO> stations) {
        super(context, cursor, false);
        _stations = stations;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView)view.findViewById(R.id.item);
        int index = cursor.getColumnIndex("_id");
        int id = cursor.getInt(index);
        StationDTO station = _stations.get(id);
        textView.setText(station.getStationName());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.search_item, parent, false);
        return view;
    }
}
