package com.mikaauer.workingtimemeasurement.Export;

import java.util.Date;
import java.util.UUID;

public class DownloadFile {
    private String filepath;
    private String mediaType;
    private UUID token;
    private long createdAt;

    public DownloadFile(String filepath, String mediaType) {
        this.filepath = filepath;
        this.mediaType = mediaType;
        this.createdAt = (new Date()).getTime();
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public UUID getToken() {
        return token;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
