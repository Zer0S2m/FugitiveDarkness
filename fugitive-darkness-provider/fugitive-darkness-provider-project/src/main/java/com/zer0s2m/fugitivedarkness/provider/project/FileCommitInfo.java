package com.zer0s2m.fugitivedarkness.provider.project;

public record FileCommitInfo(

        String commit,

        String authorName,

        String authorEmail,

        String date,

        String bodyCommit

) {
}
