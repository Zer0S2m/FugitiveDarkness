package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.models.DocxFileModel;
import com.zer0s2m.fugitivedarkness.repository.DocxFileRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.DocxFileRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

/**
 * Request handler for uploading a docx file.
 */
final public class ControllerApiDocxUpload implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiDocxUpload.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        List<FileUpload> uploadsFiles = event.fileUploads();
        final DocxFileRepository docxFileRepository = new DocxFileRepositoryImpl(event.vertx());

        uploadsFiles.forEach(fileUpload -> {
            final String fileName = fileUpload.fileName();
            final String fileNameUploaded = fileUpload.uploadedFileName().split("/")[1];
            final Path source = Path.of(
                    FileSystems.getDefault().getPath("").toAbsolutePath().toString(),
                    fileUpload.uploadedFileName());
            final String target = Path.of(Environment.ROOT_PATH_DOCX, fileNameUploaded).toString();

            event
                    .vertx()
                    .fileSystem()
                    .move(source.toString(), target)
                    .onSuccess(ar -> {
                        logger.info(String.format("Success move file: %s -> %s", source, target));

                        final DocxFileModel docxFile = new DocxFileModel(
                                target,
                                fileNameUploaded,
                                fileName);

                        docxFileRepository
                                .save(docxFile)
                                .onSuccess(rows -> {
                                    docxFileRepository.closeClient();

                                    final JsonObject object = new JsonObject();
                                    final DocxFileModel docxFileCreated = docxFileRepository.mapTo(rows).get(0);

                                    object.put("success", true);
                                    object.put("docxFile", docxFileCreated);

                                    event
                                            .response()
                                            .setChunked(true)
                                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                            .setStatusCode(HttpResponseStatus.CREATED.code())
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
                    })
                    .onFailure(error -> {
                        docxFileRepository.closeClient();

                        logger.error("Failure (UPLOAD): " + error.fillInStackTrace());

                        event
                                .response()
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end();
                    });
        });
    }

}
