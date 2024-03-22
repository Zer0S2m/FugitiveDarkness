package com.zer0s2m.fugitivedarkness.provider.git;

import java.nio.file.Path;

/**
 * Search engine for <a href="https://git-scm.com/docs/git-grep">git grep</a> command.
 */
public interface SearchEngineIOGitGrep extends SearchEngineGrep {

    /**
     * Install the root directory of the project where the unpacked git project is located.
     *
     * @param source Source directory.
     */
    void setDirectory(Path source);

    /**
     * Get the root directory of the project where the unpacked git project is located.
     *
     * @return Source directory.
     */
    Path getDirectory();

}
