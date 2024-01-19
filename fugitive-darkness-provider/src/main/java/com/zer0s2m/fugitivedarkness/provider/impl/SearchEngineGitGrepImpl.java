package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Service for providing search in git repositories. Similar to the
 * <a href="https://git-scm.com/docs/git-grep">grep</a> command in git.
 */
class SearchEngineGitGrepImpl extends SearchEngineGitGrepAbstract implements SearchEngineGitGrep {

    /**
     * Helper for finding past lines of code.
     */
    private final GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode utilPreviewCode =
            new GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode();

    /**
     * Matcher counter in one file.
     * <p>Required to set a specific parameter {@link SearchEngineGitGrep#getMaxCount}.</p>
     */
    private boolean isUseMatcherCounterInFile = false;

    /**
     * Whether to use maximum search depth.
     * <p>Required to set a specific parameter {@link SearchEngineGitGrep#getMaxDepth()}.</p>
     */
    private boolean isUseMaxDepth = false;

    private int contextBeforeReal = 1;

    private int contextAfterReal = 1;

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     * @throws IOException If an IO error occurred.
     */
    public SearchEngineGitGrepImpl(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta)
            throws IOException {
        super(pattern, source, containerGitRepoMeta);
    }

    /**
     * Start search.
     * <p>The search is based on the following criteria:</p>
     * <ul>
     *     <li>Including files in the search that have the specified
     *     extensions {@link SearchEngineGitGrep#getIncludeExtensionFilesForSearchGrep}.</li>
     *     <li>Excluding files from the search that have the specified
     *     extensions {@link SearchEngineGitGrep#getExcludeExtensionFilesForSearchGrep}.</li>
     *     <li>Match pattern {@link SearchEngineGitGrep#getPattern}.</li>
     *     <li>Include files by pattern {@link SearchEngineGitGrep#getPatternForIncludeFile} in the search.</li>
     *     <li>Exclude files from the search by pattern {@link SearchEngineGitGrep#getPatternForExcludeFile}.</li>
     *     <li>Maximum search depth {@link SearchEngineGitGrep#getMaxDepth()}.</li>
     *     <li>Maximum number of matches in one file {@link SearchEngineGitGrep#getMaxCount()}.</li>
     * </ul>
     *
     * @return Search results.
     * @throws IOException IO exception.
     */
    public List<ContainerInfoSearchFileGitRepo> callGrep() throws IOException {
        if (getMaxCount() != -1) {
            isUseMatcherCounterInFile = true;
        }
        if (getMaxDepth() != -1) {
            isUseMaxDepth = true;
        }
        if (getContextBefore() != -1) {
            contextBeforeReal = getContextBefore();
        }
        if (getContextAfter() != -1) {
            contextAfterReal = getContextAfter();
        }

        try (final ObjectReader objectReader = getGitRepositoryGrep().newObjectReader()) {
            return grep(objectReader, getGitRepositoryGrep());
        }
    }

    private List<ContainerInfoSearchFileGitRepo> grep(
            final ObjectReader objectReader, final Repository repository) throws IOException {
        final List<ContainerInfoSearchFileGitRepo> infoSearchFileGitRepos = new ArrayList<>();
        ObjectId commitId = SearchEngineGitUtils.getRevision(repository);

        try (final RevWalk revWalk = new RevWalk(objectReader)) {
            try (final TreeWalk treeWalk = new TreeWalk(objectReader)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                CanonicalTreeParser treeParser = new CanonicalTreeParser();
                treeParser.reset(objectReader, commit.getTree());

                int treeIndex = treeWalk.addTree(treeParser);
                treeWalk.setRecursive(true);

                while (treeWalk.next()) {
                    AbstractTreeIterator it = treeWalk.getTree(treeIndex, AbstractTreeIterator.class);
                    ObjectId objectId = it.getEntryObjectId();
                    ObjectLoader objectLoader = objectReader.open(objectId);

                    if (!isBinary(objectLoader.openStream())) {
                        final String extensionFile = FileSystemUtils
                                .getExtensionFromRawStrFile(it.getEntryPathString());

                        if (isUseMaxDepth && !whetherSourceMatchesMaximumDepth(it.getEntryPathString())) {
                            continue;
                        }

                        if ((getWhetherSearchByExcludeFileExtension(extensionFile)) ||
                                (!getWhetherSearchByIncludeFileExtension(extensionFile))
                        ) {
                            continue;
                        }

                        if ((getWhetherSearchByExcludeFileByPattern(it.getEntryPathString())) ||
                                (!getWhetherSearchByIncludeFileByPattern(it.getEntryPathString()))) {
                            continue;
                        }

                        List<ContainerInfoSearchFileMatcherGitRepo> matchers = getMatchedLines(
                                objectLoader.openStream(), it.getEntryPathString());
                        matchers = collectPreviewCode(matchers, it.getEntryPathString());

                        if (!matchers.isEmpty()) {
                            infoSearchFileGitRepos.add(new ContainerInfoSearchFileGitRepo(
                                    it.getEntryPathString(),
                                    extensionFile,
                                    GitRepoUtils.getLinkForFile(
                                            getContainerGitRepoMeta(),
                                            it.getEntryPathString(),
                                            getGitRepositoryGrep().getBranch()),
                                    matchers));

                            addExtensionFilesGrep(extensionFile);
                        }

                        utilPreviewCode.clearPreviewCodes();
                    }
                }
            }
        }

        return infoSearchFileGitRepos;
    }

