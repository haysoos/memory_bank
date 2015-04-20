package com.memorybank.managers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Jesus Medrano on 4/13/15.
 */
public class MemoryLocationManager {

    public static final String TAG = "MemoryLocationManager";
    public static final int GPS_EXPIRATION_THRESHOLD = 1000 * 60 * 5;
    public static final int MIN_INTERVAL_GPS_UPDATES = 1000 * 60 * 1;
    private static LocationManager mLocationManager;
    private static Location mLocation;
    private static MemoryLocationManager instance = new MemoryLocationManager();
    private static LocationListener mLocationListener;

    public static void init(Context context) {
        Context appContext = context.getApplicationContext();

        mLocationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        createLocationListener();

        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (mLocation == null || mLocation.getTime() > networkLocation.getTime()) {
            mLocation = networkLocation;
        }

    }

    private static void createLocationListener() {
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                mLocationManager.removeUpdates(mLocationListener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    public static MemoryLocationManager getInstance() {
        return instance;
    }

    private MemoryLocationManager() {}

    public Location getLocation() {
        return mLocation;
    }

    public void stopListeningForUpdates() {
        mLocationManager.removeUpdates(mLocationListener);
    }

    public void startListeningForUpdates() {
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, MIN_INTERVAL_GPS_UPDATES, 10, mLocationListener);
    }

}
