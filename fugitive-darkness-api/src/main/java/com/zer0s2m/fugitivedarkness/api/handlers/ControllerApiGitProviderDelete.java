package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderControl;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request handler for removing git provider.
 */
final public class ControllerApiGitProviderDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitProviderDelete.class);

    /**
     * Remove git provider.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start removing git provider");

        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final ContainerInfoGitProviderControl containerInfoGitProviderDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderControl.class);

        gitProviderRepository
                .deleteByTypeAndTarget(containerInfoGitProviderDelete.type(), containerInfoGitProviderDelete.target())
                .onSuccess(ar -> gitProviderRepository.closeClient())
                .onFailure((fail) -> {
                    logger.error("Failure (DB): " + fail.fillInStackTrace());
                    gitProviderRepository.closeClient();
                });

        event
                .response()
                .setStatusCode(HttpResponseStatus.NO_CONTENT.code());

        logger.info("Finish removing git provider");

        event.next();
    }

}
