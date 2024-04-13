package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.ProjectException;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReader;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReaderAdapterAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A reader for projects from git repositories.
 */
public class ProjectReaderGit implements ProjectReader {

    Logger logger = LoggerFactory.getLogger(ProjectReaderGit.class);

    private Map<String, Object> properties;

    /**
     * Read the entire file structure of the project and return the name of the file objects.
     *
     * @param adapter An adapter for providing information from where to read the source projects.
     *                <p>Supported adapter type - <b>git repository</b></p>
     * @return The name of the file objects.
     */
    @Override
    public Collection<String> read(ProjectReaderAdapterAbstract adapter) {
        try (Stream<String> streamProjectDirectory = adapter.getStream(properties)) {
            return streamProjectDirectory.toList();
        } catch (ProjectException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
