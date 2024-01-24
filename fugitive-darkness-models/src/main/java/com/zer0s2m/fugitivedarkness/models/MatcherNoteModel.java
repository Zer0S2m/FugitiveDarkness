package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatcherNoteModel {

    @JsonProperty("id")
    private long id;

    @JsonProperty("value")
    private String value;

    @JsonProperty("file")
    private String file;

    @JsonProperty("line")
    private String line;

    @JsonProperty("lineNumber")
    private int lineNumber;

    @JsonProperty("git_repository_id")
    private long gitRepositoryId;

    public MatcherNoteModel() {
    }

    public MatcherNoteModel(String value, String file, String line, int lineNumber, long gitRepositoryId) {
        this.value = value;
        this.file = file;
        this.line = line;
        this.lineNumber = lineNumber;
        this.gitRepositoryId = gitRepositoryId;
    }

}
