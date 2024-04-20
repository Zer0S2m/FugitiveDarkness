package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectFileTag {

    @JsonProperty("id")
    private long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("git_repository_id")
    private long gitRepositoryId;

    @JsonProperty("created_at")
    private String createdAt;

    public ProjectFileTag() {
    }

    public ProjectFileTag(String title, long gitRepositoryId) {
        this.title = title;
        this.gitRepositoryId = gitRepositoryId;
    }

}
