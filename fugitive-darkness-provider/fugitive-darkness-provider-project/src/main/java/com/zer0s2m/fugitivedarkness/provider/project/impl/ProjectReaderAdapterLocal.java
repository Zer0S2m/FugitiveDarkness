package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An adapter for providing information from a local project.
 */
public class ProjectReaderAdapterLocal extends ProjectReaderAdapterAbstract implements ProjectReaderAdapter {

    Logger logger = LoggerFactory.getLogger(ProjectReaderAdapterLocal.class);

    /**
     * Get the sequence in the form of information about file objects for further iteration.
     *
     * @param properties Additional information for starting the adapter.
     * @return A sequence in the form of information about file objects for further iteration.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     */
    @Override
    @SuppressWarnings("resource")
    public Stream<FileProject> getStream(Map<String, Object> properties) throws ProjectException, IOException {
        checkProperties(properties);

        final Path source = Path.of((String) properties.get("source"));

        Stream<Path> walkDirectory = Files.walk(source);
        return walkDirectory
                .map(path -> new FileProject(
                        path.toString(),
                        !Files.isDirectory(path),
                        Files.isDirectory(path)
                ))
                .map(fileProject -> fileProject.copy(
                        fileProject.path().replace(
                                source + "/",
                                "")
                ));
    }

    @Override
    protected void checkProperties(Map<String, Object> properties) throws ProjectMissingPropertiesAdapterException {
        if (!properties.containsKey("source")) {
            String msg = "The required parameter for starting the adapter is missing [source]";
            logger.debug(msg);
            throw new ProjectMissingPropertiesAdapterException(msg);
        }
    }

}