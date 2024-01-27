package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.MatcherNoteRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for deleting notes on matches.
 */
final public class ControllerApiMatcherNoteDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiMatcherNoteDelete.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final MatcherNoteRepository noteRepository = new MatcherNoteRepositoryImpl(event.vertx());
        final long idMatcherNote = Long.parseLong(event.pathParam("ID"));

        noteRepository
                .deleteById(idMatcherNote)
                .onSuccess(ar -> {
                    noteRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());
                    event.next();
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
