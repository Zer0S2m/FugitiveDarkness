package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerProjectGet;
import com.zer0s2m.fugitivedarkness.models.GitRepoModel;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectManager;
import com.zer0s2m.fugitivedarkness.provider.project.ProjectReader;
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

import java.util.Collection;

import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * The request handler for getting the file structure of the project.
 */
public class ControllerApiProjectGet implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiProjectGet.class);

    /**
     * Something has happened, so handle it.
     *
     * @param event the event to handle
     */
    @Override
    public void handle(RoutingContext event) {
        logger.info("The beginning of receiving the project");

        final ContainerProjectGet containerProjectGet = event
                .body()
                .asJsonObject()
                .mapTo(ContainerProjectGet.class);

        final GitRepoRepository gitRepoRepository = new GitRepoRepositoryImpl(event.vertx());

        gitRepoRepository
                .findById(containerProjectGet.gitRepositoryId())
                .onSuccess(ar -> {
                    gitRepoRepository.closeClient();

                    final GitRepoModel gitRepo = gitRepoRepository.mapTo(ar).get(0);

                    final ProjectManager projectManager;
                    final ProjectReader projectReader;
                    if (gitRepo.getGroup().equals("LOCAL")) {
                        projectManager = ProjectManager.createLocal();
                        projectReader = ProjectReader.createLocal(gitRepo.getSource());
                    } else {
                        projectManager = ProjectManager.createGit();
                        projectReader = ProjectReader.createGit(
                                gitRepo.getGroup(), gitRepo.getProject());
                    }

                    Collection<String> titleFiles = projectManager.getAllFilesProject(projectReader);

                    JsonObject object = new JsonObject();
                    object.put("success", true);
                    object.put("files", titleFiles);

                    event.response()
                            .putHeader(
                                    HttpHeaders.CONTENT_LENGTH,
                                    String.valueOf(object.toString().length()))
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .write(object.toString());

                    logger.info("The end of receiving the project");

                    event.next();
                })
                .onFailure(error -> {
                    gitRepoRepository.closeClient();

                    logger.error("Failure (DB): " + error.getCause());

                    event.response()
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end();
                });
    }

    /**
     * TODO: Move to {@link SchemaRepository}.
     */
    public static class GetValidation {

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
                            .requiredProperty("gitRepositoryId", intSchema())
                            .requiredProperty("isTreeStructure", booleanSchema())))
                    .build();
        }

    }

}
