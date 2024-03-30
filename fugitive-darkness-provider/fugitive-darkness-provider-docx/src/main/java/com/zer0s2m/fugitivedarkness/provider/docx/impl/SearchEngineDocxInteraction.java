package com.zer0s2m.fugitivedarkness.provider.docx.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipFile;

/**
 * A plumbing tool for extracting the main xml file from a docx file.
 */
class SearchEngineDocxInteraction implements AutoCloseable {

    private final Path source;

    private String loadedData;

    public SearchEngineDocxInteraction(Path source) {
        this.source = source;
    }

    public void load() throws IOException {
        try (final ZipFile zipFile = new ZipFile(source.toFile())) {
            try (final InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(
                    Path.of(XMLDocumentDocx.FOLDER.value(), XMLDocumentDocx.FILE_NAME.value()).toString()))) {
                loadedData = new String(inputStream.readAllBytes());
            }
        }
    }

    public String getLoadedData() {
        return loadedData;
    }

    @Override
    public void close() {
        loadedData = null;
    }
}
