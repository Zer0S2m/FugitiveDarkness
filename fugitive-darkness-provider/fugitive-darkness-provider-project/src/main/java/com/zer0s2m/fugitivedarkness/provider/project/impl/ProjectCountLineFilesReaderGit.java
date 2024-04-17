package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.FileProjectCountLine;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesReader;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectCountLineFilesReaderAdapterAbstract;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A class for counting lines of code from a file from the git repository.
 */
public class ProjectCountLineFilesReaderGit implements ProjectCountLineFilesReader {

    private Map<String, Object> properties;

    /**
     * Get the number of lines of code in the file.
     *
     * @param adapter An adapter for getting a file reader.
     * @return Number of lines of code in the file
     */
    @Override
    public Collection<FileProjectCountLine> read(ProjectCountLineFilesReaderAdapterAbstract adapter) {
        Collection<FileProjectCountLine> fileProjectCountLines = new ArrayList<>();

        try (final Reader reader = adapter.getReader(properties)) {
            long countLines = 0;
            try (final BufferedReader buf = new BufferedReader(reader)) {
                while (buf.readLine() != null) {
                    countLines++;
                }
            }
            fileProjectCountLines.add(new FileProjectCountLine(
                    (String) properties.get("file"),
                    countLines));
            return fileProjectCountLines;
        } catch (ProjectException | IOException e) {
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
