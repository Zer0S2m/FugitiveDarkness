package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitJobModel {

    @SuppressWarnings("unused")
    @JsonProperty("id")
    private long id;

    @SuppressWarnings("unused")
    @JsonProperty("created_at")
    private String createdAt;

    @SuppressWarnings("unused")
    @JsonProperty("cron")
    private String cron;

    @SuppressWarnings("unused")
    @JsonProperty("git_repository_id")
    private long gitRepositoryId;

    @SuppressWarnings("unused")
    @JsonProperty("type")
    private String type;

    @SuppressWarnings("unused")
    public GitJobModel() {
    }

    public GitJobModel(String cron, String type, long gitRepositoryId) {
        this.cron = cron;
        this.type = type;
        this.gitRepositoryId = gitRepositoryId;
    }

    public String getCron() {
        return cron;
    }

    public String getType() {
        return type;
    }

    public long getGitRepositoryId() {
        return gitRepositoryId;
    }

}
