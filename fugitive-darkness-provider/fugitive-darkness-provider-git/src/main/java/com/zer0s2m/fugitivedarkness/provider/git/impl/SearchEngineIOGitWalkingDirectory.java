package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.FileSystemUtils;
import com.zer0s2m.fugitivedarkness.provider.git.GitEquipment;
import com.zer0s2m.fugitivedarkness.provider.git.GitIgnore;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
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
     * @param source                The source path to the directory.
     * @param depth                 The maximum depth of the nested crawl.
     * @param includeFiles          Including files in the search that have the specified extensions.
     * @param excludeFiles          Excluding files from the search that have the specified extensions.
     * @param patternForIncludeFile Pattern for files that will be included in the search.
     * @param patternForExcludeFile Pattern for files that will be excluded from the search.
     * @param blobs                 String literals for matching file paths.
     * @return All files.
     * @throws IOException IO exception.
     */
    static Set<Path> walkDirectory(
            Path source,
            int depth,
            Set<String> includeFiles,
            Set<String> excludeFiles,
            Pattern patternForIncludeFile,
            Pattern patternForExcludeFile,
            Set<String> blobs)
            throws IOException {
        final GitIgnore gitIgnore = new GitIgnoreImpl();
        final Set<String> matchesPathBlob = new HashSet<>();

        int maxDepth = depth;
        if (maxDepth == -1) {
            maxDepth = Integer.MAX_VALUE;
        }

        try (Stream<Path> stream = Files.walk(source, maxDepth)) {
            return stream
                    .filter(file -> {
                        String[] splitFile = file.toString().split("/");
                        return !Arrays.asList(splitFile).contains(GitEquipment.FOLDER.value());
                    })
                    .filter(file -> {
                        Path rootProject = Path
                                .of(file
                                        .toString()
                                        .replace(source.toString() + "/", ""));
                        boolean matches = !gitIgnore.matches(rootProject, blobs);

                        if (!matches) {
                            matchesPathBlob.add(rootProject.toString());
                        }

                        return matches;
                    })
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> {
                        for (String partPath : matchesPathBlob) {
                            if (file.toString().contains(partPath)) {
                                return false;
                            }
                        }

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
