package com.memorybank.database;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public final class SqlQueries {

    public static final String MEMORIES_TABLE = "memories";
    public static final String MEMORY_TAGS_TABLE = "memory_tags";
    public static final String MEMORY_TAGS_MAP_TABLE = "memory_tags_map";

    public static final String CREATE_MEMORIES_TABLE = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(MEMORIES_TABLE)
            .append(" (")
            .append("\n\t_id integer primary key autoincrement,")
            .append("\n\ttimestamp integer,")
            .append("\n\tlatitude  real,")
            .append("\n\tlongitude real,")
            .append("\n\tvalue text")
            .append("\n);")
            .toString();

    public static final String CREATE_MEMORY_TAGS_TABLE = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(MEMORY_TAGS_TABLE)
            .append(" (")
            .append("\n\t_id integer primary key autoincrement,")
            .append("\n\tname text,")
            .append("\n\tdescription text,")
            .append("\n\tprivate integer,") //boolean field
            .append("\n\ttimestamp integer")
            .append("\n);")
            .toString();

    public static final String CREATE_MEMORY_TAGS_MAP_TABLE = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(MEMORY_TAGS_MAP_TABLE)
            .append(" (")
            .append("\n\ttag_id integer,")
            .append("\n\tmemory_id integer,")
            .append("\n\ttimestamp integer,")
            .append("\n\tUNIQUE(tag_id, memory_id)")
            .append("\n);")
            .toString();

    public static final String DELETE_MEMORY_TAGS_TABLE = "DROP TABLE IF EXISTS " + MEMORY_TAGS_TABLE;

    public static final String DELETE_MEMORY_TAGS_MAP_TABLE = "DROP TABLE IF EXISTS " + MEMORY_TAGS_MAP_TABLE;

    private SqlQueries() {}

}
