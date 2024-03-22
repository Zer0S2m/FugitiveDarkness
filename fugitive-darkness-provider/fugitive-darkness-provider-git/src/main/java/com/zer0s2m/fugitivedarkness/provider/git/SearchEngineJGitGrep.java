package com.zer0s2m.fugitivedarkness.provider.git;

import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Search engine for <a href="https://git-scm.com/docs/git-grep">git grep</a> command.
 */
public interface SearchEngineJGitGrep extends SearchEngineGrep {

    /**
     * Install the git repository via its source path.
     *
     * @param source Source path of the repository.
     * @throws IOException If an IO error occurred.
     */
    void setGitRepositoryGrep(Path source) throws IOException;

    /**
     * Get the git repository.
     *
     * @return Git repository.
     */
    Repository getGitRepositoryGrep();

}
