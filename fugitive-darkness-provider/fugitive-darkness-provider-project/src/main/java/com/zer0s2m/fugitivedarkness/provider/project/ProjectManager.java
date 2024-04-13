package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectManagerImpl;
import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectReaderAdapterGit;
import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectReaderAdapterLocal;

import java.util.Collection;

/**
 * An interface for implementing a class for project maintenance in the form of:
 * <ul>
 *     <li>Git repositories.</li>
 *     <li>Local projects</li>
 * </ul>
 */
public interface ProjectManager {

    /**
     * Get all the names of the files in the project.
     *
     * @param reader A reader for a specific type of project.
     * @return The name of the files.
     */
    Collection<String> getAllFilesProject(ProjectReader reader);

    void setAdapterReader(ProjectReaderAdapterAbstract adapterReader);

    ProjectReaderAdapterAbstract getAdapterReader();

    static ProjectManager create() {
        return new ProjectManagerImpl();
    }

    /**
     * Create a manager to maintain projects from git repositories.
     *
     * @return Project manager.
     */
    static ProjectManager createGit() {
        ProjectManager projectManager = create();
        projectManager.setAdapterReader(new ProjectReaderAdapterGit());
        return projectManager;
    }

    /**
     * Create a manager to serve local projects.
     *
     * @return Project manager.
     */
    static ProjectManager createLocal() {
        ProjectManager projectManager = create();
        projectManager.setAdapterReader(new ProjectReaderAdapterLocal());
        return projectManager;
    }

}
