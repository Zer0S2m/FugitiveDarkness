package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zer0s2m.fugitivedarkness.common.mapper.Field;
import com.zer0s2m.fugitivedarkness.common.mapper.FieldObject;

import java.time.LocalDateTime;

public class GitJobModel extends Job<String> implements FieldObject {

    @SuppressWarnings("unused")
    @JsonProperty("id")
    @Field(value = "id")
    private long id;

    @SuppressWarnings("unused")
    @JsonProperty("created_at")
    @Field(value = "created_at")
    private LocalDateTime createdAt;

    @SuppressWarnings("unused")
    @JsonProperty("next_run_at")
    @Field(value = "next_run_at")
    private LocalDateTime nextRunAt;

    @SuppressWarnings("unused")
    @JsonProperty("cron")
    @Field(value = "cron")
    private String cron;

    @SuppressWarnings("unused")
    @JsonProperty("git_repository_id")
    @Field(value = "type")
    private long gitRepositoryId;

    @SuppressWarnings("unused")
    @JsonProperty("type")
    @Field(value = "type")
    private String type;

    @SuppressWarnings("unused")
    @Field(value = "is_local")
    private boolean gitRepoIsLocal;

    @SuppressWarnings("unused")
    @Field(value = "group_")
    private String gitRepoGroup;

    @SuppressWarnings("unused")
    @Field(value = "project")
    private String gitRepoProject;

    @SuppressWarnings("unused")
    public GitJobModel() {
    }

    public GitJobModel(String cron, String type, long gitRepositoryId) {
        this.cron = cron;
        this.type = type;
        this.gitRepositoryId = gitRepositoryId;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public String getType() {
        return type;
    }

    public long getGitRepositoryId() {
        return gitRepositoryId;
    }

    public String getGitRepoGroup() {
        return gitRepoGroup;
    }

    public String getGitRepoProject() {
        return gitRepoProject;
    }

    @Override
    public LocalDateTime getNextRunAt() {
        return nextRunAt;
    }

}
