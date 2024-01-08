package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderConverter;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderWebClient;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProvider;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderInfo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoProviderType;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRepository;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.vertx.ext.web.validation.builder.Parameters.param;
import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * Request handler for retrieving git repositories from providers.
 */
public class ControllerApiGitRepoProvider implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoProvider.class);

    /**
     * Get git repositories from the provider.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("Start receiving repositories from the provider");

        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final GitRepoProviderWebClient gitRepoProviderWebClient = GitRepoProviderWebClient.create(event.vertx());

        RequestParameters parameters = event.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final GitRepoProviderType typeProvider = GitRepoProviderType.valueOf(
                parameters.queryParameter("provider").getString());
        final String targetProvider = parameters.queryParameter("target").getString();

        gitProviderRepository
                .findByTypeAndTarget(typeProvider.toString(), targetProvider)
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        final GitProviderModel gitProvider = gitProviderRepository.mapTo(ar.result()).get(0);
                        final GitRepoProvider gitRepoProvider = GitRepoProvider.create(typeProvider);
                        final GitRepoProviderConverter gitRepoProviderConverter = GitRepoProviderConverter.create();

                        GitRepoProviderInfo gitRepoProviderInfo;

                        if (gitProvider.getIsOrg()) {
                            gitRepoProviderInfo = gitRepoProvider
                                    .getInfoGitRepositoriesForOrg(gitProvider.getTarget());
                        } else {
                            gitRepoProviderInfo = gitRepoProvider
                                    .getInfoGitRepositoriesForUser(gitProvider.getTarget());
                        }

                        gitRepoProviderWebClient
                                .getGitRepositories(gitRepoProviderInfo)
                                .onSuccess(result -> {
                                    final List<ContainerInfoRepo> readyData = gitRepoProviderConverter
                                            .convert(typeProvider, result.body());

                                    JsonObject object = new JsonObject();
                                    object.put("success", true);
                                    object.put("gitRepositories", readyData);

                                    event
                                            .response()
                                            .putHeader(
                                                    HttpHeaders.CONTENT_LENGTH,
                                                    String.valueOf(object.toString().length()))
                                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                            .setStatusCode(HttpResponseStatus.OK.code())
                                            .write(object.toString());

                                    event
                                            .response()
                                            .end();
                                    logger.info("Finish receiving repositories from the supplier");
                                })
                                .onFailure(handler -> {
                                    logger.error("Failure (HTTP CLIENT): " + handler.fillInStackTrace());

                                    event
                                            .response()
                                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());

                                    event.response().end();
                                });
                    } else {
                        logger.error("Failure (DB): " + ar.cause());
                    }
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitRepoProviderValidation {

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
                    .queryParameter(param(
                            "provider",
                            stringSchema()))
                    .queryParameter(param(
                            "target",
                            stringSchema()))
                    .build();
        }

    }

}
