package com.zer0s2m.fugitivedarkness.provider;

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

}
