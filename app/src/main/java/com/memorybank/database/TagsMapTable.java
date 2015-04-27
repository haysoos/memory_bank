package com.memorybank.database;

/**
 * Created by Jesus Medrano on 4/26/15.
 */
public enum TagsMapTable {
    ID("_id"),
    MEMORY_ID("memory_id"),
    TAG_ID("tag_id"),
    TIMESTAMP("timestamp")
    ;
    private static final String TABLE_NAME = "memory_tags_map";
    private final String mColumnName;

    TagsMapTable(String columnName) {
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
