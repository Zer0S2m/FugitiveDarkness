package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.*;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileMatcherGitRepo;
import com.zer0s2m.fugitivedarkness.provider.git.FileSystemUtils;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
class SearchEngineJGitGrepImpl extends SearchEngineJGitGrepAbstract implements SearchEngineJGitGrep {

    Logger logger = LoggerFactory.getLogger(SearchEngineJGitGrepImpl.class);

    /**
     * Helper for finding past lines of code.
     */
    private final GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode utilPreviewCode =
            new GitRepoCommandGrepUtils.GitRepoCommandGrepUtilPreviewCode();

    /**
     * Matcher counter in one file.
     * <p>Required to set a specific parameter {@link SearchEngineGrep#getMaxCount}.</p>
     */
    private boolean isUseMatcherCounterInFile = false;

    /**
     * Whether to use maximum search depth.
     * <p>Required to set a specific parameter {@link SearchEngineGrep#getMaxDepth()}.</p>
     */
    private boolean isUseMaxDepth = false;

    private int contextBeforeReal = 1;

    private int contextAfterReal = 1;

    /**
     * Count files in project.
     */
    private final AtomicInteger countFiles = new AtomicInteger(0);

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     * @throws IOException If an IO error occurred.
     */
    public SearchEngineJGitGrepImpl(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta)
            throws IOException {
        super(pattern, source, containerGitRepoMeta);
    }

    /**
     * Start search.
     * <p>The search is based on the following criteria:</p>
     * <ul>
     *     <li>Including files in the search that have the specified
     *     extensions {@link SearchEngineGrep#getIncludeExtensionFilesForSearchGrep}.</li>
     *     <li>Excluding files from the search that have the specified
     *     extensions {@link SearchEngineGrep#getExcludeExtensionFilesForSearchGrep}.</li>
     *     <li>Match pattern {@link SearchEngineGrep#getPattern}.</li>
     *     <li>Include files by pattern {@link SearchEngineGrep#getPatternForIncludeFile} in the search.</li>
     *     <li>Exclude files from the search by pattern {@link SearchEngineGrep#getPatternForExcludeFile}.</li>
     *     <li>Maximum search depth {@link SearchEngineGrep#getMaxDepth()}.</li>
     *     <li>Maximum number of matches in one file {@link SearchEngineGrep#getMaxCount()}.</li>
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

        if (commitId == null) {
            return infoSearchFileGitRepos;
        }

        try (final RevWalk revWalk = new RevWalk(objectReader)) {
            try (final TreeWalk treeWalk = new TreeWalk(objectReader)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                CanonicalTreeParser treeParser = new CanonicalTreeParser();
                treeParser.reset(objectReader, commit.getTree());

                int treeIndex = treeWalk.addTree(treeParser);
                treeWalk.setRecursive(true);
                if (getAreaFile() != null) {
                    treeWalk.setFilter(PathFilter.create(getAreaFile()));
                }

                while (treeWalk.next()) {
                    countFiles.set(countFiles.get() + 1);
                    AbstractTreeIterator it = treeWalk.getTree(treeIndex, AbstractTreeIterator.class);
                    ObjectId objectId = it.getEntryObjectId();
                    ObjectLoader objectLoader;

                    try {
                        objectLoader = objectReader.open(objectId);
                    } catch (MissingObjectException exception) {
                        logger.error(exception.getMessage());
                        continue;
                    }

                    if (!SearchEngineGitUtils.isBinary(objectLoader.openStream())) {
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

                        final List<ContainerInfoSearchFileMatcherGitRepo> matchers = getMatchedLines(
                                objectLoader.openStream(), it.getEntryPathString());

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
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }

        return infoSearchFileGitRepos;
    }

    private List<ContainerInfoSearchFileMatcherGitRepo> getMatchedLines(
            InputStream stream,
            final String file) {
        final List<ContainerInfoSearchFileMatcherGitRepo> matchers;

        try (final InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            final SearchInFileMatchFilterCallableAbstract<List<ContainerInfoSearchFileMatcherGitRepo>> searchInFileMatchCallable =
                    new SearchInFileMatchCallable(reader);

            searchInFileMatchCallable.setContainerGitRepoMeta(getContainerGitRepoMeta());
            searchInFileMatchCallable.setFile(file);
            searchInFileMatchCallable.setCurrentBranch(getGitRepositoryGrep().getBranch());
            searchInFileMatchCallable.setIsUseMatcherCounterInFile(isUseMatcherCounterInFile);
            searchInFileMatchCallable.setMaxCount(getMaxCount());
            searchInFileMatchCallable.setPattern(getPattern());
            searchInFileMatchCallable.setContextBeforeReal(contextBeforeReal);
            searchInFileMatchCallable.setContextAfterReal(contextAfterReal);

            matchers = new ArrayList<>(searchInFileMatchCallable.call());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return matchers;
    }

    /**
     * Get the number of files in the project.
     *
     * @return Count files.
     */
    @Override
    public int getCountFiles() {
        final int countFiles_ = countFiles.get();
        countFiles.set(0);
        return countFiles_;
    }

    /**
     * Get the average file processing time.
     *
     * @return Average file processing time.
     */
    @Override
    public long getAverageFileProcessingTime() {
        final long totalProcessingTimeFile = StateEngineIOGitStatistics.TOTAL_PROCESSING_FILE.get();
        StateEngineIOGitStatistics.TOTAL_PROCESSING_FILE.set(0);
        if (totalProcessingTimeFile > 0) {
            return countFiles.get() / totalProcessingTimeFile;
        } else {
            return 0;
        }
    }

}
