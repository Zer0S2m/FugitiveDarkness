package com.zer0s2m.fugitivedarkness.api;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.api.exception.ObjectISExistsInSystemException;
import com.zer0s2m.fugitivedarkness.api.handlers.*;
import com.zer0s2m.fugitivedarkness.api.handlers.logger.HandlerLogger;
import com.zer0s2m.fugitivedarkness.api.handlers.validation.MatcherNoteValidationExists;
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
import io.vertx.ext.web.validation.ParameterProcessorException;
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
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/search]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/operation/get-git-repo-provider]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/get-file-from-git]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/load-git-repo-remote]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/repo]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/repo/install]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/repo/delete]
                                \t\u001b[43mPUT\u001b[0m    [/api/v1/git/repo/fetch]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/matcher/note]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/matcher/note]
                                \t\u001b[43mPUT\u001b[0m    [/api/v1/git/matcher/note/:ID]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/matcher/note/:ID]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/filter/search]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/filter/search]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/filter/search/:ID]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/provider]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/provider/delete]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/provider/install]""");
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
                                HttpMethod.OPTIONS)))
                .handler(new HandlerLogger.HandlerLoggerRequest());
        router
                .get("/api/v1/git/repo")
                .handler(new ControllerApiGitRepoGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/git/repo/install")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoInstall.GitRepoInstallValidation.validator(vertx))
                .handler(new ControllerApiGitRepoInstall.GitRepoInstallCheckIsExists())
                .handler(new ControllerApiGitRepoInstall())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/git/repo/delete")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitRepoControlValidation.validator(vertx))
                .handler(new ControllerApiGitRepoDelete())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .put("/api/v1/git/repo/fetch")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitRepoControlValidation.validator(vertx))
                .handler(new ControllerApiGitRepoFetch())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/operation/search")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoSearch.GitRepoSearchValidation.validator(vertx))
                .handler(new ControllerApiGitRepoSearch())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .get("/api/v1/operation/get-git-repo-provider")
                .handler(ControllerApiGitRepoProvider.GitRepoProviderValidation.validator(vertx))
                .handler(new ControllerApiGitRepoProvider())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/operation/load-git-repo-remote")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitProviderControlValidation.validator(vertx))
                .handler(new ControllerApiGitRepoInstallRemote())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .get("/api/v1/git/provider")
                .handler(new ControllerApiGitProviderGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/operation/get-file-from-git")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoGetFileContent.GitRepoGetFileValidation.validator(vertx))
                .handler(new ControllerApiGitRepoGetFileContent())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/git/provider/delete")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitProviderControlValidation.validator(vertx))
                .handler(new ControllerApiGitProviderDelete.GitProviderDeleteValidationIsExistsInSystem())
                .handler(new ControllerApiGitProviderDelete())
                .handler(new HandlerLogger.HandlerLoggerResponse());
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
                .handler(new ControllerApiGitProviderInstall())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .get("/api/v1/git/matcher/note")
                .handler(new ControllerApiMatcherNoteGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/git/matcher/note")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiMatcherNoteCreate.MatcherNoteValidation.validator(vertx))
                .handler(new ControllerApiMatcherNoteCreate.MatcherNoteValidationExistsGitRepo())
                .handler(new ControllerApiMatcherNoteCreate())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .put("/api/v1/git/matcher/note/:ID")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(ControllerApiMatcherNoteEdit.MatcherNoteValidation.validator(vertx))
                .handler(new MatcherNoteValidationExists())
                .handler(new ControllerApiMatcherNoteEdit())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/git/matcher/note/:ID")
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(new MatcherNoteValidationExists())
                .handler(new ControllerApiMatcherNoteDelete())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .get("/api/v1/git/filter/search")
                .handler(new ControllerApiGitFilterGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/git/filter/search")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitFilterCreate.GitFilterCreateValidation.validator(vertx))
                .handler(new ControllerApiGitFilterCreate())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/git/filter/search/:ID")
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(new ControllerApiGitFilterDelete.GitFilterCheckIsExists())
                .handler(new ControllerApiGitFilterDelete())
                .handler(new HandlerLogger.HandlerLoggerResponse());
    }

    /**
     * Install handlers errors for the router.
     *
     * @param router Primary request processor router.
     */
    private void installingHandlerErrorsAPI(final @NotNull Router router) {
        router.errorHandler(400, ctx -> {
            HandlerLogger.logger.info("response [{}] [{}] {}",
                    HandlerLogger.getMsgRequest(ctx.request().method().toString()),
                    HandlerLogger.getMsgResponse(HttpResponseStatus.BAD_REQUEST.code()),
                    ctx.request().path());

            if (ctx.failure() instanceof BadRequestException) {
                Buffer errorMsg = Buffer.buffer("Bad request");

                if (ctx.failure() instanceof BodyProcessorException) {
                    errorMsg = ((BodyProcessorException) ctx.failure()).toJson().toBuffer();
                } else if (ctx.failure() instanceof ParameterProcessorException) {
                    errorMsg = ((ParameterProcessorException) ctx.failure()).toJson().toBuffer();
                }

                ctx
                        .response()
                        .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(errorMsg.length()))
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .write(errorMsg);

                ctx
                        .response()
                        .end();
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
            HandlerLogger.logger.info("response [{}] [{}] {}",
                    HandlerLogger.getMsgRequest(ctx.request().method().toString()),
                    HandlerLogger.getMsgResponse(HttpResponseStatus.NOT_FOUND.code()),
                    ctx.request().path());

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
