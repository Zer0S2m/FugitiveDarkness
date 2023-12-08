package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.exception.NotFoundException;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoDelete;
import com.zer0s2m.fugitivedarkness.provider.FileSystemUtils;
import com.zer0s2m.fugitivedarkness.provider.HelperGitRepo;
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

import java.nio.file.Path;

import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

/**
 * Request handler to remove a git repository from the system.
 */
final public class ControllerApiGitRepoDelete implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoDelete.class);

    /**
     * Delete a repository from the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        ContainerGitRepoDelete containerGitRepoDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitRepoDelete.class);
        boolean isExistsGitRepository = HelperGitRepo.existsGitRepository(
                containerGitRepoDelete.group(), containerGitRepoDelete.project());

        if (!isExistsGitRepository) {
            event.fail(
                    HttpResponseStatus.NOT_FOUND.code(),
                    new NotFoundException("Object not found in the system"));
        } else {
            event.vertx()
                    .executeBlocking(() -> {
                        final Path sourceGitRepository = HelperGitRepo.getSourceGitRepository(
                                containerGitRepoDelete.group(), containerGitRepoDelete.project());
                        logger.info("Start deleting a git repository [" + sourceGitRepository + "]");
                        FileSystemUtils.deleteDirectory(sourceGitRepository);
                        logger.info("Finish deleting git repository [" + sourceGitRepository + "]");
                        return null;
                    });
            event.response()
                    .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                    .end();
        }
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitRepoDeleteValidation {

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
                            .requiredProperty("project", stringSchema())))
                    .build();
        }

    }

}
