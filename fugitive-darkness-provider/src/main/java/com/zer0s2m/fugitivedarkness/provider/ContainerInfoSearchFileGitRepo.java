package com.zer0s2m.fugitivedarkness.provider;

import java.util.List;

public record ContainerInfoSearchFileGitRepo(

        String filename,

        List<ContainerInfoSearchFileMatcherGitRepo> matchers

) { }
