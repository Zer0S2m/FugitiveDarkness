package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.GitJobRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitJobRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler to validate search filter existence check.
 */
public class GitJobValidationExists implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(GitJobValidationExists.class);

    /**
     * Handler to check if a git repository exists.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final GitJobRepository gitJobRepository = new GitJobRepositoryImpl(event.vertx());
        final long idGitJob = Long.parseLong(event.pathParam("ID"));

        gitJobRepository
                .existsById(idGitJob)
                .onSuccess(ar -> {
                    gitJobRepository.closeClient();

                    if (!gitJobRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("Git job search not found."));
                    } else {
                        event.next();
                    }
                })
                .onFailure(error -> {
                    gitJobRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}