package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesReaderAdapter;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesReaderAdapterAbstract;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectException;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectMissingPropertiesAdapterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * An adapter for providing a reader from a source of local projects.
 */
public class ProjectCountLineFilesReaderAdapterLocal extends ProjectCountLineFilesReaderAdapterAbstract
        implements ProjectCountLineFilesReaderAdapter {

    Logger logger = LoggerFactory.getLogger(ProjectCountLineFilesReaderAdapterLocal.class);

    /**
     * Get a reader to crawl the file.
     *
     * @param properties Additional information for starting the adapter.
     * @return Reader.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws FileNotFoundException                    No file was found.
     */
    @Override
    public Reader getReader(Map<String, Object> properties)
            throws ProjectException, FileNotFoundException {
        checkProperties(properties);

        final Path source = Path.of((String) properties.get("source"));
        final Path pathFileObject = Path.of((String) properties.get("file"));

        return new FileReader(
                Path.of(source.toString(), pathFileObject.toString()).toFile());
    }

    /**
     * Check all available information to start the adapter and issue an exception
     * if the necessary information is not provided.
     *
     * @param properties Additional information.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     */
    @Override
    protected void checkProperties(Map<String, Object> properties) throws ProjectMissingPropertiesAdapterException {
        Collection<String> missingProperties = new ArrayList<>();

        if (!properties.containsKey("source")) {
            missingProperties.add("source");
        }
        if (!properties.containsKey("file")) {
            missingProperties.add("file");
        }

        if (!missingProperties.isEmpty()) {
            String mgs = "There are no required parameters to start the adapter " + missingProperties;
            logger.debug(mgs);
            throw new ProjectMissingPropertiesAdapterException(mgs);
        }
    }

}
