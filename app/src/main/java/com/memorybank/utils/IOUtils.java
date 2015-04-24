package com.memorybank.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by mabus on 4/24/15.
 */
public final class IOUtils {

    private static final String TAG = "IOUtils";
    public static final String APP_MEMORIES_PUBLIC_DB_PATH = "memories.db";
    public static final String APP_MEMORIES_PRIVATE_DB_PATH = "memories";

    private IOUtils(){}

    public static void copySDcardDatabase(Context context) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        String DB_PATH = context.getDatabasePath(APP_MEMORIES_PRIVATE_DB_PATH).toString();
        String backupDBPath = APP_MEMORIES_PUBLIC_DB_PATH;
        File currentDB = new File(DB_PATH);
        File backupDB = new File(sd, backupDBPath);
        copyFile(backupDB, currentDB);
    }

    public static void writeToSD(Context context) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        String DB_PATH = context.getDatabasePath(APP_MEMORIES_PRIVATE_DB_PATH).toString();
        String backupDBPath = APP_MEMORIES_PUBLIC_DB_PATH;
        File currentDB = new File(DB_PATH);
        File backupDB = new File(sd, backupDBPath);
        copyFile(currentDB, backupDB);
    }

    private static void copyFile(File sourceFile, File destinationFile) throws IOException {
        if (sourceFile.exists()) {
            FileChannel src = new FileInputStream(sourceFile).getChannel();
            FileChannel dst = new FileOutputStream(destinationFile).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        } else {
            logDatabaseIssue(sourceFile, destinationFile);
        }
    }

    private static void logDatabaseIssue(File currentDB, File backupDB) {
        Log.e(TAG, "Current db does not exist\n" + currentDB.getAbsoluteFile() + "\n" + backupDB.getAbsoluteFile());
    }

}
