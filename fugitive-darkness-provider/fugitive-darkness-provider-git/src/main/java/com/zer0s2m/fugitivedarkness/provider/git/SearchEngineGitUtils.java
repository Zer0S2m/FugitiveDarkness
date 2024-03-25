package com.zer0s2m.fugitivedarkness.provider.git;

import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

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
    static boolean isBinary(InputStream stream) throws IOException {
        try (stream) {
            return RawText.isBinary(stream);
        }
    }

    /**
     * Get git revision and return object id {@link Constants#HEAD}.
     *
     * @param repository Git repository.
     * @return ObjectId.
     * @throws IOException On serious errors.
     */
    static ObjectId getRevision(final Repository repository) throws IOException {
        return repository.resolve(Constants.HEAD);
    }

    /**
     * Get git revision <b>tree</b> and return object id {@link Constants#HEAD}.
     *
     * @param repository Git repository.
     * @param branch Main branch for building the tree.
     * @return ObjectId.
     * @throws IOException On serious errors.
     */
    static ObjectId getRevisionTree(final Repository repository, final String branch) throws IOException {
        return repository.resolve(String.format(
                "refs/heads/%s^{tree}",
                branch
        ));
    }

}
