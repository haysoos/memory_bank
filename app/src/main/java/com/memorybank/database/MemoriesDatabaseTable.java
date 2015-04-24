package com.memorybank.database;

/**
 * Created by mabus on 4/24/15.
 */
public abstract class MemoriesDatabaseTable {

    private final String mDatabseTableName;

    public MemoriesDatabaseTable(String databseTableName) {
        mDatabseTableName = databseTableName;
    }


    public String getDatabseTableName() {
        return mDatabseTableName;
    }

    public abstract String[] getTableColumnNames();
}
