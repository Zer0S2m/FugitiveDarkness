package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoControl;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
import com.zer0s2m.fugitivedarkness.provider.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Request handler to remove a git repository from the system.
 */
final public class ControllerApiGitRepoDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoDelete.class);

    private final GitRepo gitRepo = GitRepo.create();

    /**
     * Delete a repository from the system.
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
            event.vertx()
                    .executeBlocking(() -> {
                        final Path sourceGitRepository = HelperGitRepo.getSourceGitRepository(
                                containerGitRepoDelete.group(), containerGitRepoDelete.project());
                        logger.info("Start deleting a git repository [" + sourceGitRepository + "]");
                        gitRepo.gDelete(containerGitRepoDelete.group(), containerGitRepoDelete.project());
                        logger.info("Finish deleting git repository [" + sourceGitRepository + "]");
                        return null;
                    }, false)
                    .onSuccess(handler -> {
                        final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
                        repositoryGit.deleteByGroupAndProject(
                                containerGitRepoDelete.group(), containerGitRepoDelete.project());
                    });
            event.response()
                    .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                    .end();
        }
    }

}
