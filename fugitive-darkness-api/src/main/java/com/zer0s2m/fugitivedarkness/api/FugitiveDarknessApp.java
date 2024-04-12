package com.zer0s2m.fugitivedarkness.api;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.api.exception.ObjectISExistsInSystemException;
import com.zer0s2m.fugitivedarkness.api.handlers.*;
import com.zer0s2m.fugitivedarkness.api.handlers.logger.HandlerLogger;
import com.zer0s2m.fugitivedarkness.api.handlers.validation.*;
import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.plugin.vertx.job.Listener;
import com.zer0s2m.fugitivedarkness.plugin.vertx.job.impl.ListenerGitJobOnetimeUse;
import com.zer0s2m.fugitivedarkness.plugin.vertx.job.impl.ListenerGitJobPermanent;
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
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class FugitiveDarknessApp extends AbstractVerticle {

    static private final Logger logger = LoggerFactory.getLogger(FugitiveDarknessApp.class);

    private final Listener listenerGitJobOnetimeUse = new ListenerGitJobOnetimeUse();

    private final Listener listenerGitJobPermanent = new ListenerGitJobPermanent();

    /**
     * Start app.
     */
    @Override
    public void start(Promise<Void> startPromise) {
        if (!checkEnvParams()) {
            startPromise.fail("Environment variables are not set");
            throw new RuntimeException("Environment variables are not set");
        }

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        installingRouterAPI(router);
        installingHandlerErrorsAPI(router);
        installJobs();

        server
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        logger.info("Starting a server on a port: 8080");
                        logger.info("""
                                Setting routes:
                                \t\u001b[44mSTATIC\u001b[0m [/static/docx/*]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/git-search]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/operation/git-get-repo-provider]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/git-get-file]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/git-load-repo-remote]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/operation/docx-search/:ID]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/repo]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/repo/install]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/repo/delete]
                                \t\u001b[43mPUT\u001b[0m    [/api/v1/git/repo/fetch]
                                \t\u001b[43mPUT\u001b[0m    [/api/v1/git/repo/checkout]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/matcher/note]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/matcher/note]
                                \t\u001b[43mPUT\u001b[0m    [/api/v1/git/matcher/note/:ID]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/matcher/note/:ID]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/filter/search]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/filter/search]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/filter/search/:ID]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/provider]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/provider/delete]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/provider/install]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/git/job]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/git/job]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/git/job/:ID]
                                \t\u001b[43mPUT\u001b[0m    [/api/v1/git/job/:ID]
                                \t\u001b[44mGET\u001b[0m    [/api/v1/docx]
                                \t\u001b[41mDELETE\u001b[0m [/api/v1/docx/:ID]
                                \t\u001b[42mPOST\u001b[0m   [/api/v1/docx/upload]""");
                        logger.info("""
                                Setting ENV:
                                \t\u001B[45mFD_ROOT_PATH\u001b[0m      - %s
                                \t\u001B[45mFD_ROOT_PATH_DOCX\u001b[0m - %s
                                \t\u001B[45mFD_POSTGRES_PORT\u001b[0m  - %s
                                \t\u001B[45mFD_POSTGRES_HOST\u001b[0m  - %s
                                \t\u001B[45mFD_POSTGRES_USER\u001b[0m  - %s
                                \t\u001B[45mFD_POSTGRES_DB\u001b[0m    - %s
                                \t\u001B[45mFD_ALLOW_ORIGIN\u001b[0m   - %s""".formatted(
                                Environment.ROOT_PATH_REPO,
                                Environment.ROOT_PATH_DOCX,
                                Environment.FD_POSTGRES_PORT,
                                Environment.FD_POSTGRES_HOST,
                                Environment.FD_POSTGRES_USER,
                                Environment.FD_POSTGRES_DB,
                                Environment.FD_ALLOW_ORIGIN
                        ));
                    } else {
                        logger.error("Failed to bind");
                        startPromise.fail(http.cause());
                    }
                });
    }

    /**
     * Set listening for scheduled tasks.
     */
    private void installJobs() {
        vertx.setPeriodic(1000 * 60, id -> {
            listenerGitJobOnetimeUse.listen(vertx);
            listenerGitJobPermanent.listen(vertx);
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

        // STATIC
        router
                .route("/static/docx/*")
                .handler(StaticHandler.create(FileSystemAccess.ROOT, Environment.ROOT_PATH_DOCX));

        // Operations
        router
                .post("/api/v1/operation/git-search")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoSearch.GitRepoSearchValidation.validator(vertx))
                .handler(new ControllerApiGitRepoSearch())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .get("/api/v1/operation/git-get-repo-provider")
                .handler(ControllerApiGitRepoProvider.GitRepoProviderValidation.validator(vertx))
                .handler(new ControllerApiGitRepoProvider())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/operation/git-load-repo-remote")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitProviderControlValidation.validator(vertx))
                .handler(new GitProviderValidationExists())
                .handler(new ControllerApiGitRepoInstallRemote())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/operation/git-get-file")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitRepoGetFileContent.GitRepoGetFileValidation.validator(vertx))
                .handler(new ControllerApiGitRepoGetFileContent())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/operation/docx-search/:ID")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(new DocxFileValidationExists())
                .handler(ControllerApiDocxSearch.DocxSearchValidation.validator(vertx))
                .handler(new ControllerApiDocxSearch())
                .handler(new HandlerLogger.HandlerLoggerResponse());

        // GIT
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
                .put("/api/v1/git/repo/checkout")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitRepoControlValidation.validator(vertx))
                .handler(new ControllerApiGitRepoCheckout())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .get("/api/v1/git/provider")
                .handler(new ControllerApiGitProviderGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/git/provider/delete")
                .consumes("application/json")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.GitProviderControlValidation.validator(vertx))
                .handler(new GitProviderValidationExists())
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
                .handler(new GitRepoValidationExists())
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
        router
                .get("/api/v1/git/job")
                .handler(new ControllerApiGitJobGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/git/job")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiGitJobCreate.GitJobCreateValidation.validator(vertx))
                .handler(new CronValidation("cron"))
                .handler(new GitRepoValidationExists())
                .handler(new ControllerApiGitJobCreate())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/git/job/:ID")
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(new GitJobValidationExists())
                .handler(new ControllerApiGitJobDelete())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .put("/api/v1/git/job/:ID")
                .handler(BodyHandler
                        .create()
                        .setHandleFileUploads(false))
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(new GitJobValidationExists())
                .handler(new CronValidation("cron"))
                .handler(new ControllerApiGitJobEdit())
                .handler(new HandlerLogger.HandlerLoggerResponse());

        // DOCX
        router
                .get("/api/v1/docx")
                .handler(new ControllerApiDocxGet())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .delete("/api/v1/docx/:ID")
                .handler(ControllerApiValidation.ValidationControlID.validator(vertx))
                .handler(new DocxFileValidationExists())
                .handler(new ControllerApiDocxDelete())
                .handler(new HandlerLogger.HandlerLoggerResponse());
        router
                .post("/api/v1/docx/upload")
                .handler(BodyHandler
                        .create()
                        .setBodyLimit(-1)
                        .setHandleFileUploads(true))
                .handler(new ControllerApiDocxUpload())
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

    private boolean checkEnvParams() {
        final AtomicBoolean isValid = new AtomicBoolean(true);

        if (!System.getenv().containsKey("FD_ROOT_PATH_REPO")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_ROOT_PATH_REPO\u001b[0m");
        }
        if (!System.getenv().containsKey("FD_ROOT_PATH_DOCX")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_ROOT_PATH_DOCX\u001b[0m");
        }
        if (!System.getenv().containsKey("FD_POSTGRES_PORT")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_POSTGRES_PORT\u001b[0m");
        }
        if (!System.getenv().containsKey("FD_POSTGRES_HOST")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_POSTGRES_HOST\u001b[0m");
        }
        if (!System.getenv().containsKey("FD_POSTGRES_USER")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_POSTGRES_USER\u001b[0m");
        }
        if (!System.getenv().containsKey("FD_POSTGRES_DB")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_POSTGRES_DB\u001b[0m");
        }
        if (!System.getenv().containsKey("FD_POSTGRES_PASSWORD")) {
            isValid.set(false);
            logger.error(
                    "\u001b[41mThe environment variable is not set\u001b[0m - \u001B[45mFD_POSTGRES_PASSWORD\u001b[0m");
        }

        return isValid.get();
    }

}
