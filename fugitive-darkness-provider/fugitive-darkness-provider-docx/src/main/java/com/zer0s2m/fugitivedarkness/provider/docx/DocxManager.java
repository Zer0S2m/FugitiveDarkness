package com.zer0s2m.fugitivedarkness.provider.docx;

import com.zer0s2m.fugitivedarkness.provider.docx.impl.DocxManagerImpl;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * An interface for implementing a manager for working with docx documents.
 */
public interface DocxManager extends AutoCloseable {

    /**
     * Searching for matches in a file.
     *
     * @param pattern A pattern for finding matches in file.
     * @throws SearchEngineDocxCloseManagerException The general exception for the search engine is that the
     *                                               manager for managing the docx file is closed.
     * @throws SearchEngineDocxException             The general exception for the search engine is docx files.
     */
    ContainerInfoSearchDocxFile search(String pattern) throws Exception;

    /**
     * Searching for matches in a file.
     *
     * @param pattern A pattern for finding matches in file.
     * @throws SearchEngineDocxCloseManagerException The general exception for the search engine is that the
     *                                               manager for managing the docx file is closed.
     * @throws SearchEngineDocxException             The general exception for the search engine is docx files.
     */
    ContainerInfoSearchDocxFile search(Pattern pattern) throws Exception;

    /**
     * Set the source path to the document.
     *
     * @param source The original path.
     */
    void setFile(Path source);

    /**
     * Get the original path to the document.
     *
     * @return The original path.
     */
    Path getFile();

    static DocxManager create() {
        return new DocxManagerImpl();
    }

}
