package com.memorybank.model;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public class Memory {

    private String mValue;
    private long mTimestamp;
    private double mLatitude;
    private double mLongitude;

    public Memory(long timestamp, double latitude, double longitude, String value) {
        mTimestamp = timestamp;
        mLatitude = latitude;
        mLongitude = longitude;
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
