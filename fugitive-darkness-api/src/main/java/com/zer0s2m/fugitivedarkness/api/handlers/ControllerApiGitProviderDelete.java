package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderDelete;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
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
 * Request handler for removing git provider.
 */
public class ControllerApiGitProviderDelete implements Handler<RoutingContext> {

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
        final ContainerInfoGitProviderDelete containerInfoGitProviderDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderDelete.class);

        gitProviderRepository
                .deleteByTypeAndTarget(containerInfoGitProviderDelete.type(), containerInfoGitProviderDelete.target());

        event
                .response()
                .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                .end();

        logger.info("Finish removing git provider");
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitProviderDeleteValidation {

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
                            .requiredProperty("target", stringSchema())))
                    .build();
        }

    }

    /**
     * Validation before deleting a git provider - checks for the existence of an entry in the database.
     */
    public static class GitProviderDeleteValidationIsExistsInSystem implements Handler<RoutingContext> {

        /**
         * Validation for the existence of a record in the database.
         *
         * @param event The event to handle.
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
            final ContainerInfoGitProviderDelete containerInfoGitProviderDelete = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerInfoGitProviderDelete.class);

            gitProviderRepository
                    .existsByTypeAndTarget(
                            containerInfoGitProviderDelete.type(),
                            containerInfoGitProviderDelete.target())
                    .onSuccess(ar -> {
                        if (!gitProviderRepository.mapToExistsColumn(ar)) {
                            event.fail(
                                    HttpResponseStatus.NOT_FOUND.code(),
                                    new NotFoundException("Git provider does not exist"));
                        } else {
                            event.next();
                        }
                    });
        }

    }

}
