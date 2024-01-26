package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatcherNoteModel {

    @SuppressWarnings("unused")
    @JsonProperty("id")
    private long id;

    @SuppressWarnings("unused")
    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("value")
    private String value;

    @JsonProperty("file")
    private String file;

    @JsonProperty("line")
    private String line;

    @JsonProperty("line_number")
    private int lineNumber;

    @JsonProperty("git_repository_id")
    private long gitRepositoryId;

    @SuppressWarnings("unused")
    public MatcherNoteModel() {
    }

    public MatcherNoteModel(String value, String file, String line, int lineNumber, long gitRepositoryId) {
        this.value = value;
        this.file = file;
        this.line = line;
        this.lineNumber = lineNumber;
        this.gitRepositoryId = gitRepositoryId;
    }

    public String getValue() {
        return value;
    }

    public String getFile() {
        return file;
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public long getGitRepositoryId() {
        return gitRepositoryId;
    }

}
