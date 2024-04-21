package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.ProjectCommentRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectCommentRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Request handler for validation for the existence of a comment on a file object in the project.
 */
public final class ProjectCommentValidationExists implements Handler<RoutingContext> {

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        final ProjectCommentRepository projectCommentRepository = new ProjectCommentRepositoryImpl(event.vertx());
        final long idDocxFile = Long.parseLong(event.pathParam("ID"));

        projectCommentRepository
                .existsById(idDocxFile)
                .onSuccess(ar -> {
                    projectCommentRepository.closeClient();

                    if (!projectCommentRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("The comment to the file object was not found"));
                    } else {
                        event.next();
                    }
                })
                .onFailure(error -> {
                    projectCommentRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
