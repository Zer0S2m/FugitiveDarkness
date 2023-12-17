package com.zer0s2m.fugitivedarkness.common.dto;

public record ContainerGitRepoSearch(

        ContainerGitRepoSearchFilters filters,

        String pattern

) {
}
