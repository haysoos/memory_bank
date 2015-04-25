package com.memorybank.model;

/**
 * Created by Jesus Medrano on 4/12/15.
 */
public class Memory {

    public static final int DEFAULT_ID = -1;
    private final long mId;
    private String mValue;
    private long mTimestamp;
    private double mLatitude;
    private double mLongitude;

    public Memory(long timestamp, double latitude, double longitude, String value) {
        this(DEFAULT_ID, timestamp, latitude, longitude, value);
    }

    public Memory(long id, long timestamp, double latitude, double longitude, String value) {
        mId = id;
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

    public long getId() {
        return mId;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
