package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.provider.git.*;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoFileContent;
import com.zer0s2m.fugitivedarkness.provider.git.FileSystemUtils;
import com.zer0s2m.fugitivedarkness.provider.git.SearchEngineGitUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

/**
 * Service for interacting with git repositories
 */
public class GitRepoManagerImpl implements GitRepoManager {

    Logger logger = LoggerFactory.getLogger(GitRepoManager.class);

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
     * Unpack the git archive of the project.
     *
     * @param group   Project group. Must not be {@literal null}.
     * @param project Project. Must not be {@literal null}.
     * @throws IOException     If an IO error occurred.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    @Override
    public void gCheckout(String group, String project) throws IOException, GitAPIException {
        final Path sourceGitRepository = HelperGitRepo.getSourceGitRepository(group, project);

        try (final Git git = Git.open(sourceGitRepository.toFile())) {
            final Repository repository = git.getRepository();
            git
                    .checkout()
                    .setName(repository.getBranch())
                    .call();
        }
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
        // TODO: Get the content of a file in a local project

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
     * <p>Uses a search engine {@link SearchEngineGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> searchByGrep_jgit(GitRepoFilterSearch filterSearch) {
        final List<ContainerInfoSearchGitRepo> searchFileGitRepos = new ArrayList<>();
        filterSearch.getSources()
                .forEach(source -> {
                    logger.info("Start the search [{}]", source);

                    final ContainerGitRepoMeta gitRepo = filterSearch.getGitMeta(source);
                    try {
                        final SearchEngineGrep commandGrep = searchEngineGitGrep_jgit(filterSearch, source, gitRepo);

                        long start = System.currentTimeMillis();

                        final List<ContainerInfoSearchFileGitRepo> searchResult = commandGrep.callGrep();

                        searchFileGitRepos.add(new ContainerInfoSearchGitRepo(
                                gitRepo.group(),
                                gitRepo.project(),
                                filterSearch.getPattern().pattern(),
                                gitRepo.getLink(true),
                                commandGrep.getExtensionFilesGrep(),
                                searchResult
                        ));

                        long finish = System.currentTimeMillis();
                        long timeElapsed = finish - start;

                        logger.info("""
                                        Statistics       [{}]
                                        \tType engine                                     [JGIT]
                                        \tExecution time                                  [{}]
                                        \tAverage file processing time                    [{}]
                                        \tThe number of files in the project              [{}]
                                        \tThe number of files in which matches were found [{}]""",
                                source,
                                timeElapsed,
                                commandGrep.getAverageFileProcessingTime(),
                                commandGrep.getCountFiles(),
                                searchResult.size());
                        logger.info("End of search    [{}]", source);
                    } catch (IOException | SearchEngineGitException e) {
                        throw new RuntimeException(e);
                    }
                });
        return searchFileGitRepos;
    }

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     * <p>Uses a search engine {@link SearchEngineGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> searchByGrep_io(GitRepoFilterSearch filterSearch) {
        final List<ContainerInfoSearchGitRepo> searchFileGitRepos = new ArrayList<>();
        filterSearch.getSources()
                .forEach(source -> {
                    logger.info("Start the search [{}]", source);

                    final ContainerGitRepoMeta gitRepo = filterSearch.getGitMeta(source);
                    try {
                        final SearchEngineGrep commandGrep = searchEngineGitGrep_io(filterSearch, source, gitRepo);

                        long start = System.currentTimeMillis();

                        final List<ContainerInfoSearchFileGitRepo> searchResult = commandGrep.callGrep();

                        searchFileGitRepos.add(new ContainerInfoSearchGitRepo(
                                filterSearch.getGitMeta(source).group(),
                                filterSearch.getGitMeta(source).project(),
                                filterSearch.getPattern().pattern(),
                                filterSearch.getGitMeta(source).getLink(true),
                                commandGrep.getExtensionFilesGrep(),
                                searchResult
                        ));

                        long finish = System.currentTimeMillis();
                        long timeElapsed = finish - start;

                        logger.info("""
                                        Statistics       [{}]
                                        \tType engine                                     [IO]
                                        \tExecution time                                  [{}]
                                        \tAverage file processing time                    [{}]
                                        \tThe number of files in the project              [{}]
                                        \tThe number of files in which matches were found [{}]""",
                                source,
                                timeElapsed,
                                commandGrep.getAverageFileProcessingTime(),
                                commandGrep.getCountFiles(),
                                searchResult.size());
                        logger.info("End of search    [{}]", source);
                    } catch (IOException | SearchEngineGitException e) {
                        throw new RuntimeException(e);
                    }
                });
        return searchFileGitRepos;
    }

    /**
     * Search for matches in files in git repositories by pattern. Git grep command.
     * <p>Uses a search engine {@link SearchEngineGrep}.</p>
     *
     * @param filterSearch Filter for searching git repositories.
     * @return Search result in git repository.
     */
    @Override
    public List<ContainerInfoSearchGitRepo> searchByGrepVirtualThreads_jgit(GitRepoFilterSearch filterSearch) {
        final List<ContainerInfoSearchGitRepo> searchFileGitRepos = new ArrayList<>();

        try (final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final List<GitRepoSearchJGitCallable> gitRepoSearchCallables = new ArrayList<>();
            filterSearch.getSources().forEach((source) -> {
                final GitRepoFilterSearch newFilterSearch = GitRepoFilterSearch.clone(filterSearch);
                newFilterSearch.clearGitMeta();
                newFilterSearch.clearSources();
                newFilterSearch.addGitRepo(source);
                newFilterSearch.addGitMeta(source, filterSearch.getGitMeta(source));

                gitRepoSearchCallables.add(new GitRepoSearchJGitCallable(newFilterSearch));
            });

            final List<Future<List<ContainerInfoSearchGitRepo>>> futures =
                    executor.invokeAll(gitRepoSearchCallables);

            futures.forEach((future) -> {
                try {
                    searchFileGitRepos.addAll(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return searchFileGitRepos;
    }

    /**
     * Build a search engine based on {@link SearchEngineGrep} - {@link SearchEngineJGitGrepImpl}.
     *
     * @param filterSearch Installed search filters.
     * @param source       Source path of the git repository.
     * @param gitRepo      More information about the git repository.
     * @return Assembled search engine.
     * @throws IOException                         If an IO error occurred.
     * @throws SearchEngineGitSetMaxCountException Exception for setting the maximum number of matches in one file.
     * @throws SearchEngineGitSetMaxDepthException Exception for setting maximum search depth.
     * @throws SearchEngineGitSetContextException  Exception for setting preview code after and before matches.
     */
    private static SearchEngineGrep searchEngineGitGrep_jgit(
            GitRepoFilterSearch filterSearch,
            Path source,
            ContainerGitRepoMeta gitRepo) throws IOException, SearchEngineGitSetMaxCountException,
            SearchEngineGitSetMaxDepthException, SearchEngineGitSetContextException {
        final SearchEngineJGitGrep commandGrep = new SearchEngineJGitGrepImpl(
                filterSearch.getPattern(),
                source,
                gitRepo
        );

        if (filterSearch.getIsSearchOnlyInArea() && filterSearch.getAreaFile() != null) {
            commandGrep.setAreaFile(filterSearch.getAreaFile());
        }

        searchEngineGitGrepCollectFilter(filterSearch, commandGrep);

        return commandGrep;
    }

    /**
     * Build a search engine based on {@link SearchEngineGrep} - {@link SearchEngineIOGitGrepImpl}.
     *
     * @param filterSearch Installed search filters.
     * @param source       Source path of the git repository.
     * @param gitRepo      More information about the git repository.
     * @return Assembled search engine.
     * @throws SearchEngineGitSetMaxCountException Exception for setting the maximum number of matches in one file.
     * @throws SearchEngineGitSetMaxDepthException Exception for setting maximum search depth.
     * @throws SearchEngineGitSetContextException  Exception for setting preview code after and before matches.
     */
    private static SearchEngineGrep searchEngineGitGrep_io(
            GitRepoFilterSearch filterSearch,
            Path source,
            ContainerGitRepoMeta gitRepo) throws SearchEngineGitSetMaxDepthException,
            SearchEngineGitSetContextException, SearchEngineGitSetMaxCountException {
        final SearchEngineIOGitGrep commandGrep = new SearchEngineIOGitGrepImpl(
                filterSearch.getPattern(),
                source,
                gitRepo
        );

        if (filterSearch.getIsSearchOnlyInArea() && filterSearch.getAreaFile() != null) {
            commandGrep.setAreaFile(filterSearch.getAreaFile());
            commandGrep.setAreaIsFile(filterSearch.getAreaIsFile());
            commandGrep.setAreaIsDirectory(filterSearch.getAreaIsDirectory());
        }

        searchEngineGitGrepCollectFilter(filterSearch, commandGrep);

        return commandGrep;
    }

    /**
     * Collect filter a search engine based on {@link SearchEngineGrep}.
     *
     * @param filterSearch Installed search filters.
     * @param commandGrep  Search engine.
     * @throws SearchEngineGitSetMaxCountException Exception for setting the maximum number of matches in one file.
     * @throws SearchEngineGitSetMaxDepthException Exception for setting maximum search depth.
     * @throws SearchEngineGitSetContextException  Exception for setting preview code after and before matches.
     */
    private static void searchEngineGitGrepCollectFilter(
            GitRepoFilterSearch filterSearch, SearchEngineGrep commandGrep)
            throws SearchEngineGitSetMaxCountException, SearchEngineGitSetContextException, SearchEngineGitSetMaxDepthException {
        if (filterSearch.getIncludeExtensionFile() != null) {
            commandGrep.setIncludeExtensionFilesForSearchGrep(filterSearch.getIncludeExtensionFile());
        }
        if (filterSearch.getExcludeExtensionFile() != null) {
            commandGrep.setExcludeExtensionFilesForSearchGrep(filterSearch.getExcludeExtensionFile());
        }

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
    }

    /**
     * Get the latest commit in a specific file.
     *
     * @param source The source path to the repository.
     * @param file   The path to the file where the last commit will be searched.
     * @return The last commit.
     */
    @Override
    public RevCommit gLastCommitOfFile(Path source, String file) {
        try (final Git git = Git.open(source.toFile())) {
            return git
                    .log()
                    .addPath(file)
                    .call()
                    .iterator()
                    .next();
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the first commit in a specific file.
     *
     * @param source The source path to the repository.
     * @param file   The path to the file where the last commit will be searched.
     * @return The first commit.
     */
    @Override
    public RevCommit gFirstCommitOfFile(Path source, String file) {
        try (final Repository repository = Git
                .open(source.toFile())
                .getRepository()) {
            try (RevWalk revWalk = new RevWalk(repository)) {
                revWalk.markStart(revWalk.parseCommit(repository.resolve(Constants.HEAD)));
                revWalk.setTreeFilter(PathFilter.create(file));
                revWalk.sort(RevSort.COMMIT_TIME_DESC);
                revWalk.sort(RevSort.REVERSE, true);
                return revWalk.next();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the all commits in a specific file.
     *
     * @param source The source path to the repository.
     * @param file   The path to the file where the all commits will be searched.
     * @return The all commits.
     */
    @Override
    public Collection<RevCommit> gAllCommitIfFile(Path source, String file) {
        try (final Git git = Git.open(source.toFile())) {
            return StreamSupport.stream(git
                                    .log()
                                    .addPath(file)
                                    .call()
                                    .spliterator(),
                            false
                    )
                    .toList();
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all commits related to files.
     *
     * @param source The source path to the repository.
     * @param files  Paths are relative paths to files in the form of a collection.
     * @return File commits.
     */
    @Override
    public Map<String, Iterable<RevCommit>> gGetAllCommitsOfFiles(Path source, Collection<String> files) {
        final Map<String, Iterable<RevCommit>> commitsFiles = new HashMap<>();

        try (final Git git = Git.open(source.toFile())) {
            files.forEach(file -> {
                try {
                    Iterable<RevCommit> commits = git
                            .log()
                            .addPath(file)
                            .call();
                    commitsFiles.put(file, commits);
                } catch (GitAPIException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return commitsFiles;
    }

}
