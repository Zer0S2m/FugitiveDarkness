package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.FileSystemUtils;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface SearchEngineIOGitWalkingDirectory {

    AtomicInteger COUNT_FILES = new AtomicInteger(0);

    /**
     * Bypass the directory and get a list of all files.
     *
     * @param source The source path to the directory.
     * @param depth The maximum depth of the nested crawl.
     * @param includeFiles Including files in the search that have the specified extensions.
     * @param excludeFiles Excluding files from the search that have the specified extensions.
     * @param patternForIncludeFile Pattern for files that will be included in the search.
     * @param patternForExcludeFile Pattern for files that will be excluded from the search.
     * @return All files.
     * @throws IOException IO exception.
     */
    static Set<Path> walkDirectory(
            Path source,
            int depth,
            Set<String> includeFiles,
            Set<String> excludeFiles,
            Pattern patternForIncludeFile,
            Pattern patternForExcludeFile)
            throws IOException {
        int maxDepth = depth;
        if (maxDepth == -1) {
            maxDepth = Integer.MAX_VALUE;
        }

        // TODO: Add a filter for files .gitignore
        try (Stream<Path> stream = Files.walk(source, maxDepth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> {
                        COUNT_FILES.set(COUNT_FILES.get() + 1);

                        try (final InputStream inputStream = new FileInputStream(file.toFile())) {
                            return !SearchEngineGitUtils.isBinary(inputStream);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(file -> {
                        final String fileName = FileSystemUtils.getFileName(file.toString());
                        return getWhetherSearchByIncludeOrExcludeFileByPattern(
                                patternForIncludeFile, fileName, false);
                    })
                    .filter(file -> {
                        final String fileName = FileSystemUtils.getFileName(file.toString());
                        return !getWhetherSearchByIncludeOrExcludeFileByPattern(
                                patternForExcludeFile, fileName, true);
                    })
                    .filter(file -> {
                        final String extensionFile = FileSystemUtils.getExtensionFromRawStrFile(file.toString());
                        if (includeFiles.isEmpty()) {
                            return true;
                        }
                        return includeFiles.contains(extensionFile);
                    })
                    .filter(file -> {
                        final String extensionFile = FileSystemUtils.getExtensionFromRawStrFile(file.toString());
                        if (excludeFiles.isEmpty()) {
                            return true;
                        }
                        return !excludeFiles.contains(extensionFile);
                    })
                    .collect(Collectors.toSet());
        }
    }

    static private boolean getWhetherSearchByIncludeOrExcludeFileByPattern(
            final Pattern pattern, final String filename, boolean isExclude) {
        if (pattern != null) {
            return pattern
                    .matcher(FileSystemUtils.getFileName(filename))
                    .find();
        }
        return !isExclude;
    }

}
