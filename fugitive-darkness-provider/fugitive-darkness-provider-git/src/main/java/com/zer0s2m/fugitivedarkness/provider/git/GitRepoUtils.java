package com.zer0s2m.fugitivedarkness.provider.git;

import com.zer0s2m.fugitivedarkness.common.Environment;

public interface GitRepoUtils {

    String LINE_NUMBER_POINTER = "#L";

    /**
     * Collect link to match.
     *
     * @param gitRepoMeta  Information about the git repository.
     * @param file         The name of the file where the match occurred.
     * @param targetBranch The target branch where the search took place.
     * @return Link to match.
     */
    static String getLinkForFile(
            final ContainerGitRepoMeta gitRepoMeta,
            final String file,
            final String targetBranch) {
        return gitRepoMeta.getLink(false) + "/tree/" + targetBranch + "/" + file;
    }

    /**
     * Collect link to match.
     *
     * @param gitRepoMeta  Information about the git repository.
     * @param file         The name of the file where the match occurred.
     * @param targetBranch The target branch where the search took place.
     * @param lineNumber   Line number where the match occurred.
     * @return Link to match.
     */
    static String getLinkForMatcherLine(
            final ContainerGitRepoMeta gitRepoMeta,
            final String file,
            final String targetBranch,
            final int lineNumber) {
        return getLinkForFile(gitRepoMeta, file, targetBranch) + LINE_NUMBER_POINTER + lineNumber;
    }

    static String cleanRawFilePath(String path, String ...parts) {
        StringBuilder readyParts = new StringBuilder(Environment.ROOT_PATH_REPO);
        if (!readyParts.substring(readyParts.length() - 1).equals('/')) {
            readyParts.append('/');
        }

        for (String part : parts) {
            readyParts.append(part).append('/');
        }

        return path.replace(readyParts.toString(), "");
    }

}
