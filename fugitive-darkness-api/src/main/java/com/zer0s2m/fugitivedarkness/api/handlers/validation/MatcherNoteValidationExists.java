package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.MatcherNoteRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for validating the existence of a note by match in the database.
 */
final public class MatcherNoteValidationExists implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(MatcherNoteValidationExists.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle (@NotNull RoutingContext event) {
        final MatcherNoteRepository noteRepository = new MatcherNoteRepositoryImpl(event.vertx());
        final long idMatcherNote = Long.parseLong(event.pathParam("ID"));

        noteRepository
                .existsById(idMatcherNote)
                .onSuccess(ar -> {
                    if (!noteRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("Note matcher not found."));
                    } else {
                        event.next();
                    }
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