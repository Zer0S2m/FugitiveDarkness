package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for providing search in git repositories. Similar to the
 * <a href="https://git-scm.com/docs/git-grep">grep</a> command in git.
 */
class SearchEngineIOGitGrepImpl extends SearchEngineIOGitGrepAbstract implements SearchEngineIOGitGrep {

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     */
    protected SearchEngineIOGitGrepImpl(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta) {
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
    @Override
    public List<ContainerInfoSearchFileGitRepo> callGrep() {
        return null;
    }

}
