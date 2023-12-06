package com.zer0s2m.fugitivedarkness.api.handlers;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Request handler for installing a git repository on the system.
 */
final public class ControllerApiGitRepoInstall implements Handler<RoutingContext> {

    /**
     * Install the repository on the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        JsonObject object = new JsonObject();
        object.put("key", "value");

        event.response()
                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(HttpResponseStatus.CREATED.code())
                .write(object.toString());

        event
                .response()
                .end();
    }

}
