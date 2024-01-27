package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.api.scheme.SchemaFilterSearch;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitFilterCreate;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoSearch;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerResponseGitFilter;
import com.zer0s2m.fugitivedarkness.models.GitFilterModel;
import com.zer0s2m.fugitivedarkness.repository.GitFilterRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitFilterRepositoryImpl;
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
 * Request handler to save the search filter.
 */
final public class ControllerApiGitFilterCreate implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitFilterCreate.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        logger.info("Start saving a search filter");

        final GitFilterRepository filterRepository = new GitFilterRepositoryImpl(event.vertx());
        final ContainerGitFilterCreate containerGitFilterCreate = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitFilterCreate.class);
        final GitFilterModel newModel = new GitFilterModel(
                containerGitFilterCreate.title(),
                JsonObject.mapFrom(containerGitFilterCreate.filter()).toString());

        filterRepository
                .save(newModel)
                .onSuccess(ar -> {
                    final GitFilterModel createdFilter = filterRepository.mapTo(ar).get(0);
                    final ContainerResponseGitFilter responseGitFilter = new ContainerResponseGitFilter(
                            createdFilter.getId(),
                            createdFilter.getCreatedAt(),
                            createdFilter.getTitle(),
                            new JsonObject(createdFilter.getFilter())
                                    .mapTo(ContainerGitRepoSearch.class));
                    final JsonObject object = new JsonObject();

                    object.put("success", true);
                    object.put("filter", responseGitFilter);

                    event
                            .response()
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.CREATED.code())
                            .write(object.toString());

                    event.next();
                })
                .onFailure(error -> {
                    filterRepository.closeClient();

                    logger.error("Failure (DB): " + error.fillInStackTrace());

                    event
                            .response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });

        logger.info("Finish saving a search filter");
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitFilterCreateValidation {

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
                            .requiredProperty("title", stringSchema())
                            .requiredProperty("filter", SchemaFilterSearch.SCHEMA_FILTER_SEARCH)))
                    .build();
        }

    }

}
