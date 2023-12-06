package com.zer0s2m.fugitivedarkness.api;

import com.zer0s2m.fugitivedarkness.api.handlers.ControllerApiGitRepoDelete;
import com.zer0s2m.fugitivedarkness.api.handlers.ControllerApiGitRepoInstall;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FugitiveDarknessApp extends AbstractVerticle {

    static private final Logger logger = LoggerFactory.getLogger(FugitiveDarknessApp.class);

    /**
     * Start app.
     */
    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        installingRouterAPI(router);

        server
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        logger.info("Starting a server on a port: 8080");
                        logger.info("""
                                Setting routes:
                                \tPOST [/api/v1/git/repo/install]
                                \tDELETE [/api/v1/git/repo/delete]""");
                    } else {
                        logger.error("Failed to bind");
                        startPromise.fail(http.cause());
                    }
                });
    }

    /**
     * Install handlers for the router.
     * @param router Primary request processor router.
     */
    private static void installingRouterAPI(final @NotNull Router router) {
        router
                .post("/api/v1/git/repo/install")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(new ControllerApiGitRepoInstall());
        router
                .delete("/api/v1/git/repo/delete")
                .handler(new ControllerApiGitRepoDelete());
    }

}
