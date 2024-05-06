package com.zer0s2m.fugitivedarkness.common.dto;

public record ContainerGitJobCreate(

        long gitRepositoryId,

        String type,

        String cron

) {
}
