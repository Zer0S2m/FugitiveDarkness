package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import com.zer0s2m.fugitivedarkness.provider.project.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

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
     * Get readers to crawl files.
     *
     * @param properties Additional information for starting the adapter.
     * @param typeFileObject Type of file object.
     * @return Reader.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     * @throws FileNotFoundException                    No file was found.
     */
    @Override
    public Iterable<ContainerInfoReader> getReader(Map<String, Object> properties, TypeFileObject typeFileObject)
            throws ProjectException, IOException {
        if (typeFileObject.equals(TypeFileObject.FILE)) {
            return List.of(new ContainerInfoReader(
                    (String) properties.get("file"),
                    getReader(properties)
            ));
        } else if (typeFileObject.equals(TypeFileObject.DIRECTORY)) {
            Collection<ContainerInfoReader> readers = new ArrayList<>();
            Path sourceProject = Path.of((String) properties.get("source"));
            Path sourceProjectDirectory = Path.of(
                    (String) properties.get("source"), (String) properties.get("file"));

            try (final Stream<Path> stream = Files.walk(sourceProjectDirectory)) {
                List<Path> pathsFiles = stream
                        .filter(file -> !Files.isDirectory(file))
                        .filter(file -> {
                            try (final InputStream inputStream = new FileInputStream(file.toFile())) {
                                return !SearchEngineGitUtils.isBinary(inputStream);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();

                for (Path pathFile : pathsFiles) {
                    Path relativePath = Path.of(pathFile
                            .toString()
                            .replace(sourceProject + "/", ""));

                    properties.put("file", relativePath.toString());

                    readers.add(new ContainerInfoReader(
                            relativePath.toString(),
                            getReader(properties)
                    ));
                }
            }

            return readers;
        }

        return null;
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
