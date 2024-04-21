package com.zer0s2m.fugitivedarkness.api.handlers.validation;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileColorRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileColorRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProjectFileColorValidationExists implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ProjectFileColorValidationExists.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        final ProjectFileColorRepository projectFileColorRepository =
                new ProjectFileColorRepositoryImpl(event.vertx());
        final long idProjectColorId = Long.parseLong(event.pathParam("ID"));

        projectFileColorRepository
                .existsById(idProjectColorId)
                .onSuccess(ar -> {
                    projectFileColorRepository.closeClient();

                    if (!projectFileColorRepository.mapToExistsColumn(ar)) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("The color to the file object was not found"));
                    } else {
                        event.next();
                    }
                })
                .onFailure(error -> {
                    projectFileColorRepository.closeClient();

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
