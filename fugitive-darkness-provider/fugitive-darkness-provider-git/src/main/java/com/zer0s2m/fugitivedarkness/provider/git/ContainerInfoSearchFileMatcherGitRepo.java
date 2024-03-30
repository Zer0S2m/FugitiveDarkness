package com.zer0s2m.fugitivedarkness.provider.git;

import java.util.Set;

public record ContainerInfoSearchFileMatcherGitRepo(

        String matcher,

        String link,

        int lineNumber,

        Set<ContainerInfoSearchFileMatcherGroupGitRepo> groups,

        Set<ContainerInfoSearchFileMatcherGitRepo> previewLast,

        Set<ContainerInfoSearchFileMatcherGitRepo> previewNext

) {

    public ContainerInfoSearchFileMatcherGitRepo copyAndSetPreviewLast(
            final Set<ContainerInfoSearchFileMatcherGitRepo> previewLast) {
        return new ContainerInfoSearchFileMatcherGitRepo(
                matcher,
                link,
                lineNumber,
                groups,
                previewLast,
                previewNext
        );
    }

    public ContainerInfoSearchFileMatcherGitRepo copyAndSetPreviewNext(
            final Set<ContainerInfoSearchFileMatcherGitRepo> previewNext) {
        return new ContainerInfoSearchFileMatcherGitRepo(
                matcher,
                link,
                lineNumber,
                groups,
                previewLast,
                previewNext
        );
    }

}
