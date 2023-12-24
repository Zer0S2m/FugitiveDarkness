package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoControl;
import com.zer0s2m.fugitivedarkness.provider.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for interacting with git repositories
 */
public class GitRepoImpl implements GitRepo {

    Logger logger = LoggerFactory.getLogger(GitRepo.class);

    /**
     * Clone a git repository from a remote host into a set path environment variable {@link Environment#ROOT_PATH_REPO}.
     *
     * @param URI Remote git repository host.
     * @return Git repository information.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    @Override
    public ContainerInfoRepo gClone(String URI) throws GitAPIException {
        final ContainerInfoRepo infoRepo = HelperGitRepo.getInfoRepo(URI);
        final Path pathSourceGitRepo = Path.of(
                Environment.ROOT_PATH_REPO,
                infoRepo.group(),
                infoRepo.project());
        final File source = pathSourceGitRepo.toFile();

        final ContainerInfoRepo updatedInfoRepo = new ContainerInfoRepo(
                infoRepo.host(),
                infoRepo.group(),
                infoRepo.project(),
                Path.of(pathSourceGitRepo.toString(), ".git")
        );

        logger.info("Start cloning the repository: " + source.getPath());

        Git.cloneRepository()
                .setURI(URI)
                .setDirectory(source)
                .setCloneAllBranches(true)
                .setCloneSubmodules(true)
                .setNoCheckout(true)
                .call()
                .close();

        logger.info("Finish cloning the repository: " + source.getPath());

        return updatedInfoRepo;
    }

    @Override
    public void gDelete(String group, String project) {

    }

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> searchByGrep(GitRepoFilterSearch filterSearch) {
        final List<ContainerInfoSearchGitRepo> searchFileGitRepos = new ArrayList<>();
        filterSearch.getSources()
                .forEach(source -> {
                    final ContainerGitRepoControl gitRepo = filterSearch.getGitMeta(source);
                    try {
                        searchFileGitRepos.add(new ContainerInfoSearchGitRepo(
                                gitRepo.group(),
                                gitRepo.project(),
                                filterSearch.getPattern().toString(),
                                new GitRepoCommandGrep(filterSearch.getPattern(), source)
                                        .call()
                        ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return searchFileGitRepos;
    }

}
