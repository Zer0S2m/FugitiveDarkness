package com.zer0s2m.fugitivedarkness.provider;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
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
     * Additional Information.
     */
    private ContainerGitRepoMeta containerGitRepoMeta;

    /**
     * File extensions that participated in the search.
     */
    private final Set<String> extensionFilesGrep = new HashSet<>();

    private Repository repository;

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     * @throws IOException If an IO error occurred.
     */
    protected SearchEngineGitGrepAbstract(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta)
            throws IOException {
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
}
