package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.Path;

public class GitRepoModel {

    @JsonProperty("id")
    private long id;

    @JsonProperty("group_")
    private String group;

    @JsonProperty("project")
    private String project;

    @JsonProperty("host")
    private String host;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("source")
    private String source;

    @JsonProperty("is_load")
    private boolean isLoad;

    public GitRepoModel() {}

    public GitRepoModel(String group, String project, String host, Path source, boolean isLoad) {
        this.group = group;
        this.project = project;
        this.host = host;
        this.source = source.toString();
        this.isLoad = isLoad;
    }

    public GitRepoModel(String group, String project, String host, Path source) {
        this.group = group;
        this.project = project;
        this.host = host;
        this.isLoad = false;
        this.source = source.toString();
    }

    public long getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getProject() {
        return project;
    }

    public String getHost() {
        return host;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean getIsLoad() {
        return isLoad;
    }

    public String getSource() {
        return source;
    }

}
