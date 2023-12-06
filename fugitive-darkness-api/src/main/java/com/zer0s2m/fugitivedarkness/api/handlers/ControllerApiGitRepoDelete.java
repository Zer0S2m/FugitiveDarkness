package com.zer0s2m.fugitivedarkness.api.handlers;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Request handler to remove a git repository from the system.
 */
final public class ControllerApiGitRepoDelete implements Handler<RoutingContext> {

    /**
     * Delete a repository from the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        event.response()
                .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                .end();
    }

}
