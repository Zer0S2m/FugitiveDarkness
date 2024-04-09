package com.zer0s2m.fugitivedarkness.provider.git;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The parser for the file .gitignore.
 */
public interface GitIgnoreParser {

    /**
     * Process string literals to map file paths.
     *
     * @param blobs String literals for matching file paths.
     * @return String literals for matching file paths.
     */
    static Set<String> parse(Collection<String> blobs) {
        final Set<String> cleanBLobs = new HashSet<>();

        blobs.forEach(blob -> {
            if (!blob.isEmpty()
                    && !Objects.equals(String.valueOf(blob.charAt(0)), GitEquipment.GITIGNORE_COMMENT.value())) {
                cleanBLobs.add(blob);
            }
        });

        return cleanBLobs;
    }

}
