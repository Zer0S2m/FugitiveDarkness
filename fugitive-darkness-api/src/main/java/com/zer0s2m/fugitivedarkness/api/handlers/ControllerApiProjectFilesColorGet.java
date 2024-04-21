package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.ProjectFileColor;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileColorRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * The request handler for getting the color palette of the file in the project.
 */
public final class ControllerApiProjectFilesColorGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesColorGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Getting started getting color palettes for files in the project");

        final ProjectFileColorRepository projectFileColorRepository =
                new ProjectFileColorRepositoryImpl(event.vertx());

        projectFileColorRepository
                .findAll()
                .onSuccess(ar -> {
                    projectFileColorRepository.closeClient();

                    Collection<ProjectFileColor> projectFileColors = projectFileColorRepository.mapTo(ar);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("colors", projectFileColors);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("Finishing getting color palettes for files in the project");

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
