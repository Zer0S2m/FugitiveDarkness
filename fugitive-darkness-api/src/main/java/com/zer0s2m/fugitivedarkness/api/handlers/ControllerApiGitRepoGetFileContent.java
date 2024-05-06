package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoGetFile;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.provider.git.GitReaderContentFileAdapter;
import com.zer0s2m.fugitivedarkness.provider.git.GitRepoManager;
import com.zer0s2m.fugitivedarkness.provider.git.impl.GitReaderContentFileAdapterGit;
import com.zer0s2m.fugitivedarkness.provider.git.impl.GitReaderContentFileAdapterLocal;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * Request handler for getting a file from a git repository.
 */
final public class ControllerApiGitRepoGetFileContent implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoGetFileContent.class);

    private final GitRepoManager gitRepo = GitRepoManager.create();

    /**
     * Getting a file from a git repository.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        ContainerGitRepoGetFile containerGitRepoDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitRepoGetFile.class);

        final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());

        gitRepoRepository
                .findByGroupAndProject(containerGitRepoDelete.group(), containerGitRepoDelete.project())
                .onSuccess(ar -> {
                    final List<GitRepoModel> gitRepositories = gitRepoRepository.mapTo(ar);

                    if (gitRepositories.isEmpty()) {
                        event.fail(
                                HttpResponseStatus.NOT_FOUND.code(),
                                new NotFoundException("Git repository not found in the system"));
                    } else {
                        GitRepoModel gitRepoModel = gitRepositories.get(0);

                        logger.info(String.format(
                                "Start receiving file [%s:%s:%s]",
                                gitRepoModel.getGroup(),
                                gitRepoModel.getProject(),
                                containerGitRepoDelete.file()
                        ));

                        final GitReaderContentFileAdapter gitReaderContentFileAdapter;
                        final Map<String, Object> propertiesForAdapter = new HashMap<>();
                        propertiesForAdapter.put("file", containerGitRepoDelete.file());
                        if (gitRepoModel.getIsLocal()) {
                            gitReaderContentFileAdapter = new GitReaderContentFileAdapterLocal();

                            propertiesForAdapter.put("source", gitRepoModel.getSource());
                        } else {
                            gitReaderContentFileAdapter = new GitReaderContentFileAdapterGit();

                            propertiesForAdapter.put("group", gitRepoModel.getGroup());
                            propertiesForAdapter.put("project", gitRepoModel.getProject());
                        }

                        event
                                .vertx()
                                .executeBlocking(() -> gitRepo.gShowFile(
                                        gitReaderContentFileAdapter,
                                        propertiesForAdapter))
                                .onSuccess((ar2) -> {
                                    final JsonObject result = new JsonObject();
                                    result.put("success", true);
                                    result.put("content", ar2);

                                    event.response()
                                            .setChunked(true)
                                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(result.toString().length()))
                                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                            .setStatusCode(HttpResponseStatus.OK.code())
                                            .write(result.toString());

                                    logger.info(String.format(
                                            "Finish receiving file [%s:%s:%s]",
                                            gitRepoModel.getGroup(),
                                            gitRepoModel.getProject(),
                                            containerGitRepoDelete.file()
                                    ));

                                    event.next();
                                })
                                .onFailure((cause) -> {
                                    logger.error("Failure (GIT): " + cause.getCause());

                                    event
                                            .response()
                                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                            .end();
                                });
                    }
                })
                .onFailure(error -> {
                    gitRepoRepository.closeClient();

                    logger.error("Failure (SYSTEM): " + error.getMessage());

                    event.response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitRepoGetFileValidation {

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
                            .requiredProperty("group", stringSchema())
                            .requiredProperty("project", stringSchema())
                            .requiredProperty("file", stringSchema())))
                    .build();
        }

    }

}
