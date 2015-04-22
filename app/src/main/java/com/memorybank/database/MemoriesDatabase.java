package com.memorybank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.memorybank.model.Memory;
import com.memorybank.model.MemoryTag;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public class MemoriesDatabase {

    public static final String TAG = "MemoriesDatabase";
    private static MemoriesDatabase instance;
    private static SQLiteDatabase mWritableDatabase;
    private static SQLiteDatabase mReadableDatabase;
    private static boolean isInitialized = false;

    private MemoriesDatabase() {}

    public static void initialize(Context context) {
        if (isInitialized) {
            return;
        }
        isInitialized = true;
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

        mWritableDatabase.insert(SqlQueries.MEMORIES_TABLE, null, contentValues);
    }

    public Cursor getMemories() {
        Cursor cursor = mReadableDatabase.query(SqlQueries.MEMORIES_TABLE, new String[] {"_id", "timestamp", "latitude",
                "longitude", "value"}, null, null, null, null, "timestamp DESC");
        return cursor;
    }

    public Cursor getTags() {
        Cursor cursor = mReadableDatabase.query(SqlQueries.MEMORY_TAGS_TABLE, null, null, null, null, null, "timestamp");
        return cursor;
    }

    public long saveTag(MemoryTag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("timestamp", tag.getTimestamp());
        contentValues.put("name", tag.getName());
        contentValues.put("description", tag.getDescription());
        contentValues.put("private", tag.isPrivate());

        return mWritableDatabase.insert(SqlQueries.MEMORY_TAGS_TABLE, null, contentValues);
    }

    public void saveTagsForMemory(long mMemoryId, List<Long> mCheckedTags) {
        long timestamp = System.currentTimeMillis();
        try {
            mWritableDatabase.beginTransaction();
            for (Long tagId : mCheckedTags) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("timestamp", timestamp);
                contentValues.put("tag_id", tagId);
                contentValues.put("memory_id", mMemoryId);

                mWritableDatabase.insert(SqlQueries.MEMORY_TAGS_MAP_TABLE, null, contentValues);
            }
            mWritableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            mWritableDatabase.endTransaction();
        }
    }
}
