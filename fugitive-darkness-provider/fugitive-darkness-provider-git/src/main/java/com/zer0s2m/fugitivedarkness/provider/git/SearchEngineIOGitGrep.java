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

    /**
     * Set the indication that the search area is a file.
     *
     * @param areaIsFile Indicates that the search area is a file.
     */
    void setAreaIsFile(boolean areaIsFile);

    /**
     * Get the indication that the search area is a file.
     *
     * @return Indicates that the search area is a file.
     */
    boolean getAreaIsFile();

    /**
     * Get the indication that the search area is a file.
     *
     * @param areaIsDirectory Indicates that the search area is a directory.
     */
    void setAreaIsDirectory(boolean areaIsDirectory);

    /**
     * Get the indication that the search area is a file.
     *
     * @return Indicates that the search area is a directory.
     */
    boolean getAreaIsDirectory();

}
