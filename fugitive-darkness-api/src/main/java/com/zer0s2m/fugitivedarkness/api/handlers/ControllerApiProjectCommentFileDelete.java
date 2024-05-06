package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.repository.ProjectCommentRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectCommentRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A request handler for deleting a comment on a file object in the project.
 */
public final class ControllerApiProjectCommentFileDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectCommentFileDelete.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start deleting a comment of a file object in the project");

        final ProjectCommentRepository projectCommentRepository = new ProjectCommentRepositoryImpl(event.vertx());
        final long idProjectComment = Long.parseLong(event.pathParam("ID"));

        projectCommentRepository
                .deleteById(idProjectComment)
                .onSuccess(ar -> {
                    projectCommentRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("End deleting a comment of a file object in the project");

                    event.next();
                })
                .onFailure(error -> {
                    projectCommentRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
