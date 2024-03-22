package com.zer0s2m.fugitivedarkness.provider.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Abstract class for initializing basic search parameters.
 */
public abstract class SearchEngineGitGrepAbstract extends SearchEngineGrepAbstract implements SearchEngineJGitGrep {

    private Repository repository;

    /**
     * @param pattern              A pattern for finding matches in files.
     * @param source               Source path to the git repository.
     * @param containerGitRepoMeta Additional Information.
     *
     * @throws IOException If an IO error occurred.
     */
    protected SearchEngineGitGrepAbstract(Pattern pattern, Path source, ContainerGitRepoMeta containerGitRepoMeta)
            throws IOException {
        super(pattern, source, containerGitRepoMeta);

        setGitRepositoryGrep(source);
    }

    /**
     * Install the git repository via its source path.
     *
     * @param source Source path of the repository.
     * @throws IOException If an IO error occurred.
     */
    @Override
    public void setGitRepositoryGrep(Path source) throws IOException {
        this.repository = Git.open(HelperGitRepo.cleanPathForGitRepo(source).toFile())
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

}
