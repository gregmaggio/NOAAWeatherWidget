package ca.datamagic.noaa.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Greg on 3/24/2018.
 */

public class PredictionsSearchAdapter extends CursorAdapter {
    public PredictionsSearchAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView)view.findViewById(R.id.item);
        int index = cursor.getColumnIndex("description");
        String text = cursor.getString(index);
        textView.setText(text);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.search_item, parent, false);
        return view;
    }
}
