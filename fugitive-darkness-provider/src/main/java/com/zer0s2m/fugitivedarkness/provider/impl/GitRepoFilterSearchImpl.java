package com.zer0s2m.fugitivedarkness.provider.impl;

import com.zer0s2m.fugitivedarkness.provider.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.GitRepoFilterSearch;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Filter by searching git repository. Similar to grep command in git.
 */
public class GitRepoFilterSearchImpl implements GitRepoFilterSearch {

    private final Set<Path> sources = new HashSet<>();

    private Pattern pattern;

    private final Map<Path, ContainerGitRepoMeta> meta = new HashMap<>();

    /**
     * Set the path to the git repository for later searching.
     *
     * @param source Path to git repository.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch addGitRepo(Path source) {
        sources.add(source);
        return this;
    }

    /**
     * Set the path to the git repository for later searching.
     *
     * @param source A collection of paths to git repositories.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch addGitRepo(Collection<Path> source) {
        sources.addAll(source);
        return this;
    }

    /**
     * Add metadata for the repository.
     *
     * @param source Source path of the git repository.
     * @param meta   Git repository information.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch addGitMeta(Path source, ContainerGitRepoMeta meta) {
        this.meta.put(source, meta);
        return this;
    }

    /**
     * Get metadata for a git repository.
     *
     * @param source Source path of the git repository.
     * @return Git repository information.
     */
    @Override
    public ContainerGitRepoMeta getGitMeta(Path source) {
        return meta.get(source);
    }

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setPattern(String pattern) {
        return setPattern(Pattern.compile(pattern));
    }

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setPattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * Get pattern for matches.
     *
     * @return Match pattern.
     */
    @Override
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Get a collection of paths to git repositories.
     *
     * @return A collection of paths to git repositories.
     */
    @Override
    public Set<Path> getSources() {
        return sources;
    }

}
