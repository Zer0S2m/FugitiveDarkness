package com.zer0s2m.fugitivedarkness.provider;

import com.zer0s2m.fugitivedarkness.provider.impl.GitRepoFilterSearchImpl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Filter by searching git repository. Similar to grep command in git.
 */
public interface GitRepoFilterSearch {

    /**
     * Set the path to the git repository for later searching.
     *
     * @param source Path to git repository.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitRepo(Path source);

    /**
     * Set the path to the git repository for later searching.
     *
     * @param source A collection of paths to git repositories.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitRepo(Collection<Path> source);

    /**
     * Add metadata for the repository.
     *
     * @param source Source path of the git repository.
     * @param meta   Git repository information.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitMeta(Path source, ContainerGitRepoMeta meta);

    /**
     * Get metadata for a git repository.
     *
     * @param source Source path of the git repository.
     * @return Git repository information.
     */
    ContainerGitRepoMeta getGitMeta(Path source);

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPattern(String pattern);

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPattern(Pattern pattern);

    /**
     * Get pattern for matches.
     *
     * @return Match pattern.
     */
    Pattern getPattern();

    /**
     * Get a collection of paths to git repositories.
     *
     * @return A collection of paths to git repositories.
     */
    Set<Path> getSources();

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFiles File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setIncludeExtensionFile(Collection<String> extensionFiles);

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFile File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setIncludeExtensionFile(String extensionFile);

    /**
     * Set file extensions that will be included in file filtering when searching for matches.
     *
     * @return File extension.
     */
    Set<String> getIncludeExtensionFile();

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFiles File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setExcludeExtensionFile(Collection<String> extensionFiles);

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFile File extension.
     * @return Search filter.
     */
    GitRepoFilterSearch setExcludeExtensionFile(String extensionFile);

    /**
     * Get file extensions that will be excluded from file filtering when searching for matches.
     *
     * @return File extension.
     */
    Set<String> getExcludeExtensionFile();

    static GitRepoFilterSearch create() {
        return new GitRepoFilterSearchImpl();
    }

}
