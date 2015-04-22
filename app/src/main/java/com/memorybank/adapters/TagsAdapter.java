package com.memorybank.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.memorybank.R;

import java.util.Date;

/**
 * Created by Jesus Medrano on 4/19/15.
 */
public class TagsAdapter extends CursorAdapter {

    public static final String TAG = "MemoriesAdapter";
    private final Context mContext;

    public TagsAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        mContext = context.getApplicationContext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tag_row_view, parent, false);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView id = (TextView) view.findViewById(R.id.tvId);
        TextView name = (TextView) view.findViewById(R.id.tvName);
        TextView description = (TextView) view.findViewById(R.id.tvDescription);
        TextView isPrivate = (TextView) view.findViewById(R.id.tvIsPrivate);
        TextView timestamp = (TextView) view.findViewById(R.id.tvTagTimeStamp);

        id.setText(cursor.getInt(0) + "");
        name.setText(cursor.getString(1));
        description.setText(cursor.getString(2));
        if (cursor.getInt(3) > 0) {
            isPrivate.setText("True");
        } else {
            isPrivate.setText("False");
        }
        Date date = new Date(cursor.getLong(4));
        timestamp.setText(date.toString());


    }
}
