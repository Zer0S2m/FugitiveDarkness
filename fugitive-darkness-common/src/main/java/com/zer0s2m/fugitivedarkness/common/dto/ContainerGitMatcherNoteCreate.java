package com.zer0s2m.fugitivedarkness.common.dto;

public record ContainerGitMatcherNoteCreate(

        String value,

        String file,

        String line,

        int lineNumber,

        long gitRepositoryId

) {
}
