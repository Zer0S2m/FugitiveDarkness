package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.GitFilterRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitFilterRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for removing a search filter.
 */
final public class ControllerApiGitFilterDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitFilterDelete.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start removing a search filter");

        final GitFilterRepository filterRepository = new GitFilterRepositoryImpl(event.vertx());
        final long idGitFilter = Long.parseLong(event.pathParam("ID"));

        filterRepository
                .deleteById(idGitFilter)
                .onSuccess(ar -> {
                    filterRepository.closeClient();

                    logger.info("Finish deleting a search filter");

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .end();
                })
                .onFailure(error -> {
                    filterRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

    /**
     * Request handler to validate search filter existence check.
     */
    public static final class GitFilterCheckIsExists implements Handler<RoutingContext> {

        /**
         * Handler to check if a git repository exists.
         *
         * @param event The event to handle.
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final GitFilterRepository filterRepository = new GitFilterRepositoryImpl(event.vertx());
            final long idGitFilter = Long.parseLong(event.pathParam("ID"));

            filterRepository
                    .existsById(idGitFilter)
                    .onSuccess(ar -> {
                        filterRepository.closeClient();

                        if (!filterRepository.mapToExistsColumn(ar)) {
                            event.fail(
                                    HttpResponseStatus.NOT_FOUND.code(),
                                    new NotFoundException("Git filter search not found."));
                        } else {
                            event.next();
                        }
                    })
                    .onFailure(error -> {
                        filterRepository.closeClient();

                        logger.error("Failure (DB): " + error.fillInStackTrace());

                        event
                                .response()
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end();
                    });
        }

    }

}
