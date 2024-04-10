package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoControl;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.git.HelperGitRepo;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The request handler for unpacking the git project.
 */
final public class ControllerApiGitRepoCheckout implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoCheckout.class);

    private final GitRepoManager gitRepoManager = GitRepoManager.create();

    /**
     * Unpacking a git project.
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
            final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());

            logger.info("The beginning of unpacking a git project [{}:{}]",
                    containerGitRepoDelete.group(), containerGitRepoDelete.project());

            try {
                gitRepoManager.gCheckout(containerGitRepoDelete.group(), containerGitRepoDelete.project());

                gitRepoRepository
                        .updateIsUnpackingByGroupAndProject(
                                containerGitRepoDelete.group(),
                                containerGitRepoDelete.project(),
                                true)
                        .onFailure(error -> logger.error("Failure (DB): " + error.getCause()));
            } catch (IOException | GitAPIException e) {
                gitRepoRepository
                        .updateIsUnpackingByGroupAndProject(
                                containerGitRepoDelete.group(),
                                containerGitRepoDelete.project(),
                                false)
                        .onFailure(error -> logger.error("Failure (DB): " + error.getCause()));
                throw new RuntimeException(e);
            }

            logger.info("The end of unpacking the git project [{}:{}]",
                    containerGitRepoDelete.group(), containerGitRepoDelete.project());

            event
                    .response()
                    .setStatusCode(HttpResponseStatus.NO_CONTENT.code());
            event.next();
        }
    }

}
