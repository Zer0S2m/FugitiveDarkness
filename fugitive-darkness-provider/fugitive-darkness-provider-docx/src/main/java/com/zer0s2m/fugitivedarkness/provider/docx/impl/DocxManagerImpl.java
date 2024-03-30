package com.zer0s2m.fugitivedarkness.provider.docx.impl;

import com.zer0s2m.fugitivedarkness.provider.docx.*;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * A service for docx file maintenance.
 * <p>Includes:</p>
 * <ul>
 *     <li>Search for a file in the form of the grep command.</li>
 * </ul>
 */
public class DocxManagerImpl implements DocxManager {

    private Path sourceDocx;

    /**
     * Searching for matches in a file.
     *
     * @param pattern A pattern for finding matches in file.
     * @throws SearchEngineDocxCloseManagerException The general exception for the search engine is that the
     *                                               manager for managing the docx file is closed.
     * @throws SearchEngineDocxException             The general exception for the search engine is docx files.
     */
    @Override
    public ContainerInfoSearchDocxFile search(String pattern) throws Exception {
        return search(Pattern.compile(pattern));
    }

    /**
     * Searching for matches in a file.
     *
     * @param pattern A pattern for finding matches in file.
     * @throws SearchEngineDocxCloseManagerException The general exception for the search engine is that the
     *                                               manager for managing the docx file is closed.
     * @throws SearchEngineDocxException             The general exception for the search engine is docx files.
     */
    @Override
    public ContainerInfoSearchDocxFile search(Pattern pattern) throws Exception {
        if (sourceDocx == null) {
            throw new SearchEngineDocxCloseManagerException("The manager for managing the docx file is closed");
        }

        try (final SearchEngineDocxGrep engineDocxGrep = new SearchEngineDocxGrepImpl()) {
            engineDocxGrep.setFile(sourceDocx);
            engineDocxGrep.setPattern(pattern);

            return engineDocxGrep.callGrep();
        }
    }

    /**
     * Set the source path to the document.
     *
     * @param source The original path.
     */
    @Override
    public void setFile(Path source) {
        sourceDocx = source;
    }

    /**
     * Get the original path to the document.
     *
     * @return The original path.
     */
    @Override
    public Path getFile() {
        return sourceDocx;
    }

    @Override
    public void close() {
        sourceDocx = null;
    }

}
