package com.zer0s2m.fugitivedarkness.provider.docx;

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
     */
    ContainerInfoSearchDocxFile search(String pattern);

    /**
     * Searching for matches in a file.
     *
     * @param pattern A pattern for finding matches in file.
     */
    ContainerInfoSearchDocxFile search(Pattern pattern);

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

}
