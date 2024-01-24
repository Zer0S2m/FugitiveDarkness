package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoGetFile;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
import com.zer0s2m.fugitivedarkness.provider.HelperGitRepo;
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
 * Request handler for getting a file from a git repository.
 */
final public class ControllerApiGitRepoGetFileContent implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoGetFileContent.class);

    private final GitRepo gitRepo = GitRepo.create();

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
        boolean isExistsGitRepository = HelperGitRepo.existsGitRepository(
                containerGitRepoDelete.group(), containerGitRepoDelete.project());

        if (!isExistsGitRepository) {
            event.fail(
                    HttpResponseStatus.NOT_FOUND.code(),
                    new NotFoundException("Object not found in the system"));
        } else {
            logger.info(String.format(
                    "Start receiving file [%s:%s:%s]",
                    containerGitRepoDelete.group(),
                    containerGitRepoDelete.project(),
                    containerGitRepoDelete.file()
            ));

            event
                    .vertx()
                    .executeBlocking(() -> gitRepo.gShowFile(
                            containerGitRepoDelete.group(),
                            containerGitRepoDelete.project(),
                            containerGitRepoDelete.file()))
                    .onSuccess((ar) -> {
                        final JsonObject result = new JsonObject();
                        result.put("success", true);
                        result.put("content", ar);

                        event.response()
                                .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(result.toString().length()))
                                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                .setStatusCode(HttpResponseStatus.OK.code())
                                .write(result.toString());

                        event
                                .response()
                                .end();

                        logger.info("Finish receiving file");
                    })
                    .onFailure((cause) -> {
                        logger.error("Failure (GIT): " + cause.getCause());

                        event
                                .response()
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end();
                    });
        }

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
