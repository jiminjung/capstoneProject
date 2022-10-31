package com.example.firsttest.model;

public class Video {
    private String timestamp;
    private String url;

    public Video(String timestamp, String url) {
        this.timestamp = timestamp;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
