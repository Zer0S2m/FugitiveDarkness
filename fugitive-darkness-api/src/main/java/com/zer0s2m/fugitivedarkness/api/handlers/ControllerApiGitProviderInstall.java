package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.api.exception.ObjectISExistsInSystemException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderInstall;
import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderWebClient;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProvider;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderInfo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRepository;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * Request handler for setting provider for git repositories.
 */
public final class ControllerApiGitProviderInstall implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitProviderInstall.class);

    /**
     * Installing a provider for git repositories.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start installing the provider for git repositories");

        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final ContainerInfoGitProviderInstall containerInfoGitProviderInstall = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderInstall.class);

        JsonObject object = new JsonObject();
        object.put("success", true);
        object.put("gitProvider", containerInfoGitProviderInstall);

        gitProviderRepository
                .save(new GitProviderModel(
                        containerInfoGitProviderInstall.type(),
                        containerInfoGitProviderInstall.isOrg(),
                        containerInfoGitProviderInstall.isUser(),
                        containerInfoGitProviderInstall.target()))
                .onComplete((resultSaved) -> {
                    if (!resultSaved.succeeded()) {
                        logger.error("Failure (DB): " + resultSaved.cause());
                    }

                    gitProviderRepository.closeClient();

                    logger.info("End of provider installation for git repositories");
                });

        event.response()
                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(HttpResponseStatus.CREATED.code())
                .write(object.toString());

        event.next();
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitProviderInstallValidation {

        /**
         * Get validation handler for incoming body.
         *
         * @param vertx App.
         * @return Incoming body handler.
         */
        public static ValidationHandler validator(Vertx vertx) {
            return ValidationHandlerBuilder
                    .create(SchemaParser.createDraft7SchemaParser(
                            SchemaRouter.create(vertx, new SchemaRouterOptions())))
                    .body(Bodies.json(objectSchema()
                            .requiredProperty("type", enumSchema(
                                    GitRepoProviderType.GITHUB.toString(),
                                    GitRepoProviderType.GITLAB.toString()))
                            .requiredProperty("isOrg", booleanSchema())
                            .requiredProperty("isUser", booleanSchema())
                            .requiredProperty("target", stringSchema())))
                    .build();
        }

    }

    /**
     * Processing a request to check the existence of a git provider in the system.
     */
    public static class GitProviderInstallValidationIsExistsInSystem implements Handler<RoutingContext> {

        /**
         * Checking the existence of a git provider in the system.
         *
         * @param event the event to handle
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
            final ContainerInfoGitProviderInstall containerInfoGitProviderInstall = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerInfoGitProviderInstall.class);

            gitProviderRepository
                    .existsByTypeAndTarget(
                            containerInfoGitProviderInstall.type(),
                            containerInfoGitProviderInstall.target())
                    .onSuccess(ar -> {
                        if (gitProviderRepository.mapToExistsColumn(ar)) {
                            event.fail(
                                    HttpResponseStatus.BAD_REQUEST.code(),
                                    new ObjectISExistsInSystemException("Git provider already exists on the system"));
                        } else {
                            event.next();
                        }

                        gitProviderRepository.closeClient();
                    });
        }

    }

    /**
     * Request handler to validate data whether the target provider is a user or an organization.
     */
    public static class GitProviderInstallValidationIsOrgAndIsUser implements Handler<RoutingContext> {

        /**
         * Validation of data whether the target of the provider is a user or an organization.
         *
         * @param event the event to handle
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final ContainerInfoGitProviderInstall containerInfoGitProviderInstall = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerInfoGitProviderInstall.class);

            if (containerInfoGitProviderInstall.isOrg() && containerInfoGitProviderInstall.isUser()) {
                event.fail(
                        HttpResponseStatus.BAD_REQUEST.code(),
                        BodyProcessorException.createValidationError(
                                event.request().getHeader(HttpHeaders.CONTENT_TYPE),
                                new RuntimeException("You can only select an organization or a user")));
            } else if (!containerInfoGitProviderInstall.isOrg() && !containerInfoGitProviderInstall.isUser()) {
                event.fail(
                        HttpResponseStatus.BAD_REQUEST.code(),
                        BodyProcessorException.createValidationError(
                                event.request().getHeader(HttpHeaders.CONTENT_TYPE),
                                new RuntimeException("You must select an organization or user")));
            } else {
                event.next();
            }
        }

    }

    /**
     * Request handler to check the existence of the provider in the external system {@link GitRepoProviderType}.
     */
    public static class GitProviderInstallValidationIsExistsInExternalSystem implements Handler<RoutingContext> {

        /**
         * Checking the existence of the provider in the external system {@link GitRepoProviderType}.
         *
         * @param event the event to handle
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final ContainerInfoGitProviderInstall containerInfoGitProviderInstall = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerInfoGitProviderInstall.class);
            final GitRepoProvider gitRepoProvider = GitRepoProvider.create(
                    GitRepoProviderType.valueOf(containerInfoGitProviderInstall.type()));
            final GitRepoProviderWebClient gitRepoProviderWebClient = GitRepoProviderWebClient.create(event.vertx());

            GitRepoProviderInfo gitRepoProviderInfo;

            if (containerInfoGitProviderInstall.isOrg()) {
                gitRepoProviderInfo = gitRepoProvider
                        .getInfoGitRepositoriesForOrg(containerInfoGitProviderInstall.target());
            } else {
                gitRepoProviderInfo = gitRepoProvider
                        .getInfoGitRepositoriesForUser(containerInfoGitProviderInstall.target());
            }

            gitRepoProviderWebClient
                    .getGitRepositories(gitRepoProviderInfo)
                    .onSuccess(ar -> event.next())
                    .onFailure(ar -> {
                        if (ar.getMessage().contains("404")) {
                            String msgError = containerInfoGitProviderInstall.isOrg() ?
                                    "The organization was not found in the provider"
                                    : "The user was not found in the provider";
                            event.fail(
                                    HttpResponseStatus.NOT_FOUND.code(),
                                    new NotFoundException(msgError));
                        } else {
                            event.next();
                        }
                    });
        }

    }

}
