package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectComment {

    @JsonProperty("id")
    private long id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("file")
    private String file;

    @JsonProperty("git_repository_id")
    private long gitRepositoryId;

    @JsonProperty("created_at")
    private String createdAt;

    public ProjectComment() {}

    public ProjectComment(String file, String text, long gitRepositoryId) {
        this.text = text;
        this.file = file;
        this.gitRepositoryId = gitRepositoryId;
    }

    public String getText() {
        return text;
    }

    public String getFile() {
        return file;
    }

    public long getGitRepositoryId() {
        return gitRepositoryId;
    }

}
