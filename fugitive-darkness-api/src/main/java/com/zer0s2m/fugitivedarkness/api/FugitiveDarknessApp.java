package com.zer0s2m.fugitivedarkness.api;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.api.handlers.ControllerApiGitRepoDelete;
import com.zer0s2m.fugitivedarkness.api.handlers.ControllerApiGitRepoGet;
import com.zer0s2m.fugitivedarkness.api.handlers.ControllerApiGitRepoInstall;
import com.zer0s2m.fugitivedarkness.api.handlers.ControllerApiGitRepoSearch;
import com.zer0s2m.fugitivedarkness.common.Environment;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
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
        installingHandlerErrorsAPI(router);

        server
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        logger.info("Starting a server on a port: 8080");
                        logger.info("""
                                Setting routes:
                                \tPOST   [/api/v1/control/search]
                                \tGET    [/api/v1/git/repo/]
                                \tPOST   [/api/v1/git/repo/install]
                                \tDELETE [/api/v1/git/repo/delete]""");
                        logger.info("""
                                Setting ENV:
                                \tFD_ROOT_PATH - %s""".formatted(Environment.ROOT_PATH_REPO));
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
    private void installingRouterAPI(final @NotNull Router router) {
        router
                .get("/api/v1/git/repo")
                .handler(new ControllerApiGitRepoGet());
        router
                .post("/api/v1/git/repo/install")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoInstall.GitRepoInstallValidation.validator(vertx))
                .handler(new ControllerApiGitRepoInstall());
        router
                .delete("/api/v1/git/repo/delete")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoDelete.GitRepoDeleteValidation.validator(vertx))
                .handler(new ControllerApiGitRepoDelete());
        router
                .post("/api/v1/operation/search")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoSearch.GitRepoSearchValidation.validator(vertx))
                .handler(new ControllerApiGitRepoSearch());
    }

    /**
     * Install handlers errors for the router.
     * @param router Primary request processor router.
     */
    private void installingHandlerErrorsAPI(final @NotNull Router router) {
        router.errorHandler(400, ctx -> {
            if (ctx.failure() instanceof BadRequestException) {
                if (ctx.failure() instanceof BodyProcessorException) {
                    Buffer errorMsg = ((BodyProcessorException) ctx.failure()).toJson().toBuffer();
                    ctx
                            .response()
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(errorMsg.length()))
                            .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                            .write(errorMsg);
                    ctx
                            .response()
                            .end();
                }
            }
        });
        router.errorHandler(404, ctx -> {
            if (ctx.failure() instanceof NotFoundException) {
                Buffer errorMsg = ((NotFoundException) ctx.failure()).toJson().toBuffer();
                ctx
                        .response()
                        .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(errorMsg.length()))
                        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
                        .write(errorMsg);
                ctx
                        .response()
                        .end();
            }
        });
    }

}