    private List<ContainerInfoSearchFileMatcherGitRepo> getMatchedLines(
            InputStream stream,
            final String file) throws IOException {
        final List<ContainerInfoSearchFileMatcherGitRepo> matchers = new ArrayList<>();
        final AtomicInteger lineNumber = new AtomicInteger(1);
        final AtomicInteger matcherCounterInFile = new AtomicInteger(0);

        try (final InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            try (final BufferedReader buf = new BufferedReader(reader)) {
                String line;

                while ((line = buf.readLine()) != null) {
                    if (getPattern().matcher(line).find()) {
                        if (isUseMatcherCounterInFile && matcherCounterInFile.get() == getMaxCount()) {
                            break;
                        }

                        matchers.add(new ContainerInfoSearchFileMatcherGitRepo(
                                line,
                                GitRepoUtils.getLinkForMatcherLine(
                                        getContainerGitRepoMeta(),
                                        file,
                                        getGitRepositoryGrep().getBranch(),
                                        lineNumber.get()),
                                lineNumber.get(),
                                null,
                                null
                        ));

                        if (isUseMatcherCounterInFile) {
                            matcherCounterInFile.set(matcherCounterInFile.get() + 1);
                        }
                    } else {
                        utilPreviewCode.addPreviewCodes(lineNumber.get(), line);
                    }

                    lineNumber.set(lineNumber.get() + 1);
                }
            }
        }

        return matchers;
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
                    matcher.previewLast(),
                    matcher.previewNext());
            currentLineNumber.set(matcher.lineNumber());

            if (currentLineNumber.get() != 1 && utilPreviewCode.getPreviewCodeLast(currentLineNumber.get()) != null) {
                final Set<ContainerInfoSearchFileMatcherGitRepo> previewLastCode = new HashSet<>();

                try {
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
                                            getContainerGitRepoMeta(),
                                            file,
                                            getGitRepositoryGrep().getBranch(),
                                            utilPreviewCode.getPreviewLineNumberLast(currentLineNumber.get() - i)),
                                    utilPreviewCode.getPreviewLineNumberLast(currentLineNumber.get() - i),
                                    null,
                                    null
                            ));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                collectedMatcher = collectedMatcher.copyAndSetPreviewLast(previewLastCode);
            }

            if (utilPreviewCode.getPreviewCodeNext(currentLineNumber.get()) != null) {
                final Set<ContainerInfoSearchFileMatcherGitRepo> previewNextCode = new HashSet<>();
                try {
                    for (int i = 0; i < contextAfterReal; i++) {
                        final String previewCodeLineNext = utilPreviewCode.getPreviewCodeNext(
                                currentLineNumber.get() + i);

                        if (previewCodeLineNext != null) {
                            previewNextCode.add(new ContainerInfoSearchFileMatcherGitRepo(
                                    previewCodeLineNext,
                                    GitRepoUtils.getLinkForMatcherLine(
                                            getContainerGitRepoMeta(),
                                            file,
                                            getGitRepositoryGrep().getBranch(),
                                            utilPreviewCode.getPreviewLineNumberNext(currentLineNumber.get() + i)),
                                    utilPreviewCode.getPreviewLineNumberNext(currentLineNumber.get() + i),
                                    null,
                                    null
                            ));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                collectedMatcher = collectedMatcher.copyAndSetPreviewNext(previewNextCode);
            }

            collectedMatchers.add(collectedMatcher);
        });

        return collectedMatchers;
    }

}
