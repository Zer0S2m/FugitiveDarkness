package com.zer0s2m.fugitivedarkness.api;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.api.exception.ObjectISExistsInSystemException;
import com.zer0s2m.fugitivedarkness.api.handlers.*;
import com.zer0s2m.fugitivedarkness.common.Environment;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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
                                \tPOST   [/api/v1/operation/search]
                                \tGET    [/api/v1/operation/get-git-repo-provider]
                                \tGET    [/api/v1/git/repo]
                                \tPOST   [/api/v1/git/repo/install]
                                \tDELETE [/api/v1/git/repo/delete]
                                \tPUT    [/api/v1/git/repo/fetch]
                                \tGET    [/api/v1/git/provider]
                                \tDELETE [/api/v1/git/provider/delete]
                                \tPOST   [/api/v1/git/provider/install]""");
                        logger.info("""
                                Setting ENV:
                                \tFD_ROOT_PATH - %s
                                \tFD_ALLOW_ORIGIN - %s""".formatted(
                                Environment.ROOT_PATH_REPO,
                                Environment.FD_ALLOW_ORIGIN
                        ));
                    } else {
                        logger.error("Failed to bind");
                        startPromise.fail(http.cause());
                    }
                });
    }

    /**
     * Install handlers for the router.
     *
     * @param router Primary request processor router.
     */
    private void installingRouterAPI(final @NotNull Router router) {
        router.route()
                .handler(CorsHandler.create()
                        .addOrigins(Environment.FD_ALLOW_ORIGIN)
                        .allowedMethods(Set.of(
                                HttpMethod.GET,
                                HttpMethod.DELETE,
                                HttpMethod.POST,
                                HttpMethod.PUT,
                                HttpMethod.OPTIONS)));
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
                .handler(new ControllerApiGitRepoInstall.GitRepoInstallCheckIsExists())
                .handler(new ControllerApiGitRepoInstall());
        router
                .delete("/api/v1/git/repo/delete")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitRepoControlValidation.validator(vertx))
                .handler(new ControllerApiGitRepoDelete());
        router
                .put("/api/v1/git/repo/fetch")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitRepoControlValidation.validator(vertx))
                .handler(new ControllerApiGitRepoFetch());
        router
                .post("/api/v1/operation/search")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoSearch.GitRepoSearchValidation.validator(vertx))
                .handler(new ControllerApiGitRepoSearch());
        router
                .get("/api/v1/operation/get-git-repo-provider")
                .handler(ControllerApiGitRepoProvider.GitRepoProviderValidation.validator(vertx))
                .handler(new ControllerApiGitRepoProvider());
        router
                .get("/api/v1/git/provider")
                .handler(new ControllerApiGitProviderGet());
        router
                .delete("/api/v1/git/provider/delete")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitProviderDelete.GitProviderDeleteValidation.validator(vertx))
                .handler(new ControllerApiGitProviderDelete.GitProviderDeleteValidationIsExistsInSystem())
                .handler(new ControllerApiGitProviderDelete());
        router
                .post("/api/v1/git/provider/install")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitProviderInstall.GitProviderInstallValidation.validator(vertx))
                .handler(new ControllerApiGitProviderInstall.GitProviderInstallValidationIsOrgAndIsUser())
                .handler(new ControllerApiGitProviderInstall.GitProviderInstallValidationIsExistsInSystem())
                .handler(new ControllerApiGitProviderInstall.GitProviderInstallValidationIsExistsInExternalSystem())
                .handler(new ControllerApiGitProviderInstall());
    }

    /**
     * Install handlers errors for the router.
     *
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
            } else if (ctx.failure() instanceof ObjectISExistsInSystemException) {
                Buffer errorMsg = ((ObjectISExistsInSystemException) ctx.failure()).toJson().toBuffer();
                ctx
                        .response()
                        .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(errorMsg.length()))
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .write(errorMsg);
                ctx
                        .response()
                        .end();
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
