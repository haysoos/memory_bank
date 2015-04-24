package com.memorybank.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.memorybank.R;
import com.memorybank.database.MemoriesDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jesus Medrano on 4/19/15.
 */
public class TagsAdapter extends CursorAdapter {

    public static final String TAG = "MemoriesAdapter";
    private final Context mContext;
    private Set<Long> mSelectedTags = new HashSet<Long>();
    private int mDefaultBackgroundColor;
    private int mSelectedBackgroundColor;
    private Set<Long> mUnselectedTags = new HashSet<Long>();

    public TagsAdapter(Context context) {
        super(context, null, false);
        mContext = context.getApplicationContext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tag_row_view, parent, false);
        Resources resources = context.getResources();
        mDefaultBackgroundColor = resources.getColor(android.R.color.white);
        mSelectedBackgroundColor = resources.getColor(android.R.color.darker_gray);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView id = (TextView) view.findViewById(R.id.tvId);
        TextView name = (TextView) view.findViewById(R.id.tvName);
        TextView description = (TextView) view.findViewById(R.id.tvDescription);
        TextView isPrivate = (TextView) view.findViewById(R.id.tvIsPrivate);
        TextView timestamp = (TextView) view.findViewById(R.id.tvTagTimeStamp);

        long tagId = cursor.getLong(0);
        id.setText(Long.toString(tagId));
        name.setText(cursor.getString(1));
        description.setText(cursor.getString(2));
        if (cursor.getInt(3) > 0) {
            isPrivate.setText(Boolean.TRUE.toString());
        } else {
            isPrivate.setText(Boolean.FALSE.toString());
        }
        Date date = new Date(cursor.getLong(4));
        timestamp.setText(date.toString());

        if (mSelectedTags.contains(tagId)) {
            view.setBackgroundColor(mSelectedBackgroundColor);
        } else {
            view.setBackgroundColor(mDefaultBackgroundColor);
        }

    }

    public void onItemClick(View view, long id) {
        if (mSelectedTags.contains(id)) {
            mSelectedTags.remove(id);
            mUnselectedTags.add(id);
            view.setBackgroundResource(android.R.color.white);
        } else {
            view.setBackgroundResource(android.R.color.darker_gray);
            mSelectedTags.add(id);
            if (mUnselectedTags.contains(id)) {
                mUnselectedTags.remove(id);
            }
        }
    }

    public Set<Long> getSelectedTags() {
        return mSelectedTags;
    }

    public void setSelectedTags(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.moveToFirst()) {
                do {
                    mSelectedTags.add(cursor.getLong(0));
                } while (cursor.moveToNext());
            }
        }
    }

    public Set<Long> getUnselectedTags() {
        return mUnselectedTags;
    }
}
