package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectFileTodo;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.git.ContainerInfoSearchFileGitRepo;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectManager;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectTodoFilters;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * The request handler for getting the number of todos in the project.
 */
public final class ControllerApiProjectFilesTodo implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectFilesTodo.class);

    private final ProjectManager projectManager = ProjectManager.create();

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Getting started getting TODO in the project");

        final ContainerProjectFileTodo containerProjectFileTodo = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectFileTodo.class);

        final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());

        gitRepoRepository
                .findById((int) containerProjectFileTodo.gitRepositoryId())
                .onSuccess(ar -> {
                    gitRepoRepository.closeClient();
                    final GitRepoModel gitRepo = gitRepoRepository.mapTo(ar).get(0);

                    ProjectTodoFilters projectTodoFilters = ProjectTodoFilters
                            .create(
                                    containerProjectFileTodo.filters().file(),
                                    containerProjectFileTodo.filters().isFile(),
                                    containerProjectFileTodo.filters().isDirectory(),
                                    gitRepo.getIsUnpacking(),
                                    gitRepo.getIsLocal(),
                                    new ContainerGitRepoMeta(
                                            gitRepo.getGroup(),
                                            gitRepo.getProject(),
                                            gitRepo.getHost()));

                    List<ContainerInfoSearchFileGitRepo> todos = projectManager.findTodo(
                            Path.of(gitRepo.getSource()), projectTodoFilters);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("todos", todos);

                    event
                            .response()
                            .setChunked(true)
                            .putHeader(
                                    HttpHeaders.CONTENT_LENGTH,
                                    String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("End of getting TODO in the project");

                    event.next();
                })
                .onFailure(error -> {
                    gitRepoRepository.closeClient();

                    logger.error("Failure (DB): " + error.getCause());

                    event.response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class FilesTodoValidation {

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
                            .requiredProperty("filters", objectSchema()
                                    .requiredProperty("file", stringSchema())
                                    .requiredProperty("isFile", booleanSchema())
                                    .requiredProperty("isDirectory", booleanSchema()))))
                    .build();
        }

    }

}
