package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.DocxFileRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.DocxFileRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Validation for the existence of a docx file.
 */
public class DocxFileValidationExists implements Handler<RoutingContext> {

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final DocxFileRepository docxFileRepository = new DocxFileRepositoryImpl(event.vertx());
        final long idDocxFile = Long.parseLong(event.pathParam("ID"));

        docxFileRepository
                .existsById(idDocxFile)
                .onSuccess(ar -> {
                    docxFileRepository.closeClient();

                    if (!docxFileRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("Docx file not found."));
                    } else {
                        event.next();
                    }
                })
                .onFailure(error -> {
                    docxFileRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}