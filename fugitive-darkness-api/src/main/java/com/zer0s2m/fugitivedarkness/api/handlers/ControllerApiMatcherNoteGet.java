package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.MatcherNoteRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for getting notes on matches.
 */
final public class ControllerApiMatcherNoteGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiMatcherNoteGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        final MatcherNoteRepository noteRepository = new MatcherNoteRepositoryImpl(event.vertx());

        noteRepository
                .findAll()
                .onSuccess(ar -> {
                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("matcherNotes", noteRepository.mapTo(ar));

                    event.response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    noteRepository.closeClient();

                    event
                            .response()
                            .end();
                })
                .onFailure(error -> {
                    noteRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
