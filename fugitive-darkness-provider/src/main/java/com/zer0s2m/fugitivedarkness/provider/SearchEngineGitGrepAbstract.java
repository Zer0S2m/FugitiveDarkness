package com.zer0s2m.fugitivedarkness.provider;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Abstract class for initializing basic search parameters.
 */
public abstract class SearchEngineGitGrepAbstract implements SearchEngineGitGrep {

    /**
     * A pattern for finding matches in files.
     * <a href="https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---basic-regexp">More about</a>.
     */
    private Pattern pattern;

    /**
     * Pattern for files that will be included in the search.
     */
    private Pattern patternForIncludeFile = null;

    /**
     * Pattern for files that will be excluded from the search.
     */
    private Pattern patternForExcludeFile = null;

    /**
     * Additional Information.
     */
    private ContainerGitRepoMeta containerGitRepoMeta;

    /**
     * File extensions that participated in the search.
     */
    private final Set<String> extensionFilesGrep = new HashSet<>();

    /**
     * The file extension that will be included in file filtering when searching for matches.
     */
    private final Set<String> extensionFilesForIncludeFilterSearch = new HashSet<>();

    /**
     * The file extension that will be excluded from files when searching for matches.
     */
    private final Set<String> extensionFilesForIncludeExcludeSearch = new HashSet<>();

    private Repository repository;

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     * @throws IOException If an IO error occurred.
     */
    protected SearchEngineGitGrepAbstract(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta)
            throws IOException {
        Objects.requireNonNull(pattern, "Pattern needs to be set");
        Objects.requireNonNull(source, "You need to set the path to the git repository");
        Objects.requireNonNull(containerGitRepoMeta, "Additional repository information needs to be set");

        setContainerGitRepoMeta(containerGitRepoMeta);
        setPattern(pattern);
        setGitRepositoryGrep(source);
    }

    /**
     * Get a pattern to search for matches.
     *
     * @return A pattern for finding matches in files.
     */
    @Override
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Set a pattern to search for matches.
     *
     * @param pattern Match pattern.
     */
    @Override
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Get file extensions that participated in the search.
     *
     * @return File extension.
     */
    @Override
    public Set<String> getExtensionFilesGrep() {
        return extensionFilesGrep;
    }

    /**
     * Add extension file.
     *
     * @param fileExtension File extension.
     * @return Whether an object has been added.
     */
    @Override
    public boolean addExtensionFilesGrep(String fileExtension) {
        return extensionFilesGrep.add(fileExtension);
    }

    /**
     * Set the file extension that will be included in the search filtering.
     *
     * @param extensionFiles File extension.
     */
    @Override
    public void setIncludeExtensionFilesForSearchGrep(Set<String> extensionFiles) {
        Objects.requireNonNull(extensionFiles, "File extensions are required");

        extensionFilesForIncludeFilterSearch.addAll(extensionFiles);
    }

    /**
     * Set file extensions that will be included in file filtering when searching for matches.
     *
     * @return File extension.
     */
    @Override
    public Set<String> getIncludeExtensionFilesForSearchGrep() {
        return extensionFilesForIncludeFilterSearch;
    }

    /**
     * Set the file extension to be excluded from search filtering.
     *
     * @param extensionFiles File extension.
     */
    @Override
    public void setExcludeExtensionFilesForSearchGrep(Set<String> extensionFiles) {
        Objects.requireNonNull(extensionFiles, "File extensions are required");

        extensionFilesForIncludeExcludeSearch.addAll(extensionFiles);
    }

    /**
     * Get file extensions that will be excluded from file filtering when searching for matches.
     *
     * @return File extension.
     */
    @Override
    public Set<String> getExcludeExtensionFilesForSearchGrep() {
        return extensionFilesForIncludeExcludeSearch;
    }

    /**
     * Install the git repository via its source path.
     *
     * @param source Source path of the repository.
     * @throws IOException If an IO error occurred.
     */
    @Override
    public void setGitRepositoryGrep(Path source) throws IOException {
        this.repository = Git.open(source.toFile())
                .checkout()
                .getRepository();
    }

    /**
     * Get the git repository.
     *
     * @return Git repository.
     */
    @Override
    public Repository getGitRepositoryGrep() {
        return repository;
    }

    /**
     * Set additional Information.
     *
     * @param containerGitRepoMeta Additional Information.
     */
    @Override
    public void setContainerGitRepoMeta(ContainerGitRepoMeta containerGitRepoMeta) {
        this.containerGitRepoMeta = containerGitRepoMeta;
    }

    /**
     * Get more information.
     *
     * @return Additional Information.
     */
    @Override
    public ContainerGitRepoMeta getContainerGitRepoMeta() {
        return containerGitRepoMeta;
    }

    /**
     * Set a pattern for files that will be included in the search.
     *
     * @param patternForIncludeFile Pattern.
     */
    @Override
    public void setPatternForIncludeFile(Pattern patternForIncludeFile) {
        Objects.requireNonNull(patternForIncludeFile, "The file pattern must not be null");

        this.patternForIncludeFile = patternForIncludeFile;
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
     */
    @Override
    public void setPatternForExcludeFile(Pattern patternForExcludeFile) {
        Objects.requireNonNull(patternForExcludeFile, "The file pattern must not be null");

        this.patternForExcludeFile = patternForExcludeFile;
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

}
