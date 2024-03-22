package com.zer0s2m.fugitivedarkness.provider.git.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface SearchEngineIOGitWalkingDirectory {

    static Set<Path> walkDirectory(Path source, int depth) throws IOException {
        if (depth != -1) {
            try (Stream<Path> stream = Files.walk(source, depth)) {
                return stream
                        .filter(file -> !Files.isDirectory(file))
                        .collect(Collectors.toSet());
            }
        } else {
            try (Stream<Path> stream = Files.walk(source)) {
                return stream
                        .filter(file -> !Files.isDirectory(file))
                        .collect(Collectors.toSet());
            }
        }
    }

}
