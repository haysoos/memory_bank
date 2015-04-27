package com.memorybank.database;

/**
 * Created by Jesus Medrano on 4/26/15.
 */
public enum MemoriesTable {

    ID("_id"),
    TIMESTAMP("timestamp"),
    LATITUDE("latitude"),
    LONGITUDE("longitude"),
    VALUE("value")
    ;
    private final String mColumnName;
    private final static String TABLE_NAME = "memories";

    MemoriesTable(String columnName) {
        mColumnName = columnName;
    }

    public String columnTableName() {
        return new StringBuilder().append(TABLE_NAME).append('.').append(mColumnName).toString();
    }

    public String columnName() {
        return mColumnName;
    }

    public int columnIndex() {
        return super.ordinal();
    }
}
