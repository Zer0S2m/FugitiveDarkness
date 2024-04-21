package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileColorRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The request handler for deleting the color palette of a file in the project.
 */
public final class ControllerApiProjectFilesColorDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesColorDelete.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start deleting the color palette for files in the project");

        final ProjectFileColorRepository projectFileColorRepository =
                new ProjectFileColorRepositoryImpl(event.vertx());
        final long idProjectColor = Long.parseLong(event.pathParam("ID"));

        projectFileColorRepository
                .deleteById(idProjectColor)
                .onSuccess(ar -> {
                    projectFileColorRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("End deleting the color palette for files in the project");

                    event.next();
                })
                .onFailure(error -> {
                    projectFileColorRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
