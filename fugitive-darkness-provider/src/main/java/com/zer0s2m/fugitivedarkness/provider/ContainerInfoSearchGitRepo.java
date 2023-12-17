package com.zer0s2m.fugitivedarkness.provider;

import java.util.List;

public record ContainerInfoSearchGitRepo(

        String group,

        String project,

        String pattern,

        List<ContainerInfoSearchFileGitRepo> found

) { }
