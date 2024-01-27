package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitFilterModel {

    @JsonProperty("id")
    private long id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("title")
    private String title;

    @JsonProperty("filter")
    private String filter;

    public GitFilterModel() {
    }

    public GitFilterModel(String title, String filter) {
        this.title = title;
        this.filter = filter;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getFilter() {
        return filter;
    }

}
