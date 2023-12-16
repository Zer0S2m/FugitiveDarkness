package com.zer0s2m.fugitivedarkness.provider;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.provider.impl.GitRepoImpl;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * Interface for implementing a service that handles git repositories.
 */
public interface GitRepo {

    /**
     * Clone a git repository from a remote host into a set path environment variable {@link Environment#ROOT_PATH_REPO}.
     *
     * @param URI Remote git repository host.
     * @return Git repository information.
     * @throws GitAPIException The exception is caused by the internal functionality of managing git repositories.
     */
    ContainerInfoRepo gClone(String URI) throws GitAPIException;

    void gDelete(String group, String project);

    void search();

    static GitRepo create() {
        return new GitRepoImpl();
    }

}
