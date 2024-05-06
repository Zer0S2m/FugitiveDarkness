package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectCountLineFilesFiltersImpl;

/**
 * An interface for implementing a class for counting lines of code in a file.
 */
public interface ProjectCountLineFilesFilters {

    /**
     * Set the type of the file object.
     *
     * @param typeFileObject Type of the file object
     */
    void setTypeFileObject(TypeFileObject typeFileObject);

    /**
     * Get the type of the file object.
     *
     * @return Type of the file object
     */
    TypeFileObject getTypeFileObject();

    /**
     * Set the path to the file object.
     *
     * @param path Path to the file object.
     */
    void setPath(String path);

    /**
     * Get the path to the file object.
     *
     * @return Path to the file object.
     */
    String getPath();

    static ProjectCountLineFilesFilters create(String path, TypeFileObject typeFileObject) {
        ProjectCountLineFilesFilters filters = new ProjectCountLineFilesFiltersImpl();

        filters.setPath(path);
        filters.setTypeFileObject(typeFileObject);

        return filters;
    }

}
