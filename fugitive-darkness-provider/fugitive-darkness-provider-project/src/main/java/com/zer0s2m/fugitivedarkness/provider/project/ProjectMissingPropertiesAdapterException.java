package com.zer0s2m.fugitivedarkness.provider.project;

/**
 * Exception for missing parameters to start the adapter {@link ProjectReaderAdapter}.
 */
public class ProjectMissingPropertiesAdapterException extends ProjectException {

    public ProjectMissingPropertiesAdapterException(String message) {
        super(message);
    }

}
