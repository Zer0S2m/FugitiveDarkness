package com.zer0s2m.fugitivedarkness.provider.git;

public record ContainerGitRepoMeta(

        String group,

        String project,

        String host

) {

    public String getLink(final boolean isUseGit) {
        String link = "https://" + host + "/" + group + "/" + project;

        if (isUseGit) {
            return link + ".git";
        } else {
            return link;
        }
    }

}
