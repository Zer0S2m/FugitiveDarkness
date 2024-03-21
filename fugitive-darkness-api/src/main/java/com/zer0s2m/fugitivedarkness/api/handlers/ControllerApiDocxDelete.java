package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import com.zer0s2m.fugitivedarkness.repository.DocxFileRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.DocxFileRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The handler for the request to delete the docx file.
 */
public class ControllerApiDocxDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiDocxDelete.class);

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
                .deleteById(idDocxFile)
                .onSuccess(ar -> {
                    docxFileRepository.closeClient();

                    final DocxFileModel docxFileCreated = docxFileRepository.mapTo(ar).get(0);

                    event
                            .vertx()
                            .fileSystem()
                            .delete(docxFileCreated.getPath())
                            .onFailure(error -> logger.error("Failure (FILE_SYSTEM): " + error.fillInStackTrace()));

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

                    event.next();
                })
                .onFailure(error -> {
                    docxFileRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
