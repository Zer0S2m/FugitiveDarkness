package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesFilters;
import com.zer0s2m.fugitivedarkness.provider.project.TypeFileObject;

/**
 * class for counting lines of code in a file.
 */
public class ProjectCountLineFilesFiltersImpl implements ProjectCountLineFilesFilters {

    private TypeFileObject typeFileObject;

    private String path;

    /**
     * Set the type of the file object.
     *
     * @param typeFileObject Type of the file object
     */
    @Override
    public void setTypeFileObject(TypeFileObject typeFileObject) {
        this.typeFileObject = typeFileObject;
    }

    /**
     * Get the type of the file object.
     *
     * @return Type of the file object
     */
    @Override
    public TypeFileObject getTypeFileObject() {
        return typeFileObject;
    }

    /**
     * Set the path to the file object.
     *
     * @param path Path to the file object.
     */
    @Override
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the path to the file object.
     *
     * @return Path to the file object.
     */
    @Override
    public String getPath() {
        return path;
    }

}
