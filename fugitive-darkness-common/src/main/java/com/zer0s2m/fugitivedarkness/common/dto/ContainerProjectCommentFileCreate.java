package com.zer0s2m.fugitivedarkness.common.dto;

public record ContainerProjectCommentFileCreate(

        String text,

        String file,

        long gitRepositoryId

) {
}
