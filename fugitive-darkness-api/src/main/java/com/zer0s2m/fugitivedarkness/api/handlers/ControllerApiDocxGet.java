package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import com.zer0s2m.fugitivedarkness.repository.DocxFileRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.DocxFileRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Request handler for getting all docx files.
 */
final public class ControllerApiDocxGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiDocxGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.error("Start getting a docx files");

        final DocxFileRepository docxFileRepository = new DocxFileRepositoryImpl(event.vertx());

        docxFileRepository
                .findAll()
                .onSuccess(ar -> {
                    final JsonObject object = new JsonObject();
                    final List<DocxFileModel> docxFiles = docxFileRepository.mapTo(ar);

                    object.put("success", true);
                    object.put("docxFiles", docxFiles);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

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

        logger.info("Finish getting a docx files");
    }

}
