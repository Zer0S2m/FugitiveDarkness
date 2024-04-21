package com.zer0s2m.fugitivedarkness.provider.project;

import java.util.Map;

public abstract class ProjectCountLineFilesReaderAdapterAbstract implements ProjectCountLineFilesReaderAdapter {

    /**
     * Check all available information to start the adapter and issue an exception
     * if the necessary information is not provided.
     *
     * @param properties Additional information.
     * @throws ProjectMissingPropertiesAdapterException There are no required parameters to start the adapter.
     */
    abstract protected void checkProperties(Map<String, Object> properties)
            throws ProjectMissingPropertiesAdapterException;

}
