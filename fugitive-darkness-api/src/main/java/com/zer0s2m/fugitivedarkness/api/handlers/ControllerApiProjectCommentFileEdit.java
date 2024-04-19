package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectCommentFileEdit;
import com.zer0s2m.fugitivedarkness.repository.ProjectCommentRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectCommentRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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
 * A request handler for updating a comment on a file object in the project.
 */
public final class ControllerApiProjectCommentFileEdit implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectCommentFileEdit.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start updating a comment on a file object in the project");

        final ProjectCommentRepository projectCommentRepository = new ProjectCommentRepositoryImpl(event.vertx());
        final long idProjectComment = Long.parseLong(event.pathParam("ID"));
        final ContainerProjectCommentFileEdit containerProjectCommentFileEdit = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectCommentFileEdit.class);

        projectCommentRepository
                .updateTextById(idProjectComment, containerProjectCommentFileEdit.text())
                .onSuccess(ar -> {
                    projectCommentRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("End update of a comment on a file object in the project");

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
                            .requiredProperty("text", stringSchema())))
                    .build();
        }

    }

}
