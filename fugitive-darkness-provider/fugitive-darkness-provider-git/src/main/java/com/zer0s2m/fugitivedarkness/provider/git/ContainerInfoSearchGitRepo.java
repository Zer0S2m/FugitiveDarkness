package com.zer0s2m.fugitivedarkness.provider.git;

import java.util.Collection;
import java.util.List;

public record ContainerInfoSearchGitRepo(

        String group,

        String project,

        String pattern,
        
        String link,

        Collection<String> extensionFiles,

        List<ContainerInfoSearchFileGitRepo> found

) { }
