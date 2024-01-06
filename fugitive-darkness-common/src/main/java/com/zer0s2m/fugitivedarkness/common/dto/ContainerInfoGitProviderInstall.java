package com.zer0s2m.fugitivedarkness.common.dto;

public record ContainerInfoGitProviderInstall(

        String type,

        boolean isOrg,

        boolean isUser,

        String target

) {
}
