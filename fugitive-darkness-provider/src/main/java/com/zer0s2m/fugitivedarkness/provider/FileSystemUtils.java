package com.zer0s2m.fugitivedarkness.provider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Utilities for working with the file system.
 */
public interface FileSystemUtils {

    /**
     * Delete a directory recursively.
     * @param source Source
     * @throws IOException IO exception.
     */
    static void deleteDirectory(Path source) throws IOException {
        try (final Stream<Path> walkDirectory = Files.walk(source)) {
            walkDirectory.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    static String getExtensionFromRawStrFile(String source) {
        final String[] splitSource = source.split("\\.");
        return splitSource[splitSource.length - 1];
    }

}
