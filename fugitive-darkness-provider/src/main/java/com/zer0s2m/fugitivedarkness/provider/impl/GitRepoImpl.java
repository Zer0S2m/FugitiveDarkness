package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.common.Environment;
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
        final ContainerInfoRepo infoRepo = gGetInfo(URI);

        logger.info("Start cloning the repository: " + infoRepo.source());

        Git.cloneRepository()
                .setURI(URI)
                .setDirectory(infoRepo.source().toFile())
                .setCloneAllBranches(true)
                .setCloneSubmodules(true)
                .setNoCheckout(true)
                .call()
                .close();

        logger.info("Finish cloning the repository: " + infoRepo.source());

        return infoRepo;
    }

    /**
     * Removing a git repository from the file system.
     *
     * @param group   Project group.
     * @param project Project
     * @throws IOException IO exception.
     */
    @Override
    public void gDelete(String group, String project) throws IOException {
        final Path sourceGitRepository = HelperGitRepo.getSourceGitRepository(group, project);
        FileSystemUtils.deleteDirectory(sourceGitRepository);
    }

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     * <p>Uses a search engine {@link SearchEngineGitGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> searchByGrep(GitRepoFilterSearch filterSearch) {
        final List<ContainerInfoSearchGitRepo> searchFileGitRepos = new ArrayList<>();
        filterSearch.getSources()
                .forEach(source -> {
                    final ContainerGitRepoMeta gitRepo = filterSearch.getGitMeta(source);
                    try {
                        final SearchEngineGitGrep commandGrep = searchEngineGitGrep(filterSearch, source, gitRepo);
                        final List<ContainerInfoSearchFileGitRepo> searchResult = commandGrep.callGrep();

                        searchFileGitRepos.add(new ContainerInfoSearchGitRepo(
                                gitRepo.group(),
                                gitRepo.project(),
                                filterSearch.getPattern().toString(),
                                gitRepo.getLink(true),
                                commandGrep.getExtensionFilesGrep(),
                                searchResult
                        ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return searchFileGitRepos;
    }

    /**
     * Build a search engine based on {@link SearchEngineGitGrep}.
     *
     * @param filterSearch Installed search filters.
     * @param source       Source path of the git repository.
     * @param gitRepo      More information about the git repository.
     * @return Assembled search engine.
     * @throws IOException If an IO error occurred.
     */
    private static SearchEngineGitGrep searchEngineGitGrep(
            GitRepoFilterSearch filterSearch,
            Path source,
            ContainerGitRepoMeta gitRepo) throws IOException {
        final SearchEngineGitGrep commandGrep = new SearchEngineGitGrepImpl(
                filterSearch.getPattern(),
                source,
                gitRepo
        );
        if (filterSearch.getIncludeExtensionFile() != null) {
            commandGrep.setIncludeExtensionFilesForSearchGrep(filterSearch.getIncludeExtensionFile());
        }
        if (filterSearch.getExcludeExtensionFile() != null) {
            commandGrep.setExcludeExtensionFilesForSearchGrep(filterSearch.getExcludeExtensionFile());
        }
        return commandGrep;
    }

}
