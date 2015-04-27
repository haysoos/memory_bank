package com.memorybank.database;

/**
 * Created by Jesus Medrano on 4/26/15.
 */
public enum TagsTable {
    ID("_id"),
    NAME("name"),
    DESCRIPTION("description"),
    PRIVATE("private"),
    TIMESTAMP("timestamp")
    ;

    private final String mColumnName;
    private final static String TABLE_NAME = "memory_tags";

    TagsTable(String columnName) {
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
