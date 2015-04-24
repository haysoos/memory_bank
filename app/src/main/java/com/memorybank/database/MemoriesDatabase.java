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
import java.util.Set;

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
        SqlQueryBuilder.init(mReadableDatabase);
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
        Cursor cursor = new SqlQueryBuilder()
                .select(new String[] {"_id", "timestamp", "latitude",
                        "longitude", "value"})
                .fromTable(SqlQueries.MEMORIES_TABLE)
                .orderBy("timestamp", SqlQueryBuilder.SortDirection.Descending)
                .executeQuery();

        return cursor;
    }

    public Cursor getTags() {
        Cursor cursor = new SqlQueryBuilder()
                .fromTable(SqlQueries.MEMORY_TAGS_TABLE)
                .orderBy("timestamp", SqlQueryBuilder.SortDirection.Ascending)
                .executeQuery();

        return cursor;
    }

    public Cursor getTagsWithMemoryid(int memoryId) {
        Cursor cursor = new SqlQueryBuilder()
                .fromTable(SqlQueries.MEMORY_TAGS_TABLE)
                .orderBy("timestamp", SqlQueryBuilder.SortDirection.Ascending)
                .executeQuery();

        return cursor;
    }

    public long saveTag(MemoryTag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("timestamp", tag.getTimestamp());
        contentValues.put("name", tag.getName());
        contentValues.put("description", tag.getDescription());
        contentValues.put("private", tag.isPrivate());

        return mWritableDatabase.insertWithOnConflict(SqlQueries.MEMORY_TAGS_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void saveTagsForMemory(long mMemoryId, Set<Long> mCheckedTags) {
        long timestamp = System.currentTimeMillis();
        try {
            mWritableDatabase.beginTransaction();
            for (Long tagId : mCheckedTags) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("timestamp", timestamp);
                contentValues.put("tag_id", tagId);
                contentValues.put("memory_id", mMemoryId);

                mWritableDatabase.insertWithOnConflict(SqlQueries.MEMORY_TAGS_MAP_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
            mWritableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            mWritableDatabase.endTransaction();
        }
    }
}
