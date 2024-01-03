package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
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
 * Request handler for fetching git repositories.
 */
final public class ControllerApiGitRepoGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoGet.class);

    /**
     * Get all git repositories.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start getting");

        final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());

        gitRepoRepository
                .findAll()
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        final List<GitRepoModel> gitRepositories = gitRepoRepository.mapTo(ar.result());
                        JsonObject object = new JsonObject();
                        object.put("success", true);
                        object.put("gitRepositories", gitRepositories);

                        event.response()
                                .putHeader(
                                        HttpHeaders.CONTENT_LENGTH,
                                        String.valueOf(object.toString().length()))
                                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .setStatusCode(HttpResponseStatus.OK.code())
                                .write(object.toString());

                        event.response()
                                .end();

                        logger.info("Finish getting");
                    } else {
                        logger.error("Failure: " + ar.cause());
                        event.response()
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end();
                    }
                });
    }

}
