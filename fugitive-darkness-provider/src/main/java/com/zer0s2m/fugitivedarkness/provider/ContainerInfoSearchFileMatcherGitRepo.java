package com.zer0s2m.fugitivedarkness.provider;

import java.util.Set;

public record ContainerInfoSearchFileMatcherGitRepo(

        String matcher,

        String link,

        int lineNumber,

        Set<ContainerInfoSearchFileMatcherGitRepo> previewLast

) { }
