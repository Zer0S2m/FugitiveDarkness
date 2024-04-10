package com.zer0s2m.fugitivedarkness.provider.git;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An indexer for searching files <a href="https://git-scm.com/docs/gitignore">.gitignore</a>.
 */
public interface GitIgnoreIndexer {

    /**
     * Find all files .gitignore.
     *
     * @param source The path to the root directory.
     * @param depth  The maximum nesting depth for the search.
     * @return Files.gitignore.
     */
    static Set<Path> find(Path source, int depth) throws IOException {

        try (Stream<Path> tree = Files.walk(source, depth)) {
            return tree
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> {
                         String[] fileSplit = file.toString().split("/");
                        return Objects.equals(fileSplit[fileSplit.length - 1], GitEquipment.GITIGNORE_FILE.value());
                    })
                    .collect(Collectors.toSet());
        }
    }

}
