package com.zer0s2m.fugitivedarkness.provider.project;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An interface for implementing an adapter for the project reader.
 * <p> Adapters are necessary in order to provide information
 * to readers from where to read information about projects.</p>
 */
public interface ProjectReaderAdapter {

    /**
     * Get a sequence in the form of file names for further iteration.
     *
     * @param properties Additional information for starting the adapter.
     * @return A sequence in the form of file names for further iteration.
     * @throws ProjectException                         The general exception is for interacting with projects.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     * @throws IOException                              If an IO error occurred.
     */
    Stream<String> getStream(Map<String, Object> properties)
            throws ProjectException, IOException;

}
