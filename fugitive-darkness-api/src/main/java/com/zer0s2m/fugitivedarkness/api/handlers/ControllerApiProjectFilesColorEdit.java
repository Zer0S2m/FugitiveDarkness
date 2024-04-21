package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectFileColorEdit;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileColorRepositoryImpl;
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
 * A request handler for editing the color palette of a file in the project.
 */
public final class ControllerApiProjectFilesColorEdit implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesColorEdit.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start editing the color palette for files in the project");

        final ProjectFileColorRepository projectFileColorRepository =
                new ProjectFileColorRepositoryImpl(event.vertx());
        final ContainerProjectFileColorEdit containerProjectFileColorEdit = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectFileColorEdit.class);
        final long idProjectColor = Long.parseLong(event.pathParam("ID"));

        projectFileColorRepository
                .updateColorById(idProjectColor, containerProjectFileColorEdit.color())
                .onSuccess(ar -> {
                    projectFileColorRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("Finishing editing the color palette for files in the project");

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
                            .requiredProperty("color", stringSchema())))
                    .build();
        }

    }

}
