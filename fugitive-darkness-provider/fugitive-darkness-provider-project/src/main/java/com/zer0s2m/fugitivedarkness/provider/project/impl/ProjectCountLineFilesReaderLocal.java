package com.zer0s2m.fugitivedarkness.provider.project.impl;

import com.zer0s2m.fugitivedarkness.provider.project.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * A class for counting lines of code from a file from local projects.
 */
public class ProjectCountLineFilesReaderLocal implements ProjectCountLineFilesReader {

    private Map<String, Object> properties;

    /**
     * Get the number of lines of code in the file.
     *
     * @param adapter An adapter for getting a file reader.
     * @return Number of lines of code in the file
     */
    @Override
    public Collection<FileProjectCountLine> read(
            ProjectCountLineFilesReaderAdapterAbstract adapter, TypeFileObject typeFileObject)
            throws ProjectException, IOException {
        Collection<FileProjectCountLine> fileProjectCountLines = new ArrayList<>();

        if (typeFileObject.equals(TypeFileObject.FILE)) {
            try (final Reader reader = adapter.getReader(properties)) {
                fileProjectCountLines.add(
                        new ProjectCountLineFilesCallable()
                                .setReader(reader)
                                .setFile((String) properties.get("file"))
                                .call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (typeFileObject.equals(TypeFileObject.DIRECTORY)) {
            for (ContainerInfoReader infoReader : adapter.getReader(properties, typeFileObject)) {
                try (Reader reader = infoReader.reader()) {
                    fileProjectCountLines.add(new ProjectCountLineFilesCallable()
                            .setReader(reader)
                            .setFile(infoReader.path())
                            .call());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fileProjectCountLines;
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
