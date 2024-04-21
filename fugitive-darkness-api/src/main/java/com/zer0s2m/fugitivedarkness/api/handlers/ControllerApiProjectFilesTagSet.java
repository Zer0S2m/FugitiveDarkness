package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectFileTagSetControl;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagSetRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileTagSetRepositoryImpl;
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

public final class ControllerApiProjectFilesTagSet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTagSet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("The beginning of linking a file to a project tag");

        final ContainerProjectFileTagSetControl containerProjectFileTagSetControl = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectFileTagSetControl.class);
        final ProjectFileTagSetRepository projectFileTagSetRepository =
                new ProjectFileTagSetRepositoryImpl(event.vertx());

        projectFileTagSetRepository
                .setTagByIdToFile(
                        containerProjectFileTagSetControl.tagId(),
                        containerProjectFileTagSetControl.file())
                .onSuccess(ar -> {
                    projectFileTagSetRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    logger.info("The end of linking the file to the project tag");

                    event.next();
                })
                .onFailure(error -> {
                    projectFileTagSetRepository.closeClient();

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
    public static class ProjectFilesTagSetValidation {

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
                            .requiredProperty("tagId", intSchema())))
                    .build();
        }

    }

}
