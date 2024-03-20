package com.zer0s2m.fugitivedarkness.provider.docx;

import java.nio.file.Path;
import java.util.Collection;
import java.util.regex.Pattern;

public interface SearchEngineDocxGrep extends AutoCloseable {

    /**
     * Run a search through the entire document. Search criteria:
     * <ul>
     *     <li> Match according to the established pattern {@link SearchEngineDocxGrep#getPattern()}.</li>
     * </ul>
     *
     * @return The search result.
     */
    Collection<ContainerInfoSearchDocxFile> callGrep();

    /**
     * Set a template to search for matches in the document.
     *
     * @param pattern A pattern for finding matches in file.
     */
    void setPattern(Pattern pattern);

    /**
     * Get a template to search for matches in the document.
     *
     * @return pattern A pattern for finding matches in file.
     */
    Pattern getPattern();

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
