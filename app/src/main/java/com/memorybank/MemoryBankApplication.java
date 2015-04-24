package com.memorybank;

import android.app.Application;

import com.memorybank.database.MemoriesDatabase;
import com.memorybank.managers.MemoryLocationManager;

/**
 * Created by mabus on 4/24/15.
 */
public class MemoryBankApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();
        initLocationManager();

    }

    private void initLocationManager() {
        MemoryLocationManager.init(this);
    }

    private void initDatabase() {
        MemoriesDatabase.initialize(this);
    }
}
