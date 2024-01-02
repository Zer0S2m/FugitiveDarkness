package com.zer0s2m.fugitivedarkness.provider;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.provider.impl.GitRepoImpl;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.List;

/**
 * Interface for implementing a service that handles git repositories.
 */
public interface GitRepo {

    String LINE_NUMBER_POINTER = "#L";

    /**
     * Clone a git repository from a remote host into a set path environment variable {@link Environment#ROOT_PATH_REPO}.
     *
     * @param URI Remote git repository host.
     * @return Git repository information.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    ContainerInfoRepo gClone(String URI) throws GitAPIException;

    void gDelete(String group, String project);

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    List<ContainerInfoSearchGitRepo> searchByGrep(GitRepoFilterSearch filterSearch);

    static GitRepo create() {
        return new GitRepoImpl();
    }

    static String getLinkForFile(
            final ContainerGitRepoMeta gitRepoMeta,
            final String file,
            final String targetBranch) {
        return gitRepoMeta.getLink(false) + "/tree/" + targetBranch + "/" + file;
    }

    static String getLinkForMatcherLine(
            final ContainerGitRepoMeta gitRepoMeta,
            final String file,
            final String targetBranch,
            final int lineNumber) {
        return getLinkForFile(gitRepoMeta, file, targetBranch) + LINE_NUMBER_POINTER + lineNumber;
    }

}
