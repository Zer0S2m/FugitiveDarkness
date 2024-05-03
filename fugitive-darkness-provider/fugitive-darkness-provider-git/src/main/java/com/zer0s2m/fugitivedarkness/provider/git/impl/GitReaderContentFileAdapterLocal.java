package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.GitReaderContentFileAdapter;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

/**
 * A class for providing a service for reading file content from a source of local projects.
 */
public class GitReaderContentFileAdapterLocal implements GitReaderContentFileAdapter {

    @Override
    public InputStream read(Map<String, Object> properties) throws FileNotFoundException {
        final Path source = Path.of((String) properties.get("source"));
        final Path pathFileObject = Path.of((String) properties.get("file"));

        return new FileInputStream(Path.of(
                        source.toString(),
                        source.relativize(pathFileObject).toString()) // TODO: A crutch for getting a path from the root of a local project
                .toFile());
    }

}
