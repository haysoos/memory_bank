package com.memorybank.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.memorybank.R;

import java.util.Date;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public class MemoriesAdapter extends CursorAdapter {

    public static final String TAG = "MemoriesAdapter";
    private final Context mContext;

    public MemoriesAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        mContext = context.getApplicationContext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.memory_row_view, parent, false);

        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView id = (TextView) view.findViewById(R.id.tvId);
        TextView timestamp = (TextView) view.findViewById(R.id.tvTimeStamp);
        TextView value = (TextView) view.findViewById(R.id.tvValue);
        TextView latitude = (TextView) view.findViewById(R.id.tvLatitude);
        TextView longitude = (TextView) view.findViewById(R.id.tvLongitude);

        id.setText(cursor.getInt(0) + "");
        Date date = new Date(cursor.getLong(1));
        timestamp.setText(date.toString());
        latitude.setText(cursor.getDouble(2) + ", ");
        longitude.setText(cursor.getDouble(3) + "");
        value.setText(cursor.getString(4));
    }
}
