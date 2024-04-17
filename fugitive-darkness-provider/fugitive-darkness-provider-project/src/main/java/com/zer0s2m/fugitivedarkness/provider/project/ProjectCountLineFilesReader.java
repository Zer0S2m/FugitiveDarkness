package com.zer0s2m.fugitivedarkness.provider.project;

import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectCountLineFilesReaderGit;
import com.zer0s2m.fugitivedarkness.provider.project.impl.ProjectCountLineFilesReaderLocal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An interface for implementing a class for counting lines of code from a file of different sources.
 */
public interface ProjectCountLineFilesReader {

    /**
     * Get the number of lines of code in the file.
     *
     * @param adapter An adapter for getting a file reader.
     * @return Number of lines of code in the file
     */
    Collection<FileProjectCountLine> read(ProjectCountLineFilesReaderAdapterAbstract adapter);

    /**
     * Set additional information to start the adapter.
     *
     * @param properties Additional information.
     */
    void setProperties(Map<String, Object> properties);

    static ProjectCountLineFilesReader createGit(String group, String project, String file) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("group", group);
        properties.put("project", project);
        properties.put("file", file);

        ProjectCountLineFilesReader reader = new ProjectCountLineFilesReaderGit();
        reader.setProperties(properties);
        return reader;
    }

    static ProjectCountLineFilesReader createLocal(String source, String file) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("source", source);
        properties.put("file", file);

        ProjectCountLineFilesReader reader = new ProjectCountLineFilesReaderLocal();
        reader.setProperties(properties);
        return reader;
    }

}
