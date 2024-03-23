package com.zer0s2m.fugitivedarkness.provider.git.impl;

import com.zer0s2m.fugitivedarkness.provider.git.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * Service for providing search in git repositories. Similar to the
 * <a href="https://git-scm.com/docs/git-grep">grep</a> command in git.
 */
class SearchEngineIOGitGrepImpl extends SearchEngineIOGitGrepAbstract implements SearchEngineIOGitGrep {

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     */
    protected SearchEngineIOGitGrepImpl(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta) {
        super(pattern, source, containerGitRepoMeta);
    }

    /**
     * Start search.
     * <p>The search is based on the following criteria:</p>
     * <ul>
     *     <li>Including files in the search that have the specified
     *     extensions {@link SearchEngineGrep#getIncludeExtensionFilesForSearchGrep}.</li>
     *     <li>Excluding files from the search that have the specified
     *     extensions {@link SearchEngineGrep#getExcludeExtensionFilesForSearchGrep}.</li>
     *     <li>Match pattern {@link SearchEngineGrep#getPattern}.</li>
     *     <li>Include files by pattern {@link SearchEngineGrep#getPatternForIncludeFile} in the search.</li>
     *     <li>Exclude files from the search by pattern {@link SearchEngineGrep#getPatternForExcludeFile}.</li>
     *     <li>Maximum search depth {@link SearchEngineGrep#getMaxDepth()}.</li>
     *     <li>Maximum number of matches in one file {@link SearchEngineGrep#getMaxCount()}.</li>
     * </ul>
     *
     * @return Search results.
     * @throws IOException IO exception.
     */
    @Override
    public List<ContainerInfoSearchFileGitRepo> callGrep() throws IOException {
        final List<ContainerInfoSearchFileGitRepo> containerInfoSearchFileGitRepos = new ArrayList<>();
        final Set<Path> files = SearchEngineIOGitWalkingDirectory
                .walkDirectory(
                        getDirectory(),
                        getMaxDepth(),
                        getIncludeExtensionFilesForSearchGrep(),
                        getExcludeExtensionFilesForSearchGrep(),
                        getPatternForIncludeFile(),
                        getPatternForExcludeFile());
        final List<SearchInFileMatchFilterCallableAbstract<ContainerInfoSearchFileGitRepo>> searchIOFileCallables =
                collectVirtualThreads(files);

        try (final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<ContainerInfoSearchFileGitRepo>> futures =
                    executor.invokeAll(searchIOFileCallables);
            futures.forEach(future -> {
                try {
                    final ContainerInfoSearchFileGitRepo containerInfoSearchFileGitRepo = future.get();
                    if (containerInfoSearchFileGitRepo != null) {
                        containerInfoSearchFileGitRepos.add(containerInfoSearchFileGitRepo);
                        addExtensionFilesGrep(containerInfoSearchFileGitRepo.extension());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return containerInfoSearchFileGitRepos;
    }

    private List<SearchInFileMatchFilterCallableAbstract<ContainerInfoSearchFileGitRepo>> collectVirtualThreads(
            Set<Path> files) {
        final List<SearchInFileMatchFilterCallableAbstract<ContainerInfoSearchFileGitRepo>> searchIOFileCallables =
                new ArrayList<>();
        files.forEach(file -> {
            SearchInFileMatchFilterCallableAbstract<ContainerInfoSearchFileGitRepo> searchFilterCallableAbstract =
                    new SearchIOFileCallable(file);

            searchFilterCallableAbstract.setContainerGitRepoMeta(getContainerGitRepoMeta());
            searchFilterCallableAbstract.setFile(file.toString());
            searchFilterCallableAbstract.setCurrentBranch("master");
            searchFilterCallableAbstract.setIsUseMatcherCounterInFile(getMaxCount() != -1);
            searchFilterCallableAbstract.setMaxCount(getMaxCount());
            searchFilterCallableAbstract.setPattern(getPattern());
            searchFilterCallableAbstract.setContextBeforeReal(getContextBefore() == -1 ? 1 : getContextBefore());
            searchFilterCallableAbstract.setContextAfterReal(getContextAfter() == -1 ? 1 : getContextAfter());

            searchIOFileCallables.add(searchFilterCallableAbstract);
        });
        return searchIOFileCallables;
    }

    /**
     * Get the number of files in the project.
     *
     * @return Count files.
     */
    @Override
    public int getCountFiles() {
        final int countFiles = SearchEngineIOGitWalkingDirectory.COUNT_FILES.get();
        SearchEngineIOGitWalkingDirectory.COUNT_FILES.set(0);
        return countFiles;
    }

}
