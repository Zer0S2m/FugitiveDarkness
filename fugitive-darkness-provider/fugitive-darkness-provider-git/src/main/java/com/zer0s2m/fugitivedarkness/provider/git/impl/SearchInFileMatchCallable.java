package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.*;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

class SearchInFileMatchCallable extends SearchInFileMatchFilterCallableAbstract<List<ContainerInfoSearchFileMatcherGitRepo>> {

    /**
     * Helper for finding past lines of code.
     */
    private final GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode utilPreviewCode =
            new GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode();

    private final Reader reader;

    public SearchInFileMatchCallable(Reader reader) {
        this.reader = reader;
    }

    @Override
    public List<ContainerInfoSearchFileMatcherGitRepo> call() throws Exception {
        final List<ContainerInfoSearchFileMatcherGitRepo> containerInfoSearchFileMatcherGitRepos = new ArrayList<>();
        final AtomicInteger lineNumber = new AtomicInteger(1);
        final AtomicInteger matcherCounterInFile = new AtomicInteger(0);

        try (final BufferedReader buf = new BufferedReader(reader)) {
            for (String line; (line = buf.readLine()) != null; ) {
                lineNumber.set(lineNumber.get() + 1);

                Matcher matcher = pattern.matcher(line);

                Set<ContainerInfoSearchFileMatcherGroupGitRepo> searchFileMatcherGroupGitRepos = new HashSet<>();

                while (matcher.find()) {
                    if (isUseMatcherCounterInFile && matcherCounterInFile.get() == maxCount) {
                        break;
                    }

                    final ContainerInfoSearchFileMatcherGroupGitRepo fileMatcherGroupGitRepo =
                            new ContainerInfoSearchFileMatcherGroupGitRepo(
                                    matcher.group(),
                                    matcher.start(),
                                    matcher.end());
                    searchFileMatcherGroupGitRepos.add(fileMatcherGroupGitRepo);

                    if (isUseMatcherCounterInFile) {
                        matcherCounterInFile.set(matcherCounterInFile.get() + 1);
                    }
                }

                if (!searchFileMatcherGroupGitRepos.isEmpty()) {
                    containerInfoSearchFileMatcherGitRepos.add(new ContainerInfoSearchFileMatcherGitRepo(
                            line,
                            GitRepoUtils.getLinkForMatcherLine(
                                    containerGitRepoMeta,
                                    file,
                                    currentBranch,
                                    lineNumber.get()),
                            lineNumber.get(),
                            searchFileMatcherGroupGitRepos,
                            null,
                            null
                    ));
                } else {
                    utilPreviewCode.addPreviewCodes(lineNumber.get(), line);
                }
            }
        }

        return containerInfoSearchFileMatcherGitRepos;
    }

}
