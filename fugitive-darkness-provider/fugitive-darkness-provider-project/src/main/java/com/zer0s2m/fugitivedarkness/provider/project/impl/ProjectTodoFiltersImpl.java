package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.ProjectTodoFilters;

/**
 * A filter to search for todos in the project.
 */
public class ProjectTodoFiltersImpl implements ProjectTodoFilters {

    private String file;

    private boolean isFile;

    private boolean isDirectory;

    private boolean isUnpackingGitRepo;

    /**
     * Set the path to the file object.
     *
     * @param file Path to the file object.
     */
    @Override
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Get the path to the file object.
     *
     * @return Path to the file object.
     */
    @Override
    public String getFile() {
        return file;
    }

    /**
     * Set the indication that the file object is a file.
     *
     * @param isFile Indicates that the file object is a file.
     */
    @Override
    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    /**
     * Get the indication that the file object is a file.
     *
     * @return Indicates that the file object is a file.
     */
    @Override
    public boolean getIsFile() {
        return isFile;
    }

    /**
     * Set the indication that the file object is a directory.
     *
     * @param isDirectory Indicates that the file object is a directory.
     */
    @Override
    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    /**
     * Get the indication that the file object is a directory.
     *
     * @return Indicates that the file object is a directory.
     */
    @Override
    public boolean getIsDirectory() {
        return isDirectory;
    }

    /**
     * Set a sign that the git repository is unpacked.
     *
     * @param isUnpackingGitRepo A sign that the git repository is unpacked.
     */
    @Override
    public void setIsUnpackingGitRepo(boolean isUnpackingGitRepo) {
        this.isUnpackingGitRepo = isUnpackingGitRepo;
    }

    /**
     * Get a sign that the git repository is unpacked.
     *
     * @return A sign that the git repository is unpacked.
     */
    @Override
    public boolean getIsUnpackingGitRepo() {
        return isUnpackingGitRepo;
    }

}
