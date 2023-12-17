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
     * @param source Path to git repository.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitRepo(Path source);

    /**
     * Set the path to the git repository for later searching.
     * @param source A collection of paths to git repositories.
     * @return Search filter.
     */
    GitRepoFilterSearch addGitRepo(Collection<Path> source);

    /**
     * Set a pattern to search for matches.
     * @param pattern Match pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPattern(String pattern);

    /**
     * Set a pattern to search for matches.
     * @param pattern Match pattern.
     * @return Search filter.
     */
    GitRepoFilterSearch setPattern(Pattern pattern);

    /**
     * Get pattern for matches.
     * @return Match pattern.
     */
    Pattern getPattern();

    /**
     * Get a collection of paths to git repositories.
     * @return A collection of paths to git repositories.
     */
    Set<Path> getSources();

    static GitRepoFilterSearch create() {
        return new GitRepoFilterSearchImpl();
    }

}
