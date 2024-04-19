package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.ProjectComment;
import com.zer0s2m.fugitivedarkness.repository.ProjectCommentRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectCommentRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * A request handler for getting comments on file objects in the project.
 */
public final class ControllerApiProjectCommentFileGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectCommentFileGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("The beginning of receiving comments on file objects in the project");

        final ProjectCommentRepository projectCommentRepository = new ProjectCommentRepositoryImpl(event.vertx());

        projectCommentRepository
                .findAll()
                .onSuccess(ar -> {
                    projectCommentRepository.closeClient();

                    Collection<ProjectComment> projectComments = projectCommentRepository.mapTo(ar);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("comments", projectComments);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("The end of receiving comments on file objects in the project");

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
