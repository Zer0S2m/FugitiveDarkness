package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.GitIgnore;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Collection;
import java.util.Objects;

/**
 * Minimal implementation <a href="https://git-scm.com/docs/gitignore">.gitignore</a>.
 */
public class GitIgnoreImpl implements GitIgnore {

    /**
     * Check the matching of the string literal for the path to the file object.
     *
     * @param source The path to the file object.
     * @param blob   A string literal for mapping file paths.
     * @return Was it a coincidence.
     */
    @Override
    public boolean matches(Path source, String blob) {
        String cleanBlob = blob;
        if (Objects.equals(cleanBlob.charAt(cleanBlob.length() - 1), '/')) {
            cleanBlob = cleanBlob.substring(0, cleanBlob.length() - 1);
        }

        final PathMatcher pathMatcher = FileSystems
                .getDefault()
                .getPathMatcher("glob:" + cleanBlob);

        if (blob.contains(MULTIPLE_SEGMENTS)) {
            return pathMatcher.matches(source);
        } else {
            return pathMatcher.matches(source.getFileName());
        }
    }

    /**
     * Check the matching of the string literal for the path to the file object.
     *
     * @param source The path to the file object.
     * @param blobs  String literals for matching file paths.
     * @return Was it a coincidence.
     */
    @Override
    public boolean matches(Path source, Collection<String> blobs) {
        for (String blob : blobs) {
            if (matches(source, blob)) {
                return true;
            }
        }
        return false;
    }

}
