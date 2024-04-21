package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.FileProject;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectException;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReader;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReaderAdapterAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A reader for local projects.
 */
public class ProjectReaderLocal implements ProjectReader {

    Logger logger = LoggerFactory.getLogger(ProjectReaderLocal.class);

    private Map<String, Object> properties;

    /**
     * Read the entire file structure of the project and return the name of the file objects.
     *
     * @param adapter An adapter for providing information from where to read the source projects.
     *                <p>Supported type - <b>local projects.</b></p>
     * @return The name of the file objects.
     */
    @Override
    public Collection<FileProject> read(ProjectReaderAdapterAbstract adapter) {
        try (Stream<FileProject> streamProjectDirectory = adapter.getStream(properties)) {
            List<FileProject> fileProjects = new java.util.ArrayList<>(streamProjectDirectory.toList());
            fileProjects.remove(0);
            return fileProjects;
        } catch (ProjectException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Set additional information to start the adapter.
     *
     * @param properties Additional information.
     */
    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
