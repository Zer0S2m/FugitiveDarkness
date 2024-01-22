package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.ObjectISExistsInSystemException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoInstall;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
import com.zer0s2m.fugitivedarkness.repository.GitRepoRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitRepoRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.*;
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
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

/**
 * Request handler for installing a git repository on the system.
 */
final public class ControllerApiGitRepoInstall implements Handler<RoutingContext> {

    private static final GitRepo serviceGit = GitRepo.create();

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoInstall.class);

    /**
     * Install the repository on the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
        final ContainerGitRepoInstall containerGitRepoInstall = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitRepoInstall.class);
        final ContainerInfoRepo infoRepo = serviceGit.gGetInfo(containerGitRepoInstall.remote());

        JsonObject object = new JsonObject();
        object.put("success", true);
        object.put("isLoadGitRepository", false);
        object.put("gitRepository", infoRepo);

        repositoryGit.save(new GitRepoModel(
                        infoRepo.group(),
                        infoRepo.project(),
                        infoRepo.host(),
                        infoRepo.source(),
                        false
                ))
                .onComplete(resultSaved -> {
                    if (!resultSaved.succeeded()) {
                        logger.error("Failure: " + resultSaved.cause());
                        return;
                    }

                    WorkerExecutor executor = event
                            .vertx()
                            .createSharedWorkerExecutor(
                                    "worker.git.operation-installing",
                                    4,
                                    5,
                                    TimeUnit.MINUTES);
                    executor
                            .executeBlocking(() -> {
                                try {
                                    return serviceGit.gClone(containerGitRepoInstall.remote());
                                } catch (GitAPIException e) {
                                    logger.error("Failure (GIT): " + e.fillInStackTrace());
                                    throw new RuntimeException(e);
                                }
                            }, false)
                            .onFailure((fail) -> repositoryGit
                                    .deleteByGroupAndProject(infoRepo.group(), infoRepo.project())
                                    .onSuccess(ar -> repositoryGit.closeClient())
                                    .onFailure(fail2 -> {
                                        logger.error("Failure (DB 1): " + fail2.getMessage());
                                        repositoryGit.closeClient();
                                    }))
                            .onSuccess(result -> repositoryGit
                                    .updateIsLoadByGroupAndProject(result.group(), result.project(), true)
                                    .onComplete(ar -> {
                                        if (!ar.succeeded()) {
                                            logger.error("Failure (DB 2): " + ar.cause());
                                        }

                                        repositoryGit.closeClient();
                                    }));
                });

        event.response()
                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(HttpResponseStatus.CREATED.code())
                .write(object.toString());

        event
                .response()
                .end();
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitRepoInstallValidation {

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
                            .requiredProperty("remote", stringSchema())
                            .requiredProperty("group", stringSchema())))
                    .build();
        }

    }

    /**
     * Handler to check if a git repository exists.
     */
    public static class GitRepoInstallCheckIsExists implements Handler<RoutingContext> {

        /**
         * Handler to check if a git repository exists.
         *
         * @param event The event to handle.
         */
        @Override
        public void handle(@NotNull RoutingContext event) {
            final GitRepoRepository repositoryGit = new GitRepoRepositoryImpl(event.vertx());
            final ContainerGitRepoInstall containerGitRepoInstall = event
                    .body()
                    .asJsonObject()
                    .mapTo(ContainerGitRepoInstall.class);
            final ContainerInfoRepo infoRepo = serviceGit.gGetInfo(containerGitRepoInstall.remote());

            repositoryGit
                    .existsByGroupAndProject(infoRepo.group(), infoRepo.project())
                    .onSuccess((ar) -> {
                        if (repositoryGit.mapToExistsColumn(ar)) {
                            event.fail(
                                    HttpResponseStatus.BAD_REQUEST.code(),
                                    new ObjectISExistsInSystemException("The repository already exists in the system"));
                        } else {
                            event.next();
                        }
                    });
        }

    }

}
