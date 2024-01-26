package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.repository.Repository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Request handler to get all providers for git repositories.
 */
final public class ControllerApiGitProviderGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitProviderGet.class);

    /**
     * Getting providers for git repositories.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start getting providers");

        final Repository<RowSet<Row>, GitProviderModel> gitProviderRepository =
                new GitProviderRepositoryImpl(event.vertx());

        gitProviderRepository
                .findAll()
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        final List<GitProviderModel> gitProviders = gitProviderRepository.mapTo(ar.result());

                        JsonObject object = new JsonObject();
                        object.put("success", true);
                        object.put("gitProviders", gitProviders);

                        event.response()
                                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .setStatusCode(HttpResponseStatus.OK.code())
                                .write(object.toString());

                        event
                                .response()
                                .end();

                        gitProviderRepository.closeClient();

                        logger.info("End of receiving providers");
                    } else {
                        gitProviderRepository.closeClient();

                        logger.error("Failure (SYSTEM): " + ar.cause());

                        event.response()
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end();
                    }
                });
    }

}
