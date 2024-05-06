package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectFileTagSet {

    @JsonProperty("id")
    private long id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("project_file_tag_id")
    private long projectFileTagId;

    @JsonProperty("file")
    private String file;

    public ProjectFileTagSet() {
    }

    public ProjectFileTagSet(String file, long projectFileTagId) {
        this.file = file;
        this.projectFileTagId = projectFileTagId;
    }

    public String getFile() {
        return file;
    }

    public long getProjectFileTagId() {
        return projectFileTagId;
    }

}
