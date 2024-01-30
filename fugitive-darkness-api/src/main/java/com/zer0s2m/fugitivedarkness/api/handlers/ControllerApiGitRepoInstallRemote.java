package com.zer0s2m.fugitivedarkness.api.handlers;

import com.zer0s2m.fugitivedarkness.common.dto.ContainerInfoGitProviderControl;
import com.zer0s2m.fugitivedarkness.models.GitProviderModel;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderConverter;
import com.zer0s2m.fugitivedarkness.plugin.vertx.git.provider.GitRepoProviderWebClient;
import com.zer0s2m.fugitivedarkness.provider.*;
import com.zer0s2m.fugitivedarkness.repository.GitProviderRepository;
import com.zer0s2m.fugitivedarkness.repository.impl.GitProviderRepositoryImpl;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Request handler for cloning git repositories from a remote host.
 */
public class ControllerApiGitRepoInstallRemote implements Handler<RoutingContext> {

    static private final Logger logger = LoggerFactory.getLogger(ControllerApiGitRepoInstallRemote.class);

    /**
     * Install the repository on the system.
     *
     * @param event The event to handle.
     */
    @Override
    public void handle(@NotNull RoutingContext event) {
        final GitProviderRepository gitProviderRepository = new GitProviderRepositoryImpl(event.vertx());
        final GitRepoProviderWebClient gitRepoProviderWebClient = GitRepoProviderWebClient.create(event.vertx());
        final ContainerInfoGitProviderControl containerInfoGitProviderDelete = event
                .body()
                .asJsonObject()
                .mapTo(ContainerInfoGitProviderControl.class);
        final GitRepoProviderType typeProvider = GitRepoProviderType.valueOf(
                containerInfoGitProviderDelete.type());

        logger.info("Start of cloning [{}:{}]",
                containerInfoGitProviderDelete.type(),
                containerInfoGitProviderDelete.target());

        gitProviderRepository
                .findByTypeAndTarget(typeProvider.toString(), containerInfoGitProviderDelete.target())
                .onComplete(ar -> {
                    gitProviderRepository.closeClient();

                    if (!ar.succeeded()) {
                        logger.error("Failure (DB): " + ar.cause());
                        return;
                    }

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
                                final List<String> uriRepo = new ArrayList<>();
                                readyData.forEach((obj) -> uriRepo.add(obj.link()));

                                uriRepo.forEach((URI) -> Thread.ofVirtual()
                                        .name("git.operation-installing")
                                        .start(RepositoryInstallerRemote.taskInstallRepoRemote(URI)));
                            })
                            .onFailure(handler ->
                                    logger.error("Failure (HTTP CLIENT): " + handler.fillInStackTrace()));
                });

        event
                .response()
                .setStatusCode(HttpResponseStatus.ACCEPTED.code());
        event.next();
    }

}
