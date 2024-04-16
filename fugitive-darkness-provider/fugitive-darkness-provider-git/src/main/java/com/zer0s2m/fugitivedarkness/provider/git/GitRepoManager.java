package com.zer0s2m.fugitivedarkness.provider.git;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.provider.git.impl.GitRepoManagerImpl;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface for implementing a service that handles git repositories.
 */
public interface GitRepoManager {

    /**
     * Clone a git repository from a remote host into a set path environment variable {@link Environment#ROOT_PATH_REPO}.
     *
     * @param command Command to clone a repository.
     * @param URI     Remote git repository host. Must not be {@literal null}.
     * @return Git repository information.
     * @throws GitAPIException he exception is caused by the internal functionality of managing git repositories.
     */
    ContainerInfoRepo gCloneStart(CloneCommand command, String URI) throws GitAPIException;

    /**
     * Create a command to clone a repository from a remote host.
     *
     * @param URI Remote git repository host. Must not be {@literal null}.
     * @return Command.
     */
    CloneCommand gCloneCreate(String URI);

    /**
     * Get git repository information from URI.
     *
     * @param URI Remote git repository host.
     * @return Git repository information.
     */
    default ContainerInfoRepo gGetInfo(String URI) {
        final ContainerInfoRepo infoRepo = HelperGitRepo.getInfoRepo(URI);

        return new ContainerInfoRepo(
                infoRepo.host(),
                infoRepo.group(),
                infoRepo.project(),
                infoRepo.link(),
                Path.of(Path.of(
                        Environment.ROOT_PATH_REPO,
                        infoRepo.group(),
                        infoRepo.project()).toString())
        );
    }

    /**
     * Get git repository information from URI.
     *
     * @param URI     Remote git repository host.
     * @param isLocal Whether the repository is local to the file system .
     * @return Git repository information.
     */
    default ContainerInfoRepo gGetInfo(String URI, boolean isLocal) {
        if (!isLocal) {
            return gGetInfo(URI);
        } else {
            List<String> URUSplit = Arrays.asList(URI.split("/"));
            return new ContainerInfoRepo(
                    "LOCAL",
                    "LOCAL",
                    URUSplit.get(URUSplit.size() - 1),
                    "LOCAL",
                    Path.of(URI)
            );
        }
    }

    /**
     * Removing a git repository from the file system.
     *
     * @param group   Project group.
     * @param project Project
     * @throws IOException IO exception.
     */
    void gDelete(String group, String project) throws IOException;

    /**
     * Update from remote repository by group and project.
     *
     * @param group   Project group. Must not be {@literal null}.
     * @param project Project. Must not be {@literal null}.
     * @throws IOException     If an IO error occurred.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    void gFetch(String group, String project) throws IOException, GitAPIException;

    /**
     * Unpack the git archive of the project.
     *
     * @param group   Project group. Must not be {@literal null}.
     * @param project Project. Must not be {@literal null}.
     * @throws IOException     If an IO error occurred.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    void gCheckout(String group, String project) throws IOException, GitAPIException;

    /**
     * Open and get the contents of a file from a git repository by group and project name.
     *
     * @param group   The name of the git repository group.
     * @param project The name of the git repository project.
     * @param file    File name.
     * @return Collected file content from git repository.
     * @throws IOException If an IO error occurred.
     */
    List<ContainerInfoFileContent> gShowFile(String group, String project, String file) throws IOException;

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     * <p>Uses a search engine {@link SearchEngineGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    List<ContainerInfoSearchGitRepo> searchByGrep_jgit(GitRepoFilterSearch filterSearch);

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     * <p>Uses a search engine {@link SearchEngineGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    List<ContainerInfoSearchGitRepo> searchByGrep_io(GitRepoFilterSearch filterSearch);

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     * <p>Uses a search engine {@link SearchEngineGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    List<ContainerInfoSearchGitRepo> searchByGrepVirtualThreads_jgit(GitRepoFilterSearch filterSearch);

    /**
     * Get the latest commit in a specific file.
     *
     * @param source The source path to the repository.
     * @param file   The path to the file where the last commit will be searched.
     * @return The last commit.
     */
    RevCommit gLastCommitOfFile(Path source, String file);

    /**
     * Get all commits related to files.
     *
     * @param source The source path to the repository.
     * @param files  Paths are relative paths to files in the form of a collection.
     * @return File commits.
     */
    Map<String, Iterable<RevCommit>> gGetAllCommitsOfFiles(Path source, Collection<String> files);

    static GitRepoManager create() {
        return new GitRepoManagerImpl();
    }

}
