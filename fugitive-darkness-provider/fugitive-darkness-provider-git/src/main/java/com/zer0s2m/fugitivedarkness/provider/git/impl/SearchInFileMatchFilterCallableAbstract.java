package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGrep;

import java.util.regex.Pattern;

abstract class SearchInFileMatchFilterCallableAbstract<T> implements SearchFilterCallable<T> {

    /**
     * Limit number of matches per file.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-countltnumgt">More about</a>.
     */
    protected int maxCount;

    /**
     * Filename.
     */
    protected String file;

    /**
     * the current branch of the git project.
     */
    protected String currentBranch;

    /**
     * Additional information about the git repository.
     */
    protected ContainerGitRepoMeta containerGitRepoMeta;

    /**
     * Matcher counter in one file.
     * <p>Required to set a specific parameter {@link SearchEngineGrep#getMaxCount}.</p>
     */
    protected boolean isUseMatcherCounterInFile;

    /**
     * A pattern for finding matches in files.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---basic-regexp">More about</a>.
     */
    protected Pattern pattern;

    protected int contextBeforeReal;

    protected int contextAfterReal;

    @Override
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
    }

    @Override
    public void setContainerGitRepoMeta(ContainerGitRepoMeta containerGitRepoMeta) {
        this.containerGitRepoMeta = containerGitRepoMeta;
    }

    @Override
    public void setIsUseMatcherCounterInFile(boolean isUseMatcherCounterInFile) {
        this.isUseMatcherCounterInFile = isUseMatcherCounterInFile;
    }

    @Override
    public void setContextBeforeReal(int contextBeforeReal) {
        this.contextBeforeReal = contextBeforeReal;
    }

    @Override
    public void setContextAfterReal(int contextAfterReal) {
        this.contextAfterReal = contextAfterReal;
    }

}
