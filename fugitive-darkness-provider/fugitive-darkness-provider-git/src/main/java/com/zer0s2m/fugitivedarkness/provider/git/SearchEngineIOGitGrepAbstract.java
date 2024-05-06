package com.zer0s2m.fugitivedarkness.provider.git;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Abstract class for initializing basic search parameters.
 */
public abstract class SearchEngineIOGitGrepAbstract extends SearchEngineGrepAbstract implements SearchEngineIOGitGrep {

    private Path source;

    private boolean areaIsFile = false;

    private boolean areaIsDirectory = false;

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     */
    protected SearchEngineIOGitGrepAbstract(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta) {
        super(pattern, source, containerGitRepoMeta);

        setDirectory(source);
    }

    /**
     * Install the root directory of the project where the unpacked git project is located.
     *
     * @param source Source directory.
     */
    @Override
    public void setDirectory(Path source) {
        Objects.requireNonNull(source, "You need to set the path to the git repository");

        this.source = source;
    }

    /**
     * Get the root directory of the project where the unpacked git project is located.
     *
     * @return Source directory.
     */
    @Override
    public Path getDirectory() {
        return source;
    }

    /**
     * Set the indication that the search area is a file.
     *
     * @param areaIsFile Indicates that the search area is a file.
     */
    @Override
    public void setAreaIsFile(boolean areaIsFile) {
        this.areaIsFile = areaIsFile;
    }

    /**
     * Get the indication that the search area is a file.
     *
     * @return Indicates that the search area is a file.
     */
    @Override
    public boolean getAreaIsFile() {
        return areaIsFile;
    }

    /**
     * Get the indication that the search area is a file.
     *
     * @param areaIsDirectory Indicates that the search area is a directory.
     */
    @Override
    public void setAreaIsDirectory(boolean areaIsDirectory) {
        this.areaIsDirectory = areaIsDirectory;
    }

    /**
     * Get the indication that the search area is a file.
     *
     * @return Indicates that the search area is a directory.
     */
    @Override
    public boolean getAreaIsDirectory() {
        return areaIsDirectory;
    }

}
