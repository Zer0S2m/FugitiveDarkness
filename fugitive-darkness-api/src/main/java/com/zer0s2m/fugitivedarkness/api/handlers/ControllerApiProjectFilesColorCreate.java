package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectFileColorCreate;
import com.zer0s2m.fugitivedarkness.models.ProjectFileColor;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileColorRepositoryImpl;
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
 * The request handler for creating the color palette of a file in the project.
 */
public final class ControllerApiProjectFilesColorCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesColorCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Starting to create a color palette for files in a project");

        final ProjectFileColorRepository projectFileColorRepository =
                new ProjectFileColorRepositoryImpl(event.vertx());
        final ContainerProjectFileColorCreate containerProjectFileColorCreate = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectFileColorCreate.class);

        projectFileColorRepository
                .save(new ProjectFileColor(
                        containerProjectFileColorCreate.color(),
                        containerProjectFileColorCreate.gitRepositoryId()))
                .onSuccess(ar -> {
                    projectFileColorRepository.closeClient();

                    ProjectFileColor projectFileColorCreated = projectFileColorRepository.mapTo(ar).get(0);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("color", projectFileColorCreated);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("The end of creating a color palette for files in the project");

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

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class ProjectFilesColorValidation {

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
                            .requiredProperty("color", stringSchema())
                            .requiredProperty("gitRepositoryId", intSchema())))
                    .build();
        }

    }

}
