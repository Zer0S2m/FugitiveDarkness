package com.zer0s2m.fugitivedarkness.common.dto;

import java.util.List;

public record ContainerGitRepoSearchFilters(

        List<ContainerGitRepoControl> git,

        List<String> includeExtensionFiles,

        List<String> excludeExtensionFiles,

        String patternForIncludeFile,

        String patternForExcludeFile

) {
}
