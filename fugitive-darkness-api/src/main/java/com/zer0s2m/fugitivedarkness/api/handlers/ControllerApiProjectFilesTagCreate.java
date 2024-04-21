package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectFileTagCreate;
import com.zer0s2m.fugitivedarkness.models.ProjectFileTag;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileTagRepositoryImpl;
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
 * A request handler for creating tags for file objects in the project.
 */
public final class ControllerApiProjectFilesTagCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTagCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Starting to create a tag for file objects in a project");

        final ContainerProjectFileTagCreate containerProjectFileTagCreate = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectFileTagCreate.class);
        final ProjectFileTagRepository projectFileTagRepository = new ProjectFileTagRepositoryImpl(event.vertx());

        projectFileTagRepository
                .save(new ProjectFileTag(
                        containerProjectFileTagCreate.title(),
                        containerProjectFileTagCreate.gitRepositoryId()))
                .onSuccess(ar -> {
                    projectFileTagRepository.closeClient();

                    ProjectFileTag projectFileTag = projectFileTagRepository
                            .mapTo(ar)
                            .get(0);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("comment", projectFileTag);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("End of tag creation for file objects in the project");

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

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class ProjectFilesTagValidation {

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
                            .requiredProperty("title", stringSchema())
                            .requiredProperty("gitRepositoryId", intSchema())))
                    .build();
        }

    }

}
