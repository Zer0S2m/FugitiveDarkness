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
        final AtomicInteger lineNumber = new AtomicInteger(0);
        final AtomicInteger matcherCounterInFile = new AtomicInteger(0);

        try (final BufferedReader buf = new BufferedReader(reader)) {
            for (String line; (line = buf.readLine()) != null; ) {
                lineNumber.set(lineNumber.get() + 1);
                final Matcher matcher = pattern.matcher(line);

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

        return collectPreviewCode(containerInfoSearchFileMatcherGitRepos, file);
    }

    /**
     * Set hints to preview past lines before the main match and next lines after the match
     * if there is no obvious match.
     * <p>Uses the helper class {@link GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode}</p>
     *
     * @param matchers Raw search results.
     * @param file     The name of the file where the search for matches took place.
     * @return Collected search results.
     */
    private List<ContainerInfoSearchFileMatcherGitRepo> collectPreviewCode(
            final List<ContainerInfoSearchFileMatcherGitRepo> matchers,
            final String file) {
        final List<ContainerInfoSearchFileMatcherGitRepo> collectedMatchers = new ArrayList<>();
        final AtomicInteger currentLineNumber = new AtomicInteger(1);

        matchers.forEach(matcher -> {
            ContainerInfoSearchFileMatcherGitRepo collectedMatcher = new ContainerInfoSearchFileMatcherGitRepo(
                    matcher.matcher(),
                    matcher.link(),
                    matcher.lineNumber(),
                    matcher.groups(),
                    matcher.previewLast(),
                    matcher.previewNext());
            currentLineNumber.set(matcher.lineNumber());

            if (currentLineNumber.get() != 1 && utilPreviewCode.getPreviewCodeLast(currentLineNumber.get()) != null) {
                final Set<ContainerInfoSearchFileMatcherGitRepo> previewLastCode = new HashSet<>();

                for (int i = 0; i < contextBeforeReal; i++) {
                    if ((currentLineNumber.get() - i) < 0) {
                        break;
                    }

                    final String previewCodeLineLast = utilPreviewCode.getPreviewCodeLast(
                            currentLineNumber.get() - i);

                    if (previewCodeLineLast != null) {
                        previewLastCode.add(new ContainerInfoSearchFileMatcherGitRepo(
                                previewCodeLineLast,
                                GitRepoUtils.getLinkForMatcherLine(
                                        containerGitRepoMeta,
                                        file,
                                        "master",
                                        utilPreviewCode.getPreviewLineNumberLast(currentLineNumber.get() - i)),
                                utilPreviewCode.getPreviewLineNumberLast(currentLineNumber.get() - i),
                                null,
                                null,
                                null
                        ));
                    }
                }

                collectedMatcher = collectedMatcher.copyAndSetPreviewLast(previewLastCode);
            }

            if (utilPreviewCode.getPreviewCodeNext(currentLineNumber.get()) != null) {
                final Set<ContainerInfoSearchFileMatcherGitRepo> previewNextCode = new HashSet<>();
                for (int i = 0; i < contextAfterReal; i++) {
                    final String previewCodeLineNext = utilPreviewCode.getPreviewCodeNext(
                            currentLineNumber.get() + i);

                    if (previewCodeLineNext != null) {
                        previewNextCode.add(new ContainerInfoSearchFileMatcherGitRepo(
                                previewCodeLineNext,
                                GitRepoUtils.getLinkForMatcherLine(
                                        containerGitRepoMeta,
                                        file,
                                        "master",
                                        utilPreviewCode.getPreviewLineNumberNext(currentLineNumber.get() + i)),
                                utilPreviewCode.getPreviewLineNumberNext(currentLineNumber.get() + i),
                                null,
                                null,
                                null
                        ));
                    }
                }

                collectedMatcher = collectedMatcher.copyAndSetPreviewNext(previewNextCode);
            }

            collectedMatchers.add(collectedMatcher);
        });

        return collectedMatchers;
    }

}
