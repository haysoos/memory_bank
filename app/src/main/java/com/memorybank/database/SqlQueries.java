package com.memorybank.database;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public final class SqlQueries {

    public static final String CREATE_MEMORIES_TABLE = new StringBuilder()
            .append("CREATE TABLE memories (")
            .append("\n\t_id integer primary key autoincrement,")
            .append("\n\ttimestamp integer,")
            .append("\n\tlatitude  real,")
            .append("\n\tlongitude real,")
            .append("\n\tvalue text")
            .append("\n);")
            .toString();

    public static final String CREATE_MEMORY_TAGS_TABLE = new StringBuilder()
            .append("CREATE TABLE memory_tags (")
            .append("\n\t_id integer primary key autoincrement,")
            .append("\n\tmemory_id integer")
            .append("\n\tname text,")
            .append("\n\tdescription text,")
            .append("\n\tprivate integer,") //boolean field
            .append("\n\ttimestamp integer")
            .append("\n);")
            .toString();

    public static final String MEMORY_TAGS_TABLE = "memory_tags";

    private SqlQueries() {}

}
