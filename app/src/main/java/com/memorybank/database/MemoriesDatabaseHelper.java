package com.memorybank.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public class MemoriesDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "memories";

    public MemoriesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlQueries.CREATE_MEMORIES_TABLE);
        db.execSQL(SqlQueries.CREATE_MEMORY_TAGS_TABLE);
        db.execSQL(SqlQueries.CREATE_MEMORY_TAGS_MAP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
