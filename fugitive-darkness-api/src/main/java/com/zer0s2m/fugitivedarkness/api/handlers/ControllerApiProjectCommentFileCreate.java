package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectCommentFileCreate;
import com.zer0s2m.fugitivedarkness.models.ProjectComment;
import com.zer0s2m.fugitivedarkness.repository.ProjectCommentRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectCommentRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRepository;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * A request handler for creating a comment on a file object in the project.
 */
public final class ControllerApiProjectCommentFileCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectCommentFileCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start creating a comment for the project file");

        final ProjectCommentRepository projectCommentRepository = new ProjectCommentRepositoryImpl(event.vertx());
        final ContainerProjectCommentFileCreate containerProjectCommentFileCreate = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectCommentFileCreate.class);
        final ProjectComment projectComment = new ProjectComment(
                containerProjectCommentFileCreate.file(),
                containerProjectCommentFileCreate.text(),
                containerProjectCommentFileCreate.gitRepositoryId());

        projectCommentRepository
                .save(projectComment)
                .onSuccess(ar -> {
                    projectCommentRepository.closeClient();

                    ProjectComment projectCommentCreated = projectCommentRepository
                            .mapTo(ar)
                            .get(0);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("comment", projectCommentCreated);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("Endings creating a comment for the project file");

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

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class ProjectCommentFileValidation {

        /**
         * Get validation handler for incoming body.
         *
         * @param vertx App.
         * @return Incoming body handler.
         */
        public static ValidationHandler validator(Vertx vertx) {
            return ValidationHandlerBuilder
                    .create(SchemaParser.createDraft7SchemaParser(
                            SchemaRouter.create(vertx, new SchemaRouterOptions())))
                    .body(Bodies.json(objectSchema()
                            .requiredProperty("text", stringSchema())
                            .requiredProperty("file", stringSchema())
                            .requiredProperty("gitRepositoryId", intSchema())))
                    .build();
        }

    }

}
