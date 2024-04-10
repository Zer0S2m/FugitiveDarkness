package com.zer0s2m.fugitivedarkness.provider.git;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Minimal implementation <a href="https://git-scm.com/docs/gitignore">.gitignore</a>.
 */
public interface GitIgnore {

    String MULTIPLE_SEGMENTS = "**";

    /**
     * Check the matching of the string literal for the path to the file object.
     *
     * @param source The path to the file object.
     * @param blob   A string literal for mapping file paths.
     * @return Was it a coincidence.
     */
    boolean matches(Path source, String blob);

    /**
     * Check the matching of the string literal for the path to the file object.
     *
     * @param source The path to the file object.
     * @param blobs  String literals for matching file paths.
     * @return Was it a coincidence.
     */
    boolean matches(Path source, Collection<String> blobs);

}
