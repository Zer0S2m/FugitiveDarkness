package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectFileColorSet {

    @JsonProperty("id")
    private long id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("project_file_color_id")
    private long projectFileColorId;

    @JsonProperty("file")
    private String file;

    public ProjectFileColorSet() {
    }

    public ProjectFileColorSet(String file, long projectFileColorId) {
        this.file = file;
        this.projectFileColorId = projectFileColorId;
    }

    public String getFile() {
        return file;
    }

    public long getProjectFileColorId() {
        return projectFileColorId;
    }

}
