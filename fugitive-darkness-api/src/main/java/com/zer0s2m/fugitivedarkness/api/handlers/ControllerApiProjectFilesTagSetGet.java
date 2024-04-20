package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.ProjectFileTagSet;
import com.zer0s2m.fugitivedarkness.repository.ProjectFileTagSetRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.ProjectFileTagSetRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public final class ControllerApiProjectFilesTagSetGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTagSetGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("The beginning of getting tags of linked tags to files");

        final ProjectFileTagSetRepository projectFileTagSetRepository =
                new ProjectFileTagSetRepositoryImpl(event.vertx());

        projectFileTagSetRepository
                .findAll()
                .onSuccess(ar -> {
                    projectFileTagSetRepository.closeClient();

                    Collection<ProjectFileTagSet> projectFileTagSets = projectFileTagSetRepository.mapTo(ar);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("filesTag", projectFileTagSets);

                    event.response()
                            .putHeader(
                                    HttpHeaders.CONTENT_LENGTH,
                                    String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("The end of getting tags of linked tags to files");

                    event.next();
                })
                .onFailure(error -> {
                    projectFileTagSetRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

}
