package com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider;

import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoRepo;
import com.zer0s2m.fugitivedarkness.provider.git.RepositoryInstallerRemote;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for calling the command for cloning {@link RepositoryInstallerRemote#taskInstallRepoRemote(String)}
 * a repository and its subsequent saving in the database.
 */
public class RepositoryInstallerRemoteVertx {

    static private final Logger logger = LoggerFactory.getLogger(RepositoryInstallerRemoteVertx.class);

    private final Vertx vertx;

    public RepositoryInstallerRemoteVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * Call the repository to be cloned and saved to the database.
     *
     * @param runnableClone {@link RepositoryInstallerRemote#taskInstallRepoRemote(String)}.
     * @param containerInfoRepo Repository information.
     * @return Executable code
     */
    public Runnable runCloneAndSaveDB(Runnable runnableClone, ContainerInfoRepo containerInfoRepo) {
        return () -> {
            final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(vertx);

            runnableClone.run();

            gitRepoRepository
                    .updateIsLoadByGroupAndProject(containerInfoRepo.group(), containerInfoRepo.project(), true)
                    .onFailure((error) -> logger.error("Failure (DB): " + error.fillInStackTrace()));
        };
    }

}
