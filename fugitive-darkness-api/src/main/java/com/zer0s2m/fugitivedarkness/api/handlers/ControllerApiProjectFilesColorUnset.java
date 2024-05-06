package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectFileColorSetControl;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorSetRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileColorSetRepositoryImpl;
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
 * The request handler for untying the color palette to a file in the project.
 */
public final class ControllerApiProjectFilesColorUnset implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesColorUnset.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("The beginning of untying the file to a project color");

        final ContainerProjectFileColorSetControl containerProjectFileColorSetControl = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectFileColorSetControl.class);
        final ProjectFileColorSetRepository projectFileColorSetRepository =
                new ProjectFileColorSetRepositoryImpl(event.vertx());

        projectFileColorSetRepository
                .unsetColorByIdToFile(
                        containerProjectFileColorSetControl.colorId(),
                        containerProjectFileColorSetControl.file())
                .onSuccess(ar -> {
                    projectFileColorSetRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("The end of untying the file to the project color");

                    event.next();
                })
                .onFailure(error -> {
                    projectFileColorSetRepository.closeClient();

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
    public static class ProjectFilesColorUnsetValidation {

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
                            .requiredProperty("file", stringSchema())
                            .requiredProperty("colorId", intSchema())))
                    .build();
        }

    }

}
