package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("is_load")
    private boolean isLoad;

    public GitRepoModel() {}

    public GitRepoModel(String group, String project, String host, boolean isLoad) {
        this.group = group;
        this.project = project;
        this.host = host;
        this.isLoad = isLoad;
    }

    public GitRepoModel(String group, String project, String host) {
        this.group = group;
        this.project = project;
        this.host = host;
        this.isLoad = false;
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

}
