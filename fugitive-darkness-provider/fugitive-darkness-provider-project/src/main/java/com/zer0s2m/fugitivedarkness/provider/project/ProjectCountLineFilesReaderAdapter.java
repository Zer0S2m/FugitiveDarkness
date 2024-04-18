package com.zer0s2m.fugitivedarkness.provider.project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * An interface for implementing a service delivery adapter
 * for a reader {@link ProjectCountLineFilesReader} of the number of lines in a file.
 * <p>Adapters are needed to provide readers with the necessary data to process from different sources.</p>
 */
public interface ProjectCountLineFilesReaderAdapter {

    /**
     * Get a reader to crawl the file.
     *
     * @param properties Additional information for starting the adapter.
     * @return Reader.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     * @throws FileNotFoundException                    No file was found.
     */
    Reader getReader(Map<String, Object> properties) throws ProjectException, IOException;

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
    Iterable<ContainerInfoReader> getReader(Map<String, Object> properties, TypeFileObject typeFileObject)
            throws ProjectException, IOException;

}
