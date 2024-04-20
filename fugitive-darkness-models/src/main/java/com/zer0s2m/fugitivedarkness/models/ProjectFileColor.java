package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectFileColor {

    @JsonProperty("id")
    private long id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("color")
    private String color;

    @JsonProperty("git_repository_id")
    private long gitRepositoryId;

    public ProjectFileColor() {
    }

    public ProjectFileColor(String color, long gitRepositoryId) {
        this.color = color;
        this.gitRepositoryId = gitRepositoryId;
    }

    public String getColor() {
        return color;
    }

    public long getGitRepositoryId() {
        return gitRepositoryId;
    }

}
