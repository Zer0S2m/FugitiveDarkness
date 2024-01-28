package com.zer0s2m.fugitivedarkness.common.dto;

public record ContainerResponseGitFilter(

        long id,

        String createdAt,

        String title,

        ContainerGitRepoSearch filter

) {
}
