package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoInstall;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
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

import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

/**
 * Request handler for installing a git repository on the system.
 */
final public class ControllerApiGitRepoInstall implements Handler<RoutingContext> {

    private final GitRepo serviceGit = GitRepo.create();

    /**
     * Install the repository on the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        ContainerGitRepoInstall containerGitRepoInstall = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitRepoInstall.class);

        JsonObject object = new JsonObject();
        object.put("success", true);

        event.vertx()
                .executeBlocking(() -> {
                    try {
                        Thread.sleep(2000);
                        return serviceGit.gClone(containerGitRepoInstall.remote());
                    } catch (GitAPIException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onSuccess(System.out::println);

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

}
