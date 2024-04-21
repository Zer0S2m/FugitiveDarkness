package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileTagRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProjectFileTagValidationExists implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ProjectFileTagValidationExists.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        final ProjectFileTagRepository projectFileTagRepository = new ProjectFileTagRepositoryImpl(event.vertx());
        final long idProjectFileId = Long.parseLong(event.pathParam("ID"));

        projectFileTagRepository
                .existsById(idProjectFileId)
                .onSuccess(ar -> {
                    projectFileTagRepository.closeClient();

                    if (!projectFileTagRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("The tag to the file object was not found"));
                    } else {
                        event.next();
                    }
                })
                .onFailure(error -> {
                    projectFileTagRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
