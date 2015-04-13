package com.memorybank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.memorybank.model.Memory;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public class MemoriesDatabase {

    private static MemoriesDatabase instance;
    private static SQLiteDatabase mWritableDatabase;
    private static SQLiteDatabase mReadableDatabase;

    private MemoriesDatabase() {}

    public static void initialize(Context context) {
        instance = new MemoriesDatabase();
        MemoriesDatabaseHelper memoriesDatabaseHelper = new MemoriesDatabaseHelper(context);
        mWritableDatabase = memoriesDatabaseHelper.getWritableDatabase();
        mReadableDatabase = memoriesDatabaseHelper.getReadableDatabase();

    }

    public static MemoriesDatabase getInstance() {
        return instance;
    }

    public void saveMemory(Memory memory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("timestamp", memory.getTimestamp());
        contentValues.put("latitude", memory.getLatitude());
        contentValues.put("longitude", memory.getLongitude());
        contentValues.put("value", memory.getValue());

        mWritableDatabase.insert("memories", null, contentValues);
    }

    public Cursor getMemories() {
        Cursor cursor = mReadableDatabase.query("memories", new String[] {"_id", "timestamp", "latitude",
                "longitude", "value"}, null, null, null, null, "timestamp");
        return cursor;
    }

}
