package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocxFileModel {

    @JsonProperty("id")
    private long id;

    @JsonProperty("created_at")
    private String createdAt;

    private String path;

    @JsonProperty("title")
    private String title;

    @JsonProperty("origin_title")
    private String originTitle;

    public DocxFileModel() {
    }

    public DocxFileModel(String path, String title, String originTitle) {
        this.path = path;
        this.title = title;
        this.originTitle = originTitle;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

}
