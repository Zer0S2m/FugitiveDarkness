package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.provider.*;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service for interacting with git repositories
 */
public class GitRepoImpl implements GitRepo {

    Logger logger = LoggerFactory.getLogger(GitRepo.class);

    /**
     * Clone a git repository from a remote host into a set path environment variable {@link Environment#ROOT_PATH_REPO}.
     *
     * @param command Command to clone a repository.
     * @param URI     Remote git repository host. Must not be {@literal null}.
     * @return Git repository information.
     * @throws GitAPIException he exception is caused by the internal functionality of managing git repositories.
     */
    @Override
    public ContainerInfoRepo gCloneStart(
            CloneCommand command, String URI) throws GitAPIException {
        final ContainerInfoRepo infoRepo = gGetInfo(URI);

        logger.info("Start cloning the repository [{}:{}]: {}",
                infoRepo.group(), infoRepo.project(), infoRepo.source());

        command
                .call()
                .close();

        logger.info("Finish cloning the repository [{}:{}]: {}",
                infoRepo.group(), infoRepo.project(), infoRepo.source());

        return infoRepo;
    }

    /**
     * Create a command to clone a repository from a remote host.
     *
     * @param URI Remote git repository host. Must not be {@literal null}.
     * @return Command.
     */
    @Override
    public CloneCommand gCloneCreate(String URI) {
        final ContainerInfoRepo infoRepo = gGetInfo(URI);

        return Git.cloneRepository()
                .setURI(URI)
                .setDirectory(infoRepo.source().toFile())
                .setCloneAllBranches(true)
                .setCloneSubmodules(true)
                .setNoCheckout(true);
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
     * Update from remote repository by group and project.
     *
     * @param group   Project group. Must not be {@literal null}.
     * @param project Project. Must not be {@literal null}.
     * @throws IOException     If an IO error occurred.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    @Override
    public void gFetch(String group, String project) throws IOException, GitAPIException {
        final Path sourceGitRepository = HelperGitRepo.getSourceGitRepository(group, project);

        Git.open(sourceGitRepository.toFile())
                .fetch()
                .call();
    }

    /**
     * Open and get the contents of a file from a git repository by group and project name.
     *
     * @param group   The name of the git repository group.
     * @param project The name of the git repository project.
     * @param file    File name.
     * @return Collected file content from git repository.
     * @throws IOException If an IO error occurred.
     */
    @Override
    public List<ContainerInfoFileContent> gShowFile(String group, String project, String file) throws IOException {
        final Path source = HelperGitRepo.getSourceGitRepository(group, project);

        try (final Repository repository = Git.open(source.toFile())
                .checkout()
                .getRepository()) {
            final ObjectId revision = SearchEngineGitUtils
                    .getRevisionTree(repository, repository.getBranch());

            try (final TreeWalk treeWalk = TreeWalk.forPath(repository, file, revision)) {
                try (final ObjectReader objectReader = repository.newObjectReader()) {
                    ObjectLoader objectLoader = objectReader.open(treeWalk.getObjectId(0));
                    try (final InputStream stream = objectLoader.openStream()) {
                        return gShowFileReadContent(stream);
                    }
                }
            }
        }
    }

    /**
     * Collect file content from a git repository with line numbering.
     *
     * @param stream Obtain an input stream to read this object's data.
     * @return Collected file content from git repository.
     */
    private List<ContainerInfoFileContent> gShowFileReadContent(final InputStream stream) {
        final AtomicInteger lineNumber = new AtomicInteger(1);
        final List<ContainerInfoFileContent> collectedContent = new ArrayList<>();

        try (final InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            try (final BufferedReader buf = new BufferedReader(reader)) {
                String line;

                while ((line = buf.readLine()) != null) {
                    collectedContent.add(new ContainerInfoFileContent(
                            lineNumber.get(),
                            line
                    ));

                    lineNumber.set(lineNumber.get() + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return collectedContent;
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

                        if (filterSearch.getPatternForIncludeFile() != null) {
                            commandGrep.setPatternForIncludeFile(filterSearch.getPatternForIncludeFile());
                        }

                        if (filterSearch.getPatternForExcludeFile() != null) {
                            commandGrep.setPatternForExcludeFile(filterSearch.getPatternForExcludeFile());
                        }

                        commandGrep.setMaxCount(filterSearch.getMaxCount());
                        commandGrep.setMaxDepth(filterSearch.getMaxDepth());

                        if (filterSearch.getContext() == -1 || filterSearch.getContext() == 0) {
                            commandGrep.setContextBefore(filterSearch.getContextBefore());
                            commandGrep.setContextAfter(filterSearch.getContextAfter());
                        } else {
                            commandGrep.setContext(filterSearch.getContext());
                        }

                        final List<ContainerInfoSearchFileGitRepo> searchResult = commandGrep.callGrep();

                        searchFileGitRepos.add(new ContainerInfoSearchGitRepo(
                                gitRepo.group(),
                                gitRepo.project(),
                                filterSearch.getPattern().toString(),
                                gitRepo.getLink(true),
                                commandGrep.getExtensionFilesGrep(),
                                searchResult
                        ));
                    } catch (IOException | SearchEngineGitException e) {
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
