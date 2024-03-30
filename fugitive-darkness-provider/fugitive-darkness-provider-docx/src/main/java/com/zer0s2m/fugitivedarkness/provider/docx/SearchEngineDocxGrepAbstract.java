package com.zer0s2m.fugitivedarkness.provider.docx;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class SearchEngineDocxGrepAbstract implements SearchEngineDocxGrep {

    private Path sourceFile = null;

    private Pattern patternMatcher = null;

    /**
     * Set a template to search for matches in the document.
     *
     * @param pattern A pattern for finding matches in file.
     */
    @Override
    public void setPattern(Pattern pattern) {
        Objects.requireNonNull(pattern);

        patternMatcher = pattern;
    }

    /**
     * Get a template to search for matches in the document.
     *
     * @return pattern A pattern for finding matches in file.
     */
    @Override
    public Pattern getPattern() {
        return patternMatcher;
    }

    /**
     * Set the source path to the document.
     *
     * @param source The original path.
     */
    @Override
    public void setFile(Path source) {
        Objects.requireNonNull(source);

        sourceFile = source;
    }

    /**
     * Get the original path to the document.
     *
     * @return The original path.
     */
    @Override
    public Path getFile() {
        return sourceFile;
    }

    @Override
    public void close() {
        sourceFile = null;
        patternMatcher = null;
    }

}
