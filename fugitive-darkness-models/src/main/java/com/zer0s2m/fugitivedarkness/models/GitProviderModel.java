package com.zer0s2m.fugitivedarkness.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitProviderModel {

    @JsonProperty("id")
    private long id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("is_org")
    private boolean isOrg;

    @JsonProperty("is_user")
    private boolean isUser;

    @JsonProperty("target")
    private String target;

    @JsonProperty("created_at")
    private String createdAt;

    public GitProviderModel(String type, boolean isOrg, boolean isUser, String target) {
        this.type = type;
        this.isOrg = isOrg;
        this.isUser = isUser;
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public boolean getIsOrg() {
        return isOrg;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public String getTarget() {
        return target;
    }

}
