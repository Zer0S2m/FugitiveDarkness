package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitMatcherNoteCreate;
import com.zer0s2m.fugitivedarkness.models.MatcherNoteModel;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.MatcherNoteRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import com.zer0s2m.fugitivedarkness.repository.impl.MatcherNoteRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
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
 * Request handler for creating notes on matches.
 */
final public class ControllerApiMatcherNoteCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiMatcherNoteCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final MatcherNoteRepository matcherNoteRepository = new MatcherNoteRepositoryImpl(event.vertx());
        final ContainerGitMatcherNoteCreate containerGitMatcherNoteCreate = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitMatcherNoteCreate.class);

        matcherNoteRepository
                .save(new MatcherNoteModel(
                        containerGitMatcherNoteCreate.value(),
                        containerGitMatcherNoteCreate.file(),
                        containerGitMatcherNoteCreate.line(),
                        containerGitMatcherNoteCreate.lineNumber(),
                        containerGitMatcherNoteCreate.gitRepositoryId()
                ))
                .onSuccess(ar -> {
                    matcherNoteRepository.closeClient();

                    final MatcherNoteModel matcherNoteModel = matcherNoteRepository.mapTo(ar).get(0);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("matcherNote", matcherNoteModel);

                    event.response()
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.CREATED.code())
                            .write(object.toString());

                    event.next();
                })
                .onFailure(error -> {
                    matcherNoteRepository.closeClient();

                    logger.error("Failure (DB): ", error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.CREATED.code())
                            .end();
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class MatcherNoteValidation {

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
                            .requiredProperty("value", stringSchema())
                            .requiredProperty("file", stringSchema())
                            .requiredProperty("line", stringSchema())
                            .requiredProperty("lineNumber", intSchema())
                            .requiredProperty("gitRepositoryId", intSchema())))
                    .build();
        }

    }

    /**
     * Validation handler for checking the repository in the system.
     */
    public static class MatcherNoteValidationExistsGitRepo implements Handler<RoutingContext> {

        /**
         * Something has happened, so handle it.
         *
         * @param event the event to handle
         */
        @Override
        public void handle (@NotNull RoutingContext event) {
            final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
            final ContainerGitMatcherNoteCreate containerGitMatcherNoteCreate = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerGitMatcherNoteCreate.class);

            repositoryGit
                    .existsById(containerGitMatcherNoteCreate.gitRepositoryId())
                    .onSuccess((ar) -> {
                        repositoryGit.closeClient();

                        if (!repositoryGit.mapToExistsColumn(ar)) {
                            event.fail(
                                    HttpResponseStatus.NOT_FOUND.code(),
                                    new NotFoundException("The repository not found in the system"));
                        } else {
                            event.next();
                        }
                    });
        }

    }

}
