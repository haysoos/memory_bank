package com.memorybank.model;

/**
 * Created by mabus on 4/19/15.
 */
public class MemoryTag {

    private final boolean mIsPrivate;
    private long mTimestamp;
    private String mName;
    private String mDescription;

    public MemoryTag(String tagName, String tagDescription, boolean isTagPrivate, long timestamp) {
        mName = tagName;
        mDescription = tagDescription;
        mTimestamp = timestamp;
        mIsPrivate = isTagPrivate;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isPrivate() {
        return mIsPrivate;
    }
}
