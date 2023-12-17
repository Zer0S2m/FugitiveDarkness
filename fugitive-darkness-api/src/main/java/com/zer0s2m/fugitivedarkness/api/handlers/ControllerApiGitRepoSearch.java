package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoControl;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoSearch;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoSearchFileGitRepo;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoSearchGitRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoFilterSearch;
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
 * Processing a request to find matches in a git repository.
 */
final public class ControllerApiGitRepoSearch implements Handler<RoutingContext> {

    private final GitRepo serviceGit = GitRepo.create();

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoSearch.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        ContainerGitRepoSearch gitRepoSearch = event
                .body()
                .asJsonObject()
                .mapTo(ContainerGitRepoSearch.class);

        ContainerGitRepoControl gitRepoControl = gitRepoSearch.filters().git().get(0);
        final Path gitRepoSource = Path.of(
                Environment.ROOT_PATH_REPO,
                gitRepoControl.group(),
                gitRepoControl.project(),
                ".git");

        JsonObject object = new JsonObject();
        object.put("success", true);

        event.vertx()
                .executeBlocking(() -> {
                    logger.info("Start a search");
                    final List<ContainerInfoSearchFileGitRepo> result = serviceGit.searchByGrep(GitRepoFilterSearch
                            .create()
                            .setPattern(gitRepoSearch.pattern())
                            .addGitRepo(gitRepoSource));
                    logger.info("Search ends");
                    return new ContainerInfoSearchGitRepo(
                            gitRepoControl.group(),
                            gitRepoControl.project(),
                            gitRepoSearch.pattern(),
                            result);
                })
                .onSuccess(result -> {
                    object.put("searchResult", result);

                    event.response()
                            .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());
                    event.response().end();
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GitRepoSearchValidation {

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
                            .requiredProperty("pattern", stringSchema())
                            .requiredProperty("filters", objectSchema()
                                    .requiredProperty("git", arraySchema()
                                            .items(objectSchema()
                                                    .requiredProperty("group", stringSchema())
                                                    .requiredProperty("project", stringSchema()))
                                    ))
                            )
                    )
                    .build();
        }

    }

}
