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

    private Pattern patternForIncludeFile = null;

    private Pattern patternForExcludeFile = null;

    private int maxCount = -1;

    private final Map<Path, ContainerGitRepoMeta> meta = new HashMap<>();

    private final Set<String> includeExtensionFiles = new HashSet<>();

    private final Set<String> excludeExtensionFiles = new HashSet<>();

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

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFiles File extension.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setIncludeExtensionFile(Collection<String> extensionFiles) {
        Objects.requireNonNull(extensionFiles, "File extensions are required");

        this.includeExtensionFiles.addAll(extensionFiles);
        return this;
    }

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFile File extension.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setIncludeExtensionFile(String extensionFile) {
        Objects.requireNonNull(extensionFile, "File extensions are required");

        this.includeExtensionFiles.add(extensionFile);
        return this;
    }

    /**
     * Set file extensions that will be included in file filtering when searching for matches.
     *
     * @return File extension.
     */
    @Override
    public Set<String> getIncludeExtensionFile() {
        return includeExtensionFiles;
    }

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFiles File extension.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setExcludeExtensionFile(Collection<String> extensionFiles) {
        Objects.requireNonNull(extensionFiles, "File extensions are required");

        this.excludeExtensionFiles.addAll(extensionFiles);
        return this;
    }

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFile File extension.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setExcludeExtensionFile(String extensionFile) {
        Objects.requireNonNull(extensionFile, "File extensions are required");

        this.excludeExtensionFiles.add(extensionFile);
        return this;
    }

    /**
     * Get file extensions that will be excluded from file filtering when searching for matches.
     *
     * @return File extension.
     */
    @Override
    public Set<String> getExcludeExtensionFile() {
        return excludeExtensionFiles;
    }

    /**
     * Set a pattern for files that will be included in the search.
     *
     * @param patternForIncludeFile Pattern.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setPatternForIncludeFile(Pattern patternForIncludeFile) {
        this.patternForIncludeFile = patternForIncludeFile;
        return this;
    }

    /**
     * Get a pattern for files that will be included in the search.
     *
     * @return Pattern.
     */
    @Override
    public Pattern getPatternForIncludeFile() {
        return patternForIncludeFile;
    }

    /**
     * Set a pattern for files that will be excluded from the search.
     *
     * @param patternForExcludeFile Pattern.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setPatternForExcludeFile(Pattern patternForExcludeFile) {
        this.patternForExcludeFile = patternForExcludeFile;
        return this;
    }

    /**
     * Get a template for files that will be excluded from the search.
     *
     * @return Pattern.
     */
    @Override
    public Pattern getPatternForExcludeFile() {
        return patternForExcludeFile;
    }

    /**
     * Set a limit on the number of matches per file.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-countltnumgt">More about</a>.
     *
     * @param maxCount Limit.
     * @return Search filter.
     */
    @Override
    public GitRepoFilterSearch setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    /**
     * Get the limit on the number of matches per file.
     *
     * @return limit
     */
    @Override
    public int getMaxCount() {
        return maxCount;
    }

}
