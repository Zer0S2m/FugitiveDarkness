package com.zer0s2m.fugitivedarkness.provider.git.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface FileReader {

    static List<String> read(Path source) {
        List<String> lines = new ArrayList<>();

        try (final Reader reader = new java.io.FileReader(source.toFile())) {
            try (final BufferedReader buf = new BufferedReader(reader)) {
                for (String line; (line = buf.readLine()) != null; ) {
                    if (!line.isEmpty()) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    static List<String> read(Collection<Path> sources) {
        List<String> lines = new ArrayList<>();
        sources.forEach(source -> lines.addAll(read(source)));
        return lines;
    }

}
