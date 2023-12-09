package com.zer0s2m.fugitivedarkness.models;

public class GitRepoModel {

    private int id;

    private String group;

    private String project;

    private String host;

    private boolean isLoad;

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

    public String getGroup() {
        return group;
    }

    public String getProject() {
        return project;
    }

    public String getHost() {
        return host;
    }

}
