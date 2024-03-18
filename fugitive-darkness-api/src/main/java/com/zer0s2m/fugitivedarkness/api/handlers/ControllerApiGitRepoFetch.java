package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoControl;
import com.zer0s2m.fugitivedarkness.provider.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.WorkerExecutor;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Request handler for updating the repository.
 */
final public class ControllerApiGitRepoFetch implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoFetch.class);

    private final GitRepoManager gitRepo = GitRepoManager.create();

    /**
     * Updates repository from remote.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        ContainerGitRepoControl containerGitRepoDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitRepoControl.class);
        boolean isExistsGitRepository = HelperGitRepo.existsGitRepository(
                containerGitRepoDelete.group(), containerGitRepoDelete.project());

        if (!isExistsGitRepository) {
            event.fail(
                    HttpResponseStatus.NOT_FOUND.code(),
                    new NotFoundException("Object not found in the system"));
        } else {
            final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
            WorkerExecutor executor = event
                    .vertx()
                    .createSharedWorkerExecutor(
                            "worker.git.operation-updating",
                            4,
                            5,
                            TimeUnit.MINUTES);

            repositoryGit
                    .updateIsLoadByGroupAndProject(
                            containerGitRepoDelete.group(),
                            containerGitRepoDelete.project(),
                            false)
                    .onSuccess(arDB1 -> executor
                            .executeBlocking(() -> {
                                logger.info(String.format(
                                        "Start updating the repository [%s/%s]",
                                        containerGitRepoDelete.group(), containerGitRepoDelete.project()
                                ));

                                gitRepo.gFetch(containerGitRepoDelete.group(), containerGitRepoDelete.project());

                                logger.info(String.format(
                                        "End of repository update [%s/%s]",
                                        containerGitRepoDelete.group(), containerGitRepoDelete.project()
                                ));

                                return null;
                            }, false)
                            .onFailure(error -> {
                                repositoryGit.closeClient();
                                logger.error("Failure (GIT): " + error.fillInStackTrace());
                            })
                            .onSuccess(ar -> repositoryGit.updateIsLoadByGroupAndProject(
                                            containerGitRepoDelete.group(),
                                            containerGitRepoDelete.project(),
                                            true)
                                    .onFailure(error -> {
                                        repositoryGit.closeClient();
                                        logger.error("Failure (DB 1): " + error.fillInStackTrace());
                                    })
                                    .onSuccess((ar2) -> repositoryGit.closeClient()))
                    .onFailure(error -> {
                        repositoryGit.closeClient();
                        logger.error("Failure (DB 2): " + error.fillInStackTrace());
                    }));

            event
                    .response()
                    .setStatusCode(HttpResponseStatus.NO_CONTENT.code());
            event.next();
        }
    }

}
