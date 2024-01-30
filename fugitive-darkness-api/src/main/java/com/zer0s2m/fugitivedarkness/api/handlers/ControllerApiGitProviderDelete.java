package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderControl;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for removing git provider.
 */
final public class ControllerApiGitProviderDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitProviderDelete.class);

    /**
     * Remove git provider.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start removing git provider");

        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final ContainerInfoGitProviderControl containerInfoGitProviderDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderControl.class);

        gitProviderRepository
                .deleteByTypeAndTarget(containerInfoGitProviderDelete.type(), containerInfoGitProviderDelete.target())
                .onSuccess(ar -> gitProviderRepository.closeClient())
                .onFailure((fail) -> {
                    logger.error("Failure (DB): " + fail.fillInStackTrace());
                    gitProviderRepository.closeClient();
                });

        event
                .response()
                .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

        logger.info("Finish removing git provider");

        event.next();
    }

    /**
     * Validation before deleting a git provider - checks for the existence of an entry in the database.
     */
    public static class GitProviderDeleteValidationIsExistsInSystem implements Handler<RoutingContext> {

        /**
         * Validation for the existence of a record in the database.
         *
         * @param event The event to handle.
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
            final ContainerInfoGitProviderControl containerInfoGitProviderDelete = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerInfoGitProviderControl.class);

            gitProviderRepository
                    .existsByTypeAndTarget(
                            containerInfoGitProviderDelete.type(),
                            containerInfoGitProviderDelete.target())
                    .onSuccess(ar -> {
                        if (!gitProviderRepository.mapToExistsColumn(ar)) {
                            event.fail(
                                    HttpResponseStatus.NOT_FOUND.code(),
                                    new NotFoundException("Git provider does not exist"));
                        } else {
                            event.next();
                        }

                        gitProviderRepository.closeClient();
                    });
        }

    }

}
