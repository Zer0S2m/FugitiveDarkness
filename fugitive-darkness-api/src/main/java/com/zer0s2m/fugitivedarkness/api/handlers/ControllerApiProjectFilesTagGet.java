package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.ProjectFileTag;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileTagRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * The request handler for getting tags to file objects in the project.
 */
public final class ControllerApiProjectFilesTagGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTagGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Getting started getting tags for file objects in a project");

        final ProjectFileTagRepository projectFileTagRepository = new ProjectFileTagRepositoryImpl(event.vertx());

        projectFileTagRepository
                .findAll()
                .onSuccess(ar -> {
                    projectFileTagRepository.closeClient();

                    Collection<ProjectFileTag> projectComments = projectFileTagRepository.mapTo(ar);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("tags", projectComments);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("End of getting tags for file objects in the project");

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
