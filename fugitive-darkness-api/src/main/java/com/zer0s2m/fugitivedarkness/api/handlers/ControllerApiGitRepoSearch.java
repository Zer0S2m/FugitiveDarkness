package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.Environment;
import com.zer0s2m.fugitivedarkness.common.dto.ContainerGitRepoSearch;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.provider.ContainerGitRepoMeta;
import com.zer0s2m.fugitivedarkness.provider.ContainerInfoSearchGitRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepo;
import com.zer0s2m.fugitivedarkness.provider.GitRepoFilterSearch;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

        final GitRepoFilterSearch gitRepoFilterSearch = GitRepoFilterSearch
                .create()
                .setPattern(gitRepoSearch.pattern());

        if (gitRepoSearch.filters().includeExtensionFiles() != null &&
                !gitRepoSearch.filters().includeExtensionFiles().isEmpty()) {
            gitRepoFilterSearch.setIncludeExtensionFile(gitRepoSearch.filters().includeExtensionFiles());
        }
        if (gitRepoSearch.filters().excludeExtensionFiles() != null &&
                !gitRepoSearch.filters().excludeExtensionFiles().isEmpty()) {
            gitRepoFilterSearch.setExcludeExtensionFile(gitRepoSearch.filters().excludeExtensionFiles());
        }
        if (gitRepoSearch.filters().patternForIncludeFile() != null &&
                !gitRepoSearch.filters().patternForIncludeFile().isEmpty()) {
            gitRepoFilterSearch.setPatternForIncludeFile(
                    Pattern.compile(gitRepoSearch.filters().patternForIncludeFile()));
        }
        if (gitRepoSearch.filters().patternForExcludeFile() != null &&
                !gitRepoSearch.filters().patternForExcludeFile().isEmpty()) {
            gitRepoFilterSearch.setPatternForExcludeFile(
                    Pattern.compile(gitRepoSearch.filters().patternForExcludeFile()));
        }

        gitRepoFilterSearch.setMaxCount(gitRepoSearch.filters().maxCount());
        gitRepoFilterSearch.setMaxDepth(gitRepoSearch.filters().maxDepth());
        if (gitRepoSearch.filters().context() == -1 || gitRepoSearch.filters().context() == 0) {
            gitRepoFilterSearch.setContextBefore(gitRepoSearch.filters().contextBefore());
            gitRepoFilterSearch.setContextAfter(gitRepoSearch.filters().contextAfter());
        } else {
            gitRepoFilterSearch.setContext(gitRepoSearch.filters().context());
        }

        JsonObject object = new JsonObject();
        object.put("success", true);

        final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());

        gitRepoRepository
                .findAll()
                .onComplete(ar -> event.vertx()
                        .executeBlocking(() -> {
                            logger.info("Start a search");

                            final List<GitRepoModel> gitRepositories = gitRepoRepository
                                    .mapTo(ar.result());
                            final Map<String, String> hostGitRepoByGroupAndProject = new HashMap<>();
                            gitRepositories.forEach(gitRepository -> hostGitRepoByGroupAndProject.put(
                                    gitRepository.getGroup() + "__" + gitRepository.getProject(),
                                    gitRepository.getHost()
                            ));

                            gitRepoSearch.filters().git()
                                    .forEach(repo -> {
                                        final Path source = Path.of(
                                                Environment.ROOT_PATH_REPO,
                                                repo.group(),
                                                repo.project(),
                                                ".git");

                                        gitRepoFilterSearch
                                                .addGitRepo(source)
                                                .addGitMeta(source, new ContainerGitRepoMeta(
                                                        repo.group(),
                                                        repo.project(),
                                                        hostGitRepoByGroupAndProject.get(
                                                                repo.group() + "__" + repo.project())
                                                ));
                                    });

                            final List<ContainerInfoSearchGitRepo> resultSearch = serviceGit
                                    .searchByGrep(gitRepoFilterSearch);

                            logger.info("Search ends");

                            return resultSearch;
                        }, false)
                        .onSuccess(result -> {
                            object.put("searchResult", result);

                            event.response()
                                    .setChunked(true)
                                    .putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(object.toString().length()))
                                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                                    .setStatusCode(HttpResponseStatus.OK.code())
                                    .write(object.toString());
                            event.response().end();
                        }));
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
                                            )
                                            .optionalProperty("includeExtensionFiles", arraySchema()
                                                    .items(stringSchema())
                                                    .nullable())
                                            .optionalProperty("excludeExtensionFiles", arraySchema()
                                                    .items(stringSchema())
                                                    .nullable())
                                            .optionalProperty("patternForIncludeFile", stringSchema())
                                            .optionalProperty("patternForExcludeFile", stringSchema())
                                            .optionalProperty("maxCount", intSchema())
                                            .optionalProperty("maxDepth", intSchema())
                                            .optionalProperty("context", intSchema())
                                            .optionalProperty("contextBefore", intSchema())
                                            .optionalProperty("contextAfter", intSchema()))
                            )
                    )
                    .build();
        }

    }

}
