package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectTodoFilters;

/**
 * A filter to search for todos in the project.
 */
public class ProjectTodoFiltersImpl implements ProjectTodoFilters {

    private String file;

    private boolean isFile;

    private boolean isDirectory;

    private boolean isUnpackingGitRepo;

    private boolean isLocalGitRepo;

    private ContainerGitRepoMeta containerGitRepoMeta;

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

    /**
     * Set an indication that the project is local.
     *
     * @param isLocalGitRepo Indicates that the project is local.
     */
    @Override
    public void setIsLocalGitRepo(boolean isLocalGitRepo) {
        this.isLocalGitRepo = isLocalGitRepo;
    }

    /**
     * Get an indication that the project is local.
     *
     * @return Indicates that the project is local.
     */
    @Override
    public boolean getIsLocalGitRepo() {
        return isLocalGitRepo;
    }

    /**
     * Install additional information about the git project.
     *
     * @param containerGitRepoMeta Additional information about the git project.
     */
    @Override
    public void setContainerGitRepoMeta(ContainerGitRepoMeta containerGitRepoMeta) {
        this.containerGitRepoMeta = containerGitRepoMeta;
    }

    /**
     * Get additional information about the git project.
     *
     * @return Additional information about the git project.
     */
    @Override
    public ContainerGitRepoMeta getContainerGitRepoMeta() {
        return containerGitRepoMeta;
    }

}
