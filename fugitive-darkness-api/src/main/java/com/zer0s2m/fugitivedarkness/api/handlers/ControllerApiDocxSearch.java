package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerDocxSearch;
import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import com.zer0s2m.fugitivedarkness.provider.docx.ContainerInfoSearchDocxFile;
import com.zer0s2m.fugitivedarkness.provider.docx.DocxManager;
import com.zer0s2m.fugitivedarkness.repository.DocxFileRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.DocxFileRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRepository;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static io.vertx.json.schema.common.dsl.Schemas.*;

final public class ControllerApiDocxSearch implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiDocxSearch.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start searching a docx file");

        final long idDocxFile = Long.parseLong(event.pathParam("ID"));
        final ContainerDocxSearch containerDocxSearch = event
                .body()
                .asJsonObject()
                .mapTo(ContainerDocxSearch.class);
        final DocxFileRepository docxFileRepository = new DocxFileRepositoryImpl(event.vertx());

        docxFileRepository
                .findById(idDocxFile)
                .onSuccess(ar -> {
                    docxFileRepository.closeClient();

                    final JsonObject object = new JsonObject();
                    final DocxFileModel docxFile = docxFileRepository.mapTo(ar).get(0);

                    try (final DocxManager docxManager = DocxManager.create()) {
                        docxManager.setFile(Path.of(docxFile.getPath()));

                        final ContainerInfoSearchDocxFile infoSearchDocxFile =
                                docxManager.search(containerDocxSearch.pattern());

                        object.put("success", true);
                        object.put("searchResult", infoSearchDocxFile);

                        event
                                .response()
                                .setChunked(true)
                                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .setStatusCode(HttpResponseStatus.OK.code())
                                .write(object.toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    logger.info("Finish searching a docx file");

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

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class DocxSearchValidation {

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
                            .requiredProperty("pattern", stringSchema())))
                    .build();
        }

    }

}
