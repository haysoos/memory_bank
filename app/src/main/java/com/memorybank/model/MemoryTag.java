package com.memorybank.model;

/**
 * Created by Jesus Medrano on 4/19/15.
 */
public class MemoryTag {

    public static final int DEFAULT_ID = -1;
    private boolean mIsPrivate;
    private long mTimestamp;
    private String mName;
    private String mDescription;
    private final long mId;

    public MemoryTag(long id, String tagName, String tagDescription, boolean isTagPrivate, long timestamp) {
        mName = tagName;
        mDescription = tagDescription;
        mTimestamp = timestamp;
        mIsPrivate = isTagPrivate;
        mId = id;
    }

    public MemoryTag(String tagName, String tagDescription, boolean isTagPrivate, long timestamp) {
        this(DEFAULT_ID, tagName, tagDescription, isTagPrivate, timestamp);
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

    public long getId() {
        return mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setIsPrivate(boolean isPrivate) {
        mIsPrivate = isPrivate;
    }
}
