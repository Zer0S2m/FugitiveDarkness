package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.ProjectManager;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReader;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReaderAdapterAbstract;

import java.util.Collection;

public class ProjectManagerImpl implements ProjectManager {

    private ProjectReaderAdapterAbstract adapterReader;

    /**
     * Get all the names of the files in the project.
     *
     * @param reader A reader for a specific type of project.
     * @return The name of the files.
     */
    @Override
    public Collection<String> getAllFilesProject(ProjectReader reader) {
        return reader.read(getAdapterReader());
    }

    @Override
    public void setAdapterReader(ProjectReaderAdapterAbstract adapterReader) {
        this.adapterReader = adapterReader;
    }

    @Override
    public ProjectReaderAdapterAbstract getAdapterReader() {
        return adapterReader;
    }

}
