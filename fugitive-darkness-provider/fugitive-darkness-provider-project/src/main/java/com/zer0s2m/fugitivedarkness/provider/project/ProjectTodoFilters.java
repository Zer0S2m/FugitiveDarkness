package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectTodoFiltersImpl;

/**
 * A filter to search for todos in the project.
 */
public interface ProjectTodoFilters {

    /**
     * Set the path to the file object.
     *
     * @param file Path to the file object.
     */
    void setFile(String file);

    /**
     * Get the path to the file object.
     *
     * @return Path to the file object.
     */
    String getFile();

    /**
     * Set the indication that the file object is a file.
     *
     * @param isFile Indicates that the file object is a file.
     */
    void setIsFile(boolean isFile);

    /**
     * Get the indication that the file object is a file.
     *
     * @return Indicates that the file object is a file.
     */
    boolean getIsFile();

    /**
     * Set the indication that the file object is a directory.
     *
     * @param isDirectory Indicates that the file object is a directory.
     */
    void setIsDirectory(boolean isDirectory);

    /**
     * Get the indication that the file object is a directory.
     *
     * @return Indicates that the file object is a directory.
     */
    boolean getIsDirectory();

    /**
     * Set a sign that the git repository is unpacked.
     *
     * @param isUnpackingGitRepo A sign that the git repository is unpacked.
     */
    void setIsUnpackingGitRepo(boolean isUnpackingGitRepo);

    /**
     * Get a sign that the git repository is unpacked.
     *
     * @return A sign that the git repository is unpacked.
     */
    boolean getIsUnpackingGitRepo();

    static ProjectTodoFilters create() {
        return new ProjectTodoFiltersImpl();
    }

    static ProjectTodoFilters create(
            String file,
            boolean isFile,
            boolean isDirectory,
            boolean isUnpackingGitRepo) {
        final ProjectTodoFilters filters = create();

        filters.setFile(file);
        filters.setIsFile(isFile);
        filters.setIsDirectory(isDirectory);
        filters.setIsUnpackingGitRepo(isUnpackingGitRepo);

        return filters;
    }

}
