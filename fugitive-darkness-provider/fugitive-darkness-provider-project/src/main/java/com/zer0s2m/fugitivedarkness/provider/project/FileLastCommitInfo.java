package com.zer0s2m.fugitivedarkness.provider.project;

public record FileLastCommitInfo(

        String commit,

        String authorName,

        String authorEmail,

        String date,

        String bodyCommit

) {
}
