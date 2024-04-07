package com.zer0s2m.fugitivedarkness.provider.git.helpers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class RepositoryBranch {

    private String currentBranch = null;

    private Path sourceHeadFile;

    /**
     * Get current reference.
     *
     * @return Reference
     */
    public String getCurrentBranch() {
        if (currentBranch != null) {
            return currentBranch;
        }

        String ref = null;

        try (final FileInputStream fileInputStream = new FileInputStream(sourceHeadFile.toFile())) {
            try (final InputStreamReader inputStreamReader =
                         new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
                try (final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }

                        ref = line;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert ref != null;
        final String[] refSplit = ref.split("/");

        currentBranch = refSplit[refSplit.length - 1];

        return currentBranch;
    }

    public void setSourceHeadFile(Path sourceHeadFile) {
        this.sourceHeadFile = sourceHeadFile;
    }

}
