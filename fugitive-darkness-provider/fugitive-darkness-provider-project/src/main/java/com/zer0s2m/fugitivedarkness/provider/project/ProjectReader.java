package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectReaderGit;
import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectReaderLocal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An interface for implementing a reading project.
 */
public interface ProjectReader {

    /**
     * Read the entire file structure of the project and return the name of the file objects.
     *
     * @param adapter An adapter for providing information from where to read the source projects.
     *                <p>Supported adapter types:</p>
     *                <ul>
     *                <li>Git repositories.</li>
     *                <li>Local projects.</li>
     *                </ul>
     * @return The name of the file objects.
     */
    Collection<String> read(ProjectReaderAdapterAbstract adapter);

    void setProperties(Map<String, Object> properties);

    /**
     * Create a reader for git projects.
     *
     * @return Reader project.
     */
    static ProjectReader createGit(String group, String project) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("group", group);
        properties.put("project", project);

        ProjectReader reader = new ProjectReaderGit();
        reader.setProperties(properties);
        return reader;
    }

    /**
     * Create a reader for local projects.
     *
     * @return Reader project.
     */
    static ProjectReader createLocal(String path) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("source", path);

        ProjectReader reader = new ProjectReaderLocal();
        reader.setProperties(properties);
        return reader;
    }

}
