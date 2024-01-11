package com.zer0s2m.fugitivedarkness.provider;

import org.eclipse.jgit.diff.RawText;

import java.io.IOException;
import java.io.InputStream;

public interface SearchEngineGitUtils {

    /**
     * Is the file object a binary.
     *
     * @param stream Obtain an input stream to read this object's data.
     * @return Is the file object a binary.
     * @throws IOException If input stream could not be read.
     */
    default boolean isBinary(InputStream stream) throws IOException {
        try (stream) {
            return RawText.isBinary(stream);
        }
    }

}
