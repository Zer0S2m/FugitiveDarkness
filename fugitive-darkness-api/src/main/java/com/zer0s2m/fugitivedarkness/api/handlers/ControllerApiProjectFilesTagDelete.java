package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileTagRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The request handler for deleting tags to file objects in the project.
 */
public final class ControllerApiProjectFilesTagDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTagDelete.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Starting to delete a tag for file objects in a project");

        final long idProjectTag = Long.parseLong(event.pathParam("ID"));
        final ProjectFileTagRepository projectFileTagRepository = new ProjectFileTagRepositoryImpl(event.vertx());

        projectFileTagRepository
                .deleteById(idProjectTag)
                .onSuccess(ar -> {
                    projectFileTagRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("End of tag delete for file objects in the project");

                    event.next();
                })
                .onFailure(error -> {
                    projectFileTagRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
