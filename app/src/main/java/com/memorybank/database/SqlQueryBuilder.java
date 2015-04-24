package com.memorybank.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jesus Medrano on 4/24/15.
 */
public class SqlQueryBuilder {
    private String mTableName;
    private static SQLiteDatabase mReadableDatabase;
    private String[] mColumns;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;
    private String mLimit;

    public static void init(SQLiteDatabase readableDatabase) {
        mReadableDatabase = readableDatabase;
    }

    public SqlQueryBuilder fromTable(String tableName) {
        mTableName = tableName;
        return this;
    }

    public SqlQueryBuilder select(String[] columnsSelected) {
        mColumns = columnsSelected;
        return this;
    }

    public SqlQueryBuilder where(String whereStatement, String[] selectedArguments) {
        mSelection = whereStatement;
        mSelectionArgs = selectedArguments;
        return this;
    }

    public SqlQueryBuilder groupBy(String groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    public SqlQueryBuilder having(String having) {
        mHaving = having;
        return this;
    }

    public SqlQueryBuilder orderBy(String orderByColumn, SortDirection orderDirection) {
        mOrderBy = orderByColumn + " " + orderDirection.toString();
        return this;
    }

    public SqlQueryBuilder limit(int count) {
        mLimit = Integer.toString(count);
        return this;
    }

    public Cursor executeQuery() {
        return mReadableDatabase.query(mTableName, mColumns, mSelection, mSelectionArgs, mGroupBy, mHaving, mOrderBy, mLimit);
    }

    enum SortDirection {
        Ascending("ASC"),
        Descending("DESC");

        private final String mDirection;

        SortDirection(String direction){
            mDirection = direction;
        }

        @Override
        public String toString() {
            return mDirection;
        }
    }
}
