package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderInstall;
import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import com.zer0s2m.fugitivedarkness.repository.Repository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
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
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
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

        final Repository<RowSet<Row>, GitProviderModel> gitProviderRepository =
                new GitProviderRepositoryImpl(event.vertx());
        final ContainerInfoGitProviderInstall containerInfoGitProviderInstall = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderInstall.class);

        JsonObject object = new JsonObject();
        object.put("success", true);
        object.put("provider", containerInfoGitProviderInstall);

        gitProviderRepository
                .save(new GitProviderModel(
                        containerInfoGitProviderInstall.type(),
                        containerInfoGitProviderInstall.isOrg(),
                        containerInfoGitProviderInstall.isUser(),
                        containerInfoGitProviderInstall.target()))
                .onComplete((resultSaved) -> {
                    if (!resultSaved.succeeded()) {
                        logger.error("Failure: " + resultSaved.cause());
                    }

                    logger.info("End of provider installation for git repositories");
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

}
