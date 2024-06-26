package com.zer0s2m.fugitivedarkness.provider.git;

import com.zer0s2m.fugitivedarkness.provider.git.impl.GitRepoManagerImpl;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Assistant for assembling called functionality - copying repositories.
 */
public interface RepositoryInstallerRemote {

    /**
     * Collect runtime code to clone the repository.
     *
     * @param URI Remote git repository host. Must not be {@literal null}.
     * @return Execution code.
     */
    static Runnable taskInstallRepoRemote(final String URI) {
        return () -> {
            final GitRepoManager gitRepoService = new GitRepoManagerImpl();
            try {
                gitRepoService.gCloneStart(
                        gitRepoService.gCloneCreate(URI),
                        URI
                );
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
