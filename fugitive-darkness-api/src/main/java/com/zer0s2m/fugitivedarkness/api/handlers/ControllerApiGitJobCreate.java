package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitJobCreate;
import com.zer0s2m.fugitivedarkness.models.GitJobModel;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.plugin.job.GitTypeJob;
import com.zer0s2m.fugitivedarkness.repository.GitJobRepository;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitJobRepositoryImpl;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.json.schema.common.dsl.Schemas.*;

final public class ControllerApiGitJobCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitJobCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        final GitJobRepository gitJobRepository = new GitJobRepositoryImpl(event.vertx());
        final ContainerGitJobCreate containerGitJobCreate = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitJobCreate.class);

        gitJobRepository
                .save(new GitJobModel(
                        containerGitJobCreate.cron(),
                        containerGitJobCreate.type(),
                        containerGitJobCreate.gitRepositoryId()))
                .onSuccess(ar -> {
                    final JsonObject jsonObject = new JsonObject();
                    final GitJobModel createdFilter = gitJobRepository.mapTo(ar).get(0);
                    gitJobRepository.closeClient();

                    jsonObject.put("success", true);
                    jsonObject.put("gitJob", createdFilter);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(jsonObject.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.CREATED.code())
                            .write(jsonObject.toString());

                    event.next();
                })
                .onFailure(error -> {
                    gitJobRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitJobCreateValidation {

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
                            .requiredProperty("gitRepositoryId", intSchema())
                            .requiredProperty("type", enumSchema(
                                    GitTypeJob.ONETIME_USE.toString(),
                                    GitTypeJob.PERMANENT.toString()))
                            .requiredProperty("cron", stringSchema())))
                    .build();
        }

    }

    /**
     * Validation of the git repository for its locality in the system.
     */
    public static class GitRepoValidationIsLocal implements Handler<RoutingContext> {

        /**
         * Something has happened, so handle it.
         *
         * @param event the event to handle
         */
        @Override
        public void handle(RoutingContext event) {
            final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
            final JsonObject requestBody = event
                    .body()
                    .asJsonObject();

            final long gitRepositoryId = requestBody.getLong("gitRepositoryId");

            repositoryGit
                    .findById((int) gitRepositoryId)
                    .onSuccess((ar) -> {
                        repositoryGit.closeClient();
                        final GitRepoModel gitRepo = repositoryGit.mapTo(ar).get(0);

                        if (gitRepo.getIsLocal()) {
                            event.fail(
                                    HttpResponseStatus.BAD_REQUEST.code(),
                                    BodyProcessorException.createValidationError(
                                            event.request().getHeader(HttpHeaders.CONTENT_TYPE),
                                            new RuntimeException("You cannot schedule deferred tasks " +
                                                    "to a local repository")));
                        } else {
                            event.next();
                        }
                    });
        }

    }

}
