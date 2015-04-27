package com.memorybank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.memorybank.model.Memory;
import com.memorybank.model.MemoryTag;

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
        contentValues.put(MemoriesTable.TIMESTAMP.columnName(), memory.getTimestamp());
        contentValues.put(MemoriesTable.LATITUDE.columnName(), memory.getLatitude());
        contentValues.put(MemoriesTable.LONGITUDE.columnName(), memory.getLongitude());
        contentValues.put(MemoriesTable.VALUE.columnName(), memory.getValue());

        mWritableDatabase.insert(SqlQueries.MEMORIES_TABLE, null, contentValues);
    }

    public Cursor getMemories() {
        Cursor cursor = new SqlQueryBuilder()
                .select(new String[] {
                        MemoriesTable.ID.columnTableName(),
                        MemoriesTable.TIMESTAMP.columnTableName(),
                        MemoriesTable.LATITUDE.columnTableName(),
                        MemoriesTable.LONGITUDE.columnTableName(),
                        MemoriesTable.VALUE.columnTableName()
                })
                .fromTable(SqlQueries.MEMORIES_TABLE)
                .where("memories._id not in (select memory_id from memory_tags_map inner join memory_tags on " +
                        "memory_tags_map.tag_id=memory_tags._id where private=1)")
                .orderBy(MemoriesTable.TIMESTAMP.columnTableName(), SqlQueryBuilder.SortDirection.Descending)
                .executeQuery();

        return cursor;
    }

    public Cursor getTags() {
        Cursor cursor = new SqlQueryBuilder()
                .fromTable(SqlQueries.MEMORY_TAGS_TABLE)
                .orderBy(TagsTable.TIMESTAMP.columnTableName(), SqlQueryBuilder.SortDirection.Ascending)
                .executeQuery();

        return cursor;
    }

    public Cursor getTagsWithMemoryid(int memoryId) {
        Cursor cursor = new SqlQueryBuilder()
                .fromTable(SqlQueries.MEMORY_TAGS_TABLE)
                .innerJoin(SqlQueries.MEMORY_TAGS_MAP_TABLE, "memory_tags_map.tag_id=memory_tags._id")
                .where("memory_id = ?", Integer.toString(memoryId))
                .orderBy("timestamp", SqlQueryBuilder.SortDirection.Ascending)
                .executeQuery();

        return cursor;
    }

    public Cursor getMatchingTagsIds(long memoryId) {
        Cursor cursor = new SqlQueryBuilder()
                .select(new String[]{TagsTable.ID.columnTableName()})
                .fromTable(SqlQueries.MEMORY_TAGS_TABLE)
                .innerJoin(SqlQueries.MEMORY_TAGS_MAP_TABLE, "memory_tags_map.tag_id=memory_tags._id")
                .where("memory_id = ?", Long.toString(memoryId))
                .executeQuery();

        return cursor;
    }

    public long saveTag(MemoryTag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagsTable.TIMESTAMP.columnName(), tag.getTimestamp());
        contentValues.put(TagsTable.NAME.columnName(), tag.getName());
        contentValues.put(TagsTable.DESCRIPTION.columnName(), tag.getDescription());
        contentValues.put(TagsTable.PRIVATE.columnName(), tag.isPrivate());

        return mWritableDatabase.insertWithOnConflict(SqlQueries.MEMORY_TAGS_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void saveTagsForMemory(long memoryId, Set<Long> selectedTags, Set<Long> unselectedTags) {
        long timestamp = System.currentTimeMillis();
        try {
            mWritableDatabase.beginTransaction();
            for (Long tagId : selectedTags) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TagsMapTable.TIMESTAMP.columnName(), timestamp);
                contentValues.put(TagsMapTable.TAG_ID.columnName(), tagId);
                contentValues.put(TagsMapTable.MEMORY_ID.columnName(), memoryId);

                mWritableDatabase.insertWithOnConflict(SqlQueries.MEMORY_TAGS_MAP_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            for (Long tagId : unselectedTags) {
                mWritableDatabase.delete(SqlQueries.MEMORY_TAGS_MAP_TABLE, "memory_id=? AND tag_id=?", new String[]{Long.toString(memoryId), Long.toString(tagId)});
            }

            mWritableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            mWritableDatabase.endTransaction();
        }
    }

    public Cursor searchMemories(String searchQuery) {
        Cursor cursor = new SqlQueryBuilder()
                .select(new String[] {"memories._id", "memories.timestamp", "latitude",
                        "longitude", "value"})
                .fromTable(SqlQueries.MEMORIES_TABLE)
                .innerJoin(SqlQueries.MEMORY_TAGS_MAP_TABLE, "memory_tags_map.memory_id = memories._id")
                .innerJoin(SqlQueries.MEMORY_TAGS_TABLE, "memory_tags_map.tag_id = memory_tags._id")
                .where("memory_tags.name like ?", "%" + searchQuery + "%")
                .orderBy("memories.timestamp", SqlQueryBuilder.SortDirection.Descending)
                .executeQuery();

        return cursor;
    }

    public Memory getMemory(long mMemoryId) {

        Cursor cursor = new SqlQueryBuilder()
                .select(new String[] {"memories._id", "memories.timestamp", "latitude",
                        "longitude", "value"})
                .fromTable(SqlQueries.MEMORIES_TABLE)
                .where("_id = ?", Long.toString(mMemoryId))
                .executeQuery();

        try {
            Memory memory = null;
            if (cursor != null && cursor.moveToFirst()) {
                if (cursor.moveToFirst()) {
                    do {
                        memory = new Memory(cursor.getLong(0), cursor.getLong(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4));
                    } while (cursor.moveToNext());
                }
            }

            return memory;
        } finally {
            cursor.close();
        }

    }

    public void updateMemory(Memory memory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", memory.getValue());
        mWritableDatabase.updateWithOnConflict(SqlQueries.MEMORIES_TABLE, contentValues, "_id=?", new String[] {Long.toString(memory.getId())}, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void updateTag(MemoryTag tag) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", tag.getName());
        contentValues.put("description", tag.getDescription());
        contentValues.put("private", tag.isPrivate());
        mWritableDatabase.updateWithOnConflict(SqlQueries.MEMORY_TAGS_TABLE, contentValues, "_id=?", new String[] {Long.toString(tag.getId())}, SQLiteDatabase.CONFLICT_IGNORE);
    }
}
