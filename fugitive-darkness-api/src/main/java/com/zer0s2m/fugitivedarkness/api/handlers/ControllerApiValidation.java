package com.zer0s2m.fugitivedarkness.api.handlers;

import io.vertx.core.Vertx;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.Parameters;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRepository;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;

import static io.vertx.json.schema.common.dsl.Schemas.*;

final public class ControllerApiValidation {

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitRepoControlValidation {

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
                            .requiredProperty("group", stringSchema())
                            .requiredProperty("project", stringSchema())))
                    .build();
        }

    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class MatcherNoteControlID {

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
                    .pathParameter(Parameters.param("ID", intSchema()))
                    .build();
        }

    }

}
