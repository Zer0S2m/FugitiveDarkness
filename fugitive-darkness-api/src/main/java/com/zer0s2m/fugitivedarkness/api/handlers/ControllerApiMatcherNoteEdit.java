package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.MatcherNoteRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
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
 * Request handler for editing notes on matches.
 */
final public class ControllerApiMatcherNoteEdit implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiMatcherNoteEdit.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        final MatcherNoteRepository noteRepository = new MatcherNoteRepositoryImpl(event.vertx());
        final long idMatcherNote = Long.parseLong(event.pathParam("ID"));
        RequestParameters parameters = event.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        JsonObject body = parameters.body().getJsonObject();

        noteRepository
                .updateValueById(idMatcherNote, body.getString("value"))
                .onSuccess(ar -> {
                    noteRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                            .end();
                })
                .onFailure(error -> {
                    noteRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class MatcherNoteValidation {

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
                            .requiredProperty("value", stringSchema())))
                    .build();
        }

    }

}
